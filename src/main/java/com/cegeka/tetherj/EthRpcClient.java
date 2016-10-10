package com.cegeka.tetherj;

import java.math.BigInteger;
import java.util.List;

import com.cegeka.tetherj.crypto.CryptoUtil;
import com.cegeka.tetherj.pojo.Block;
import com.cegeka.tetherj.pojo.CompileOutput;
import com.cegeka.tetherj.pojo.FilterLogObject;
import com.cegeka.tetherj.pojo.FilterLogRequest;
import com.cegeka.tetherj.pojo.Transaction;
import com.cegeka.tetherj.pojo.TransactionCall;
import com.cegeka.tetherj.pojo.TransactionReceipt;
import com.googlecode.jsonrpc4j.JsonRpcClientException;

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
    private EthRpcAdapter ethRpcAdapter;

    public EthRpcClient() {
        this.ethRpcAdapter = new EthRpcAdapter();
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
		this.ethRpcAdapter = new EthRpcAdapter(hostname, port);
    }
  
    
    /**
     * Get the ethereum client coinbase.
     *
     * @return Returns coinbase address from ethereum client.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String getCoinbase() throws JsonRpcClientException {
    	return this.ethRpcAdapter.eth_coinbase();
    }

    /**
     * Get all the wallets registed in the ethereum client.
     *
     * @return Returns accounts from ethereum client/
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public String[] getAccounts() throws JsonRpcClientException {
        return this.ethRpcAdapter.eth_accounts();
    }

    /**
     * Get latest block number.
     *
     * @return Returns latest block number.
     * @throws JsonRpcClientException
     *             In case of rpc errors.
     */
    public BigInteger getLatestBlockNumber() throws JsonRpcClientException {
        return CryptoUtil.hexToBigInteger(this.ethRpcAdapter.eth_blockNumber());
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
        String txCount = this.ethRpcAdapter.eth_getTransactionCount(address, "latest");
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
        String txCount = this.ethRpcAdapter.eth_getTransactionCount(address, "pending");
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
        return this.ethRpcAdapter.personal_unlockAccount(address, secret);
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
        boolean unlock = this.ethRpcAdapter.personal_unlockAccount(from, fromSecret);

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

        return this.ethRpcAdapter.eth_sendTransaction(transaction);
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
        return this.ethRpcAdapter.eth_sendRawTransaction(encodedSignedTransaction);
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
        String balance = this.ethRpcAdapter.eth_getBalance(address, "latest");
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
        return this.ethRpcAdapter.eth_getTransactionReceipt(txHash);
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
        return this.ethRpcAdapter.eth_getTransactionByHash(txHash);
    }

    /**
     * Call a contract method or dry call it.
     *
     * @param call
     *            to make
     * @return output encoded
     */
    public String callMethod(TransactionCall call) {
        return this.ethRpcAdapter.eth_call(call, "latest");
    }

    /**
     * Call a contract method or dry call it.
     *
     * @param call
     *            to make
     * @return output encoded
     */
    public String callMethod(EthCall call) {
        return this.ethRpcAdapter.eth_call(call.getCall(), "latest");
    }

    /**
     * Get latest block from on ethereum client.
     *
     * @return The latest block object
     */
    public Block getLatestBlock() {
        return this.ethRpcAdapter.eth_getBlockByNumber("latest", true);
    }

    /**
     * Get latest block gas limit.
     *
     * @return The gas limit of latest block on ethereum client
     */
    public BigInteger getLatestBlockGasLimit() {
        Block block = this.ethRpcAdapter.eth_getBlockByNumber("latest", true);
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
        return this.ethRpcAdapter.eth_compileSolidity(sourceCode);
    }

    /**
     * Create an ethereum filter.
     *
     * @return Returns filter id from ethereum client.
     */
    public String newFilter() {
        return this.ethRpcAdapter.eth_newFilter(FilterLogRequest.DEFAULT);
    }

    /**
     * Create an ethereum filter.
     *
     * @param filterLogRequest
     *            The filter log request to send.
     * @return Returns filter id from ethereum client.
     */
    public String newFilter(FilterLogRequest filterLogRequest) {
        return this.ethRpcAdapter.eth_newFilter(filterLogRequest);
    }

    /**
     * Create an ethereum filter for pending transactions.
     *
     * @return Returns filter id from ethereum client.
     */
    public String newPendingTransactionFilter() {
        return this.ethRpcAdapter.eth_newPendingTransactionFilter();
    }

    /**
     * Remove ethereum filter from ethereum client.
     *
     * @param filterId
     *            Filter to be removed.
     * @return Returns true for success.
     */
    public Boolean uninstallFilter(BigInteger filterId) {
        return this.ethRpcAdapter.eth_uninstallFilter(filterId);
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            Filter to check.
     * @return Retuns the filter log object.
     */
    public List<FilterLogObject> getFilterChanges(String filterId) {
        return this.ethRpcAdapter
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
        return this.ethRpcAdapter.eth_getFilterChanges("0x" + filterId.toString(16));
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            filter id to fetch changes of.
     * @return Returns a filter log object
     */
    public List<String> getPendingTransactionFilterChanges(String filterId) {
        return this.ethRpcAdapter.eth_getFilterChangesTransactions(filterId);
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            id to fetch changes of
     * @return a filter log object
     */
    public List<String> getPendingTransactionFilterChanges(BigInteger filterId) {
        return this.ethRpcAdapter.eth_getFilterChangesTransactions("0x" + filterId.toString(16));
    }

    /**
     * Get ethereum filter changes.
     *
     * @param filterId
     *            Filter to check.
     * @return Retuns the filter log object.
     */
    public List<FilterLogObject> getFilterLogs(String filterId) {
        return this.ethRpcAdapter
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
        return this.ethRpcAdapter.eth_getFilterLogs("0x" + filterId.toString(16));
    }
}
