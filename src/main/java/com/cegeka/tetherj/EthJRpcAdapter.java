package com.cegeka.tetherj;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.ethereum.jsonrpc.JsonRpc;
import org.ethereum.solidity.compiler.CompilationResult;
import org.ethereum.solidity.compiler.SolidityCompiler;

import com.cegeka.tetherj.pojo.Block;
import com.cegeka.tetherj.pojo.CompileOutput;
import com.cegeka.tetherj.pojo.FilterLogObject;
import com.cegeka.tetherj.pojo.FilterLogRequest;
import com.cegeka.tetherj.pojo.Transaction;
import com.cegeka.tetherj.pojo.TransactionCall;
import com.cegeka.tetherj.pojo.TransactionReceipt;

public class EthJRpcAdapter implements EthRpcInterface {
	private static final Logger logger = Logger.getLogger(EthJRpcAdapter.class.getName());
	
	/**
	 * Underlying implementation, using EthereumJ's JsonRPC(which uses an embedded EVM)
	 */
	private JsonRpc rpc;
	
	// This will be injected from a Spring context
	public EthJRpcAdapter(JsonRpc rpc) {
		this.rpc = rpc;
	}

	@Override
	public String eth_getBalance(String address, String tag) {
		try {
			return rpc.eth_getBalance(address, tag);
		} catch (Exception e) {
			logger.warning("Failed to retrieve balance for address " + address);
			return null;
		}
	}

	@Override
	public String[] eth_accounts() {
		return rpc.eth_accounts();
	}

	@Override
	public String eth_blockNumber() {
		return rpc.eth_blockNumber();
	}

	@Override
	public Transaction eth_getTransactionByHash(String txhash) {
		try {
			return new Transaction(rpc.eth_getTransactionByHash(txhash));
		} catch (Exception e) {
			logger.warning("Failed to get transaction for hash " + txhash);
			return null;
		}
	}

	@Override
	public TransactionReceipt eth_getTransactionReceipt(String txhash) {
		try {
			return new TransactionReceipt(rpc.eth_getTransactionReceipt(txhash));
		} catch (Exception e) {
			logger.warning("Failed to get transaction receipt for hash " + txhash);
			return null;
		}
	}

	@Override
	public String eth_sendTransaction(Transaction transactions) {
		try {
			return rpc.eth_sendTransaction(transactions.toCallArgs());
		} catch (Exception e) {
			logger.warning("Failed to send transaction " + transactions);
			return null;
		}
	}

	@Override
	public String eth_sendRawTransaction(String encoded) {
		try {
			return rpc.eth_sendRawTransaction(encoded);
		} catch (Exception e) {
			logger.warning("Failed to send raw tx " + encoded);
			return null;
		}
	}

	@Override
	public String eth_coinbase() {
		return rpc.eth_coinbase();
	}

	@Override
	public String eth_getTransactionCount(String address, String block) {
		try {
			return rpc.eth_getTransactionCount(address, block);
		} catch (Exception e) {
			logger.warning("Failed to get tx count for address " + address);
			return null;
		}
	}

	@Override
	public Block eth_getBlockByNumber(Object string, Boolean full) {
		try {
			return new Block(rpc.eth_getBlockByNumber(string.toString(), full));
		} catch (Exception e) {
			logger.warning("Failed to get block " + string);
			return null;
		}
	}

	@Override
	public String eth_call(TransactionCall txCall, String tag) {
		try {
			return rpc.eth_call(txCall.toCallArgs(), tag);
		} catch (Exception e) {
			logger.warning("Failed to make call " + txCall);
			return null;
		}
	}

	@Override
	public CompileOutput eth_compileSolidity(String sourceCode) {
        SolidityCompiler.Result res;
		try {
			res = SolidityCompiler.compile(sourceCode.getBytes(), true,
			        SolidityCompiler.Options.ABI, SolidityCompiler.Options.BIN,
			        SolidityCompiler.Options.INTERFACE);
		} catch (IOException e) {
			logger.warning("Failed to compile contract " + sourceCode);
			return null;
		}
        

        CompilationResult result;
		try {
			result = CompilationResult.parse(res.output);
		} catch (IOException e) {
			logger.warning("Failed to compile contract " + sourceCode);
			return null;
		}
        
        return new CompileOutput(result, sourceCode);
	}

	@Override
	public String eth_newFilter(FilterLogRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eth_newPendingTransactionFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean eth_uninstallFilter(BigInteger filterId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilterLogObject> eth_getFilterChanges(String filterId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> eth_getFilterChangesTransactions(String filterId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilterLogObject> eth_getFilterLogs(String filterId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean personal_unlockAccount(String account, String secret) {
		// TODO Auto-generated method stub
		return false;
	}

}
