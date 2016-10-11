package com.cegeka.tetherj.crypto;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.ethereum.tether.crypto.ECKey;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

/**
 * Wallet v3 storage pojo and manager.
 *
 * @author Andrei Grigoriu
 *
 */
@Data
public class WalletStoragePojoV3 implements Serializable {

    private static final long serialVersionUID = 4140390043976265580L;

    public static final int storageVersion = 3;
    public static final String kdf = "pbkdf2";
    public static final int dklen = 32;
    public static final int iterations = 262144;
    public static final String prf = "hmac-sha256";
    public static final String cipher = "aes-128-ctr";

    String address;
    WalletCryptoPojoV3 crypto;
    String id;
    int version;

    private WalletStoragePojoV3() {
    }

    /**
     * Create a random wallet (new keys pairs).
     *
     * @param passphrase
     *            to encrypt private key with
     * @return storage
     */
    public static WalletStoragePojoV3 createWallet(String passphrase) {
        WalletStoragePojoV3 wallet = new WalletStoragePojoV3();
        wallet.version = storageVersion;
        wallet.id = UUID.randomUUID().toString();

        ECKey ecdsaPair = new ECKey();

        try {
            wallet.address = CryptoUtil.byteToHex(ecdsaPair.getAddress());

            WalletCryptoPojoV3 crypto = new WalletCryptoPojoV3();
            wallet.crypto = crypto;
            crypto.cipher = cipher;

            // create key to crypt private key with AES
            // key will be a derived hash

            byte[] saltBytes = new byte[32];
            Random saltRandom = new Random();
            saltRandom.nextBytes(saltBytes);
            String salt = CryptoUtil.byteToHex(saltBytes);

            crypto.kdf = kdf;
            crypto.kdfparams.salt = salt;
            crypto.kdfparams.dklen = dklen;
            crypto.kdfparams.prf = prf;
            crypto.kdfparams.c = iterations;

            byte[] key = Pbkdf2.derive(passphrase, saltBytes, crypto.kdfparams.c,
                    crypto.kdfparams.dklen);

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
            crypto.mac = CryptoUtil.byteToHex(mac);
            byte[] iv = cipher.getIV();
            crypto.cipherparams.iv = CryptoUtil.byteToHex(iv);
            crypto.ciphertext = CryptoUtil.byteToHex(ciphertext);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return wallet;
    }

    /**
     * Create storage from json string.
     *
     * @param json
     *            to create storage from
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
     * @param privateKey to generate storage from.
     * @return storage instance
     */
    public static WalletStoragePojoV3 createFromPrivateKey(byte[] privateKey) {
        WalletStoragePojoV3 wallet = new WalletStoragePojoV3();
        wallet.version = storageVersion;
        wallet.id = UUID.randomUUID().toString();
        wallet.crypto = null;

        ECKey key = ECKey.fromPrivate(privateKey);
        wallet.address = CryptoUtil.byteToHex(key.getAddress());

        return wallet;
    }

    /**
     * Create storage from file.
     *
     * @param wallet
     *            to read and deserialize storage from
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

    /**
     * Serialize to file.
     *
     * @param file
     *            to write to
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
     * @param passphrase
     *            to decrypt
     * @return private key data
     */
    public byte[] getPrivateKey(String passphrase) {
        if (crypto.cipher.equals(cipher) && crypto.kdf.equals(kdf)) {
            byte[] key = Pbkdf2.derive(passphrase, CryptoUtil.hexToBytes(crypto.kdfparams.salt),
                    crypto.kdfparams.c, crypto.kdfparams.dklen);
            try {
                // macKey that will be used to validate wallet unlocking (as per
                // ethereum standard)
                byte[] macKey = Arrays.copyOfRange(key, 16, 32);

                byte[] ciphertext = CryptoUtil.hexToBytes(crypto.ciphertext);
                // generate MAC as per ethereum standard
                KeccakDigest md = new KeccakDigest(256);
                byte[] macSource = new byte[macKey.length + ciphertext.length];
                System.arraycopy(macKey, 0, macSource, 0, macKey.length);
                System.arraycopy(ciphertext, 0, macSource, macKey.length, ciphertext.length);

                md.update(macSource, 0, macSource.length);
                byte[] mac = new byte[md.getDigestSize()];
                md.doFinal(mac, 0);

                if (!CryptoUtil.byteToHex(mac).equals(crypto.mac)) {
                    // MAC MISMATCH
                    return null;
                }

                // key will only be the first 16 bytes of the hash key
                byte[] trimmedKey = Arrays.copyOfRange(key, 0, 16);
                SecretKeySpec secretKeySpec = new SecretKeySpec(trimmedKey, "AES");
                byte[] ivAsBytes = CryptoUtil.hexToBytes(crypto.cipherparams.iv);
                IvParameterSpec iv = new IvParameterSpec(ivAsBytes);

                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
                byte[] privateKey = cipher.doFinal(CryptoUtil.hexToBytes(crypto.ciphertext));

                return privateKey;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("Wallet is incompatible or corrupted!");
        }

        return null;
    }
}
