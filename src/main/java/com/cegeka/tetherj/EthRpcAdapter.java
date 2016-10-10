package com.cegeka.tetherj;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ethereum.jsonrpc.JsonRpc;

import com.cegeka.tetherj.pojo.Block;
import com.cegeka.tetherj.pojo.CompileOutput;
import com.cegeka.tetherj.pojo.FilterLogObject;
import com.cegeka.tetherj.pojo.FilterLogRequest;
import com.cegeka.tetherj.pojo.Transaction;
import com.cegeka.tetherj.pojo.TransactionCall;
import com.cegeka.tetherj.pojo.TransactionReceipt;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;

public class EthRpcAdapter implements EthRpcInterface {
	
	// Defaults for GETH RPC
    public static final String DEFAULT_HOSTNAME = Optional
        .ofNullable(System.getProperty("geth.address")).orElse("127.0.0.1");		
    public static final int DEFAULT_PORT = 8545;

    private static final Logger logger = Logger.getLogger(EthRpcAdapter.class.getName());
			
	// Available Ethereum implementations
	public enum EthAdapterType {
		GETH,
		EMBEDDED
	};
	
	@FunctionalInterface
	public interface BiFunctionWithException<T, U, R> {
		public R apply(T t, U u) throws Exception;
	}
	
	private EthRpcInterface gethRpc;
	private JsonRpc ethereumjRpc;
	
	// Implementation to be used
	private EthAdapterType adapterType;
    

    public EthRpcAdapter() {
    	this(EthAdapterType.GETH);
    }
    
    public EthRpcAdapter(String hostname, int port) {
    	this(EthAdapterType.GETH, hostname, port);
    }
    
    public EthRpcAdapter(JsonRpc rpc) {
    	this(EthAdapterType.EMBEDDED, rpc);
    }
    
	private EthRpcAdapter(EthAdapterType type, Object... hostArgs) {
		if (adapterType == EthAdapterType.GETH) {
			buildGethRpcInterface(hostArgs);
		} else {
			if (hostArgs.length > 1 && hostArgs[0] instanceof JsonRpc)
			{
				this.ethereumjRpc = (JsonRpc) hostArgs[0];
			}
			else {
				throw new IllegalArgumentException(
						"Pass in JsonRpc reference if you want to use the EthereumJ implementation");
			}
		}
	}

	private void buildGethRpcInterface(Object... hostArgs) {
		String hostname = hostArgs.length > 1 ? (String)hostArgs[0] : DEFAULT_HOSTNAME;
		int port = hostArgs.length > 1 ? (int)hostArgs[1] : DEFAULT_PORT;
		
		URL url;
		logger.log(Level.INFO, "Geth address: " + hostname + ":" + port);
		try {
		    url = new URL("http://" + hostname + ":" + port + "/");
		    JsonRpcHttpClient rpcClient = new JsonRpcHttpClient(url);
		    this.gethRpc = ProxyUtil.createClientProxy(getClass().getClassLoader(), EthRpcInterface.class,
		            rpcClient);

		} catch (MalformedURLException exception) {
		    exception.printStackTrace();
		}
	}
	
	private <T, R> R buildEthCall(Function<T, R> gethCall, Function<T, R> ethereumjCall, T arg) {
		if (this.adapterType == EthAdapterType.GETH) {
			return gethCall.apply(arg);
		} else {
			return ethereumjCall.apply(arg);
		}
	}
	
	private <T, U, R> R buildEthBiCall(BiFunction<T, U, R> gethCall, BiFunctionWithException<T, U, R> ethereumjCall, T arg0, U arg1) {
		if (this.adapterType == EthAdapterType.GETH) {
			return gethCall.apply(arg0, arg1);
		} else {
			try {
				return ethereumjCall.apply(arg0, arg1);
			} catch (Exception e) {
				// TODO: handle exceptions better
				logger.warning("Failed to call " + ethereumjCall);
				
				return null;
			}
		}
	}
	
	private <T> T buildEthNoArgCall(Supplier<T> gethCall, Supplier<T> ethereumjCall) {
		if (this.adapterType == EthAdapterType.GETH) {
			return gethCall.get();
		} else {
			return ethereumjCall.get();
		}
		
	}
	
	@Override
	public String eth_getBalance(String address, String tag) {
		return this.buildEthBiCall(this.gethRpc::eth_getBalance, this.ethereumjRpc::eth_getBalance, address, tag);
	}

	@Override
	public String[] eth_accounts() {
		return this.buildEthNoArgCall(this.gethRpc::eth_accounts, this.ethereumjRpc::eth_accounts);
	}

	@Override
	public String eth_blockNumber() {
		return this.buildEthNoArgCall(this.gethRpc::eth_blockNumber, this.ethereumjRpc::eth_blockNumber);
	}

	@Override
	public Transaction eth_getTransactionByHash(String txhash) {
		return this.buildEthCall(this.gethRpc::eth_getTransactionByHash, this.ethereumjRpc::eth_getTransactionByHash, txhash);
	}

	@Override
	public TransactionReceipt eth_getTransactionReceipt(String txhash) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eth_sendTransaction(Transaction transactions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eth_sendRawTransaction(String encoded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eth_coinbase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eth_getTransactionCount(String address, String block) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block eth_getBlockByNumber(Object string, Boolean full) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eth_call(TransactionCall txCall, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompileOutput eth_compileSolidity(String sourceCode) {
		// TODO Auto-generated method stub
		return null;
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
