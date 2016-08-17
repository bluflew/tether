package com.cegeka.tetherSample.transactions;

import com.cegeka.tetherSample.ServiceProvider;
import com.cegeka.tetherSample.wallets.Wallets;
import com.cegeka.tetherj.EthTransaction;
import com.cegeka.tetherj.EthWallet;
import com.cegeka.tetherj.Util;

/**
 * Sample to show transaction generator and signing.
 */
public class Transactions {

    public static void createFundTransaction() {
        EthWallet wallet = Wallets.generateEncryptedWallet();

        // send 2 ether from wallet to 0xbb9bc244d798123fde783fcc1c72d3bb8c189413 address
        String to = "0xbb9bc244d798123fde783fcc1c72d3bb8c189413";
        EthTransaction transaction = new EthTransaction(to, Util.fromEtherToWei(2));

        ServiceProvider.getInstance().sendTransaction()

    }
}
