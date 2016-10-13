package com.cegeka.tetherj;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ethereum.jsonrpc.JsonRpc;

import com.cegeka.tetherj.crypto.CryptoUtil;
import com.cegeka.tetherj.pojo.Block;
import com.cegeka.tetherj.pojo.CompileOutput;
import com.cegeka.tetherj.pojo.FilterLogObject;
import com.cegeka.tetherj.pojo.FilterLogRequest;
import com.cegeka.tetherj.pojo.Transaction;
import com.cegeka.tetherj.pojo.TransactionCall;
import com.cegeka.tetherj.pojo.TransactionReceipt;
import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

/**
 * Class for rpc request invoker to ethereum client.
 *
 * @author Andrei Grigoriu
 *
 */
public class EthRpcClient {
    /**
     * Ethereum rpc interface.
     */
    private EthRpcInterface rpc;
    
    public static final String DEFAULT_HOSTNAME = Optional
            .ofNullable(System.getProperty("geth.address")).orElse("127.0.0.1");
    
    public static final int DEFAULT_PORT = 8545;
    private static final Logger logger = Logger.getLogger(EthRpcClient.class.getName());

    public EthRpcClient() {
        this(DEFAULT_HOSTNAME, DEFAULT_PORT);
    }

    /**
     * Constructor to specify hostname and port for ethereum client.
     *
     * @param hostname
     *            Hostname for ethereum client.
     * @param port
     *            Port for ethereum client.
     */
    public EthRpcClient(String hostname, int port) {
        URL url;
        logger.log(Level.INFO, "Geth address: " + hostname + ":" + port);
        try {
            url = new URL("http://" + hostname + ":" + port + "/");
            JsonRpcHttpClient rpcClient = new JsonRpcHttpClient(url);
            
            rpc = ProxyUtil.createClientProxy(getClass().getClassLoader(), EthRpcInterface.class,
                    rpcClient);

        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
    }
    
    public EthRpcClient(JsonRpc rpc) {
    	// We will use the embedded EVM from ethereumj
    	this.rpc = new EthJRpcAdapter(rpc);
    }
    
    /**
     * Get the ethereum client coinbase.
     *
     * @return Returns coinbase address from ethereum client.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String getCoinbase() throws JsonRpcClientException {
    	return rpc.eth_coinbase();
    }

    /**
     * Get all the wallets registed in the ethereum client.
     *
     * @return Returns accounts from ethereum client/
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String[] getAccounts() throws JsonRpcClientException {
        return rpc.eth_accounts();
    }

    /**
     * Get latest block number.
     *
     * @return Returns latest block number.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public BigInteger getLatestBlockNumber() throws JsonRpcClientException {
        return CryptoUtil.hexToBigInteger(rpc.eth_blockNumber());
    }

    /**
     * This get only counts mined transactions.
     *
     * @param address
     *            Address to get transaction count for.
     * @return Returns nonce of address based on mined transactions.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public BigInteger getAccountNonce(String address) throws JsonRpcClientException {
        String txCount = rpc.eth_getTransactionCount(address, "latest");
        return CryptoUtil.hexToBigInteger(txCount);
    }

    /**
     * This get also counts pending transactions.
     *
     * @param address
     *            Address to get transaction count for.
     * @return Returns nonce of address based on mined transactions.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public BigInteger getAccountNonceWithPending(String address) throws JsonRpcClientException {
        String txCount = rpc.eth_getTransactionCount(address, "pending");
        return CryptoUtil.hexToBigInteger(txCount);
    }

    /**
     * EXPERIMENTAL (should not be used), only works with custom ethereum clients.
     *
     * @param address
     *            Address from the ethereum client to unlock.
     * @param secret
     *            Passphrase to unlock with.
     * @return true if unlocked
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public boolean unlockAccount(String address, String secret) throws JsonRpcClientException {
        return rpc.personal_unlockAccount(address, secret);
    }

    /**
     * EXPERIMENTAL (should not be used), only works with custom ethereum clients.
     *
     * @param from
     *            Address from ethereum client to send from.
     * @param fromSecret
     *            Unlock with this passphrase.
     * @param to
     *            Address to send to.
     * @param valueWei
     *            Total wei to send.
     * @return Transaction hash from ethereum client.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String sendTransaction(String from, String fromSecret, String to, BigInteger valueWei)
            throws JsonRpcClientException {
        boolean unlock = rpc.personal_unlockAccount(from, fromSecret);

        if (unlock) {
            return this.sendTransaction(from, to, valueWei);
        }
        return null;
    }

    /**
     * Send transaction from already unlocked accounts.
     *
     * @param from
     *            Address to send from.
     * @param to
     *            Address to send to.
     * @param valueWei
     *            Wei to send
     * @return Transaction hash.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String sendTransaction(String from, String to, BigInteger valueWei)
            throws JsonRpcClientException {
        Transaction transaction = new Transaction();

        transaction.setFrom(from.toString());
        transaction.setTo(to);
        transaction.setValue(valueWei.toString());

        return rpc.eth_sendTransaction(transaction);
    }

    /**
     * Send self encoded transaction. The safest way to rpc send transactions.
     *
     * @param encodedSignedTransaction
     *            encoded data as hex
     * @return transaction hash
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String sendRawTransaction(String encodedSignedTransaction)
            throws JsonRpcClientException {
        return rpc.eth_sendRawTransaction(encodedSignedTransaction);
    }

    /**
     * Send self encoded transaction. The safest way to rpc send transactions.
     *
     * @param encodedSignedTransaction
     *            encoded data
     * @return transaction hash
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String sendRawTransaction(byte[] encodedSignedTransaction)
            throws JsonRpcClientException {
        return sendRawTransaction(CryptoUtil.byteToHex(encodedSignedTransaction));
    }

    /**
     * Get balance of address.
     *
     * @param address
     *            Address to get balance of.
     * @return balance as wei
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public BigInteger getBalance(String address) throws JsonRpcClientException {
        String balance = rpc.eth_getBalance(address, "latest");
        return CryptoUtil.hexToBigInteger(balance);
    }

    /**
     * Returns the transaction receipt, null if the transaction is not mined.
     *
     * @param txHash
     *            to get receipt of
     * @return receipt
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public TransactionReceipt getTransactionReceipt(String txHash) throws JsonRpcClientException {
        return rpc.eth_getTransactionReceipt(txHash);
    }

    /**
     * Get transaction data by transaction hash/
     *
     * @param txHash
     *            to get data by.
     * @return transaction data.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public Transaction getTransaction(String txHash) throws JsonRpcClientException {
        return rpc.eth_getTransactionByHash(txHash);
    }

    /**
     * Call a contract method or dry call it.
     *
     * @param call
     *            to make
     * @return output encoded
     */
    public String callMethod(TransactionCall call) {
        return rpc.eth_call(call, "latest");
    }

    /**
     * Call a contract method or dry call it.
     *
     * @param call
     *            to make
     * @return output encoded
     */
    public String callMethod(EthCall call) {
        return rpc.eth_call(call.getCall(), "latest");
    }

    /**
     * Get latest block from on ethereum client.
     *
     * @return The latest block object
     */
    public Block getLatestBlock() {
        return rpc.eth_getBlockByNumber("latest", true);
    }

    /**
     * Get latest block gas limit.
     *
     * @return The gas limit of latest block on ethereum client
     */
    public BigInteger getLatestBlockGasLimit() {
        Block block = rpc.eth_getBlockByNumber("latest", true);
        if (block != null) {
            return CryptoUtil.hexToBigInteger(block.gasLimit);
        }

        return null;
    }

    /**
     * Compile solidity source on ethereum client and return a compile output.
     *
     * @param sourceCode
     *            Source code to compile.
     * @return Compile output.
     */
    public CompileOutput compileSolidity(String sourceCode) {
        return rpc.eth_compileSolidity(sourceCode);
    }

    /**
     * Create an ethereum filter.
     *
     * @return Returns filter id from ethereum client.
     */
    public String newFilter() {
        return rpc.eth_newFilter(FilterLogRequest.DEFAULT);
    }

    /**
     * Create an ethereum filter.
     *
     * @param filterLogRequest
     *            The filter log request to send.
     * @return Returns filter id from ethereum client.
     */
    public String newFilter(FilterLogRequest filterLogRequest) {
        return rpc.eth_newFilter(filterLogRequest);
    }

    /**
     * Create an ethereum filter for pending transactions.
     *
     * @return Returns filter id from ethereum client.
     */
    public String newPendingTransactionFilter() {
        return rpc.eth_newPendingTransactionFilter();
    }

    /**
     * Remove ethereum filter from ethereum client.
     *
     * @param filterId
     *            Filter to be removed.
     * @return Returns true for success.
     */
    public Boolean uninstallFilter(BigInteger filterId) {
        return rpc.eth_uninstallFilter(filterId);
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            Filter to check.
     * @return Retuns the filter log object.
     */
    public List<FilterLogObject> getFilterChanges(String filterId) {
        return rpc
                .eth_getFilterChanges(filterId);
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            id to fetch changes of.
     * @return Returns a filter log object.
     */
    public List<FilterLogObject> getFilterChanges(BigInteger filterId) {
        return rpc.eth_getFilterChanges("0x" + filterId.toString(16));
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            filter id to fetch changes of.
     * @return Returns a filter log object
     */
    public List<String> getPendingTransactionFilterChanges(String filterId) {
        return rpc.eth_getFilterChangesTransactions(filterId);
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            id to fetch changes of
     * @return a filter log object
     */
    public List<String> getPendingTransactionFilterChanges(BigInteger filterId) {
        return rpc.eth_getFilterChangesTransactions("0x" + filterId.toString(16));
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            Filter to check.
     * @return Retuns the filter log object.
     */
    public List<FilterLogObject> getFilterLogs(String filterId) {
        return rpc
                .eth_getFilterLogs(filterId);
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            id to fetch changes of.
     * @return Returns a filter log object.
     */
    public List<FilterLogObject> getFilterLogs(BigInteger filterId) {
        return rpc.eth_getFilterLogs("0x" + filterId.toString(16));
    }
    
    /**
     * Mines a block. Works only on the embedded EVM.
     */
    public String mineBlock() {
    	if (this.rpc instanceof EthJRpcAdapter) {
            String blockFilterId = rpc.eth_newBlockFilter();
            
            rpc.miner_start();
            
            int cnt = 0;
            String newBlockHash;
            while (true) {
                List<String> blocks = rpc.eth_getFilterChangesTransactions(blockFilterId);
                cnt += blocks.size();
                if (cnt > 0) {
                    newBlockHash = blocks.get(0);
                    break;
                }
                try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					logger.info("Interrupted sleep");
				}
            }
            
            rpc.miner_stop();
            
            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.info("Interrupted sleep");
			}
            
            List<String> blocks = rpc.eth_getFilterChangesTransactions(blockFilterId);
            cnt += blocks.size();
            logger.info(cnt + " blocks mined");
            rpc.eth_uninstallFilter(blockFilterId);
            
            return newBlockHash;
    	} else {
    		throw new UnsupportedOperationException("Cannot ask Geth to mine block, that's not how this works");
    	}
    }
}
