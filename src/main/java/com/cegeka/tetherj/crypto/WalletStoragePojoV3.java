package com.cegeka.tetherj.crypto;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.ethereum.tether.crypto.ECKey;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * Wallet v3 storage pojo and manager.
 *
 * @author Andrei Grigoriu
 */
public class WalletStoragePojoV3 implements Serializable {

    public static final int STORAGE_VERSION = 3;
    public static final String KDF = "pbkdf2";
    public static final int DKLEN = 32;
    public static final int ITERATIONS = 262144;
    public static final String PRF = "hmac-sha256";
    public static final String CIPHER = "aes-128-ctr";
    private static final long serialVersionUID = 4140390043976265580L;
    private String address;
    private WalletCryptoPojoV3 crypto;
    private String id;
    private int version;

    private WalletStoragePojoV3() {
    }

    /**
     * Create a random wallet (new keys pairs).
     *
     * @param passphrase to encrypt private key with
     * @return storage
     */
    public static WalletStoragePojoV3 createWallet(String passphrase) {
        WalletStoragePojoV3 wallet = new WalletStoragePojoV3();
        wallet.version = STORAGE_VERSION;
        wallet.id = UUID.randomUUID().toString();

        ECKey ecdsaPair = new ECKey();

        try {
            wallet.address = CryptoUtil.byteToHex(ecdsaPair.getAddress());

            WalletCryptoPojoV3 crypto = new WalletCryptoPojoV3();
            wallet.crypto = crypto;
            crypto.setCipher(CIPHER);

            // create key to crypt private key with AES
            // key will be a derived hash

            byte[] saltBytes = new byte[32];
            Random saltRandom = new Random();
            saltRandom.nextBytes(saltBytes);
            String salt = CryptoUtil.byteToHex(saltBytes);

            crypto.setKdf(KDF);
            crypto.getKdfparams().setSalt(salt);
            crypto.getKdfparams().setDklen(DKLEN);
            crypto.getKdfparams().setPrf(PRF);
            crypto.getKdfparams().setC(ITERATIONS);

            byte[] key = Pbkdf2.derive(passphrase, saltBytes, crypto.getKdfparams().getC(), crypto.getKdfparams().getDklen());

            // select AES algorithm
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            // key will only be the first 16 bytes of the hash key
            byte[] trimmedKey = Arrays.copyOfRange(key, 0, 16);

            // macKey that will be used to validate wallet unlocking (as per
            // ethereum standard)
            byte[] macKey = Arrays.copyOfRange(key, 16, 32);

            // crypt using AES and get generated IV
            SecretKeySpec secretKeySpec = new SecretKeySpec(trimmedKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] privateKeyBytes = ecdsaPair.getPrivKeyBytes();
            byte[] ciphertext = cipher.doFinal(privateKeyBytes);

            // generate MAC as per ethereum standard
            KeccakDigest md = new KeccakDigest(256);
            byte[] macSource = new byte[macKey.length + ciphertext.length];
            System.arraycopy(macKey, 0, macSource, 0, macKey.length);
            System.arraycopy(ciphertext, 0, macSource, macKey.length, ciphertext.length);

            md.update(macSource, 0, macSource.length);
            byte[] mac = new byte[md.getDigestSize()];
            md.doFinal(mac, 0);

            // set in storage
            crypto.setMac(CryptoUtil.byteToHex(mac));
            byte[] iv = cipher.getIV();
            crypto.getCipherparams().setIv(CryptoUtil.byteToHex(iv));
            crypto.setCiphertext(CryptoUtil.byteToHex(ciphertext));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return wallet;
    }

    /**
     * Create storage from json string.
     *
     * @param json to create storage from
     * @return storage object
     */
    public static WalletStoragePojoV3 loadWalletFromString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, WalletStoragePojoV3.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Creates storage from private key. Storage will lack encrypted part.
     *
     * @param privateKey to generate storage from.
     * @return storage instance
     */
    public static WalletStoragePojoV3 createFromPrivateKey(byte[] privateKey) {
        WalletStoragePojoV3 wallet = new WalletStoragePojoV3();
        wallet.version = STORAGE_VERSION;
        wallet.id = UUID.randomUUID().toString();
        wallet.crypto = null;

        ECKey key = ECKey.fromPrivate(privateKey);
        wallet.address = CryptoUtil.byteToHex(key.getAddress());

        return wallet;
    }

    /**
     * Create storage from file.
     *
     * @param wallet to read and deserialize storage from
     * @return storage object
     */
    public static WalletStoragePojoV3 loadWalletFromFile(File wallet) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(wallet, WalletStoragePojoV3.class);
        } catch (JsonParseException | JsonMappingException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Serialize to file.
     *
     * @param file to write to
     * @throws IOException in case write fails
     */
    public void writeToFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, this);
        } catch (JsonGenerationException | JsonMappingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Serialize to String.
     *
     * @return serialized json
     */
    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Decrypt private key.
     *
     * @param passphrase to decrypt
     * @return private key data
     */
    public byte[] getPrivateKey(String passphrase) {
        if (crypto.getCipher().equals(CIPHER) && crypto.getKdf().equals(KDF)) {
            byte[] key = Pbkdf2.derive(passphrase, CryptoUtil.hexToBytes(crypto.getKdfparams().getSalt()),
                    crypto.getKdfparams().getC(), crypto.getKdfparams().getDklen());
            try {
                // macKey that will be used to validate wallet unlocking (as per
                // ethereum standard)
                byte[] macKey = Arrays.copyOfRange(key, 16, 32);

                byte[] ciphertext = CryptoUtil.hexToBytes(crypto.getCiphertext());
                // generate MAC as per ethereum standard
                KeccakDigest md = new KeccakDigest(256);
                byte[] macSource = new byte[macKey.length + ciphertext.length];
                System.arraycopy(macKey, 0, macSource, 0, macKey.length);
                System.arraycopy(ciphertext, 0, macSource, macKey.length, ciphertext.length);

                md.update(macSource, 0, macSource.length);
                byte[] mac = new byte[md.getDigestSize()];
                md.doFinal(mac, 0);

                if (!CryptoUtil.byteToHex(mac).equals(crypto.getMac())) {
                    // MAC MISMATCH
                    return null;
                }

                // key will only be the first 16 bytes of the hash key
                byte[] trimmedKey = Arrays.copyOfRange(key, 0, 16);
                SecretKeySpec secretKeySpec = new SecretKeySpec(trimmedKey, "AES");
                byte[] ivAsBytes = CryptoUtil.hexToBytes(crypto.getCipherparams().getIv());
                IvParameterSpec iv = new IvParameterSpec(ivAsBytes);

                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
                byte[] privateKey = cipher.doFinal(CryptoUtil.hexToBytes(crypto.getCiphertext()));

                return privateKey;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Wallet is incompatible or corrupted!");
        }

        return null;
    }

    public WalletCryptoPojoV3 getCrypto() {
        return crypto;
    }

    public void setCrypto(WalletCryptoPojoV3 crypto) {
        this.crypto = crypto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
