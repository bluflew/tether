package com.cegeka.blocklinks.ethereum;

import java.io.File;
import java.io.IOException;

import com.cegeka.blocklinks.ethereum.crypto.WalletStoragePojoV3;

public class EthWallet {
	WalletStoragePojoV3 storage;
	private byte[] privateKey;
	
	private EthWallet(WalletStoragePojoV3 storage) {
		privateKey = null;
		this.storage = storage;
	}
	
	public static EthWallet createWallet(String passphrase) {
		return new EthWallet(WalletStoragePojoV3.createWallet(passphrase));
	}
	
	public static EthWallet loadWalletFromString(String json) {
		return new EthWallet(WalletStoragePojoV3.loadWalletFromString(json));
	}
	
	public static EthWallet loadWalletFromFile(File file) throws IOException {
		return new EthWallet(WalletStoragePojoV3.loadWalletFromFile(file));
	}
	
	public void writeToFile(File file) throws IOException {
		storage.writeToFile(file);
	}
	
	public boolean isUnlocked() {
		return privateKey != null;
	}
	
	public boolean unlock(String passphrase) {
		privateKey = storage.getPrivateKey(passphrase);
		if (privateKey == null) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * Be careful with it
	 */
	public String getPrivateKey() {
		if (privateKey != null) {
			return com.cegeka.blocklinks.ethereum.crypto.Util.byteToHex(privateKey);
		}
		
		return null;
	}
	
	public void lock() {
		storage = null;
	}

	public WalletStoragePojoV3 getStorage() {
		return storage;
	}
}
