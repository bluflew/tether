package com.cegeka.tetherSample.wallets;

import com.cegeka.tetherSample.ServiceProvider;
import com.cegeka.tetherj.EthWallet;

import java.io.File;
import java.io.IOException;

/**
 * Sample class to show wallet generator.
 */
public class Wallets {

    public static EthWallet generateEncryptedWallet() {
        EthWallet wallet = ServiceProvider.getInstance().createWallet("pass1234");
        return wallet;
    }

    public static void writeWalletToFile() {
        EthWallet wallet = generateEncryptedWallet();

        try {

            // write to personal file
            File file = new File("~/tetherWallet");
            wallet.writeToFile(file);

            // write to standard geth file
            File gethFile = new File("~/.ethereum/keystore/" + wallet.generateStandardFilename());
            wallet.writeToFile(gethFile);


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
