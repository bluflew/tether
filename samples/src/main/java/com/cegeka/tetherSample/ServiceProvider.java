package com.cegeka.tetherSample;

import com.cegeka.tetherj.api.EthereumService;

/**
 * Singleton class to provide service.
 */
public class ServiceProvider {

    private static EthereumService service = null;

    public static EthereumService getInstance() {
        if (service == null) {
            // start service on two threads, connect to ethereum client localhost:8545
            service = new EthereumService(2, "localhost", 8545);
        }

        return service;
    }
}
