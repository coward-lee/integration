package org.lee;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Main {
    public static void main(String[] args) throws IOException {     // 连接以太坊节点
        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/556e1d4638914dc19955654571dce5c7"));

        // 加载合约
        String contractAddress = "0x..."; // 合约地址
        Credentials credentials = Credentials.create("YOUR_PRIVATE_KEY"); // 账户私钥
        YourContract contract = YourContract.load(contractAddress, web3j, credentials, new DefaultGasProvider());

        // 调用只读方法
        BigInteger value = contract.getBalance("0x...").send();
        System.out.println("Balance: " + value);

        // 调用写入方法
        TransactionReceipt receipt = contract.setValue(new BigInteger("100")).send();
        System.out.println("Transaction Hash: " + receipt.getTransactionHash());
    }

    public static void blockNum() throws IOException {

        String url = "https://mainnet.infura.io/v3/556e1d4638914dc19955654571dce5c7";
        OkHttpClient httpClient = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(7890))).build();
        Web3j web3j = Web3j.build(new HttpService(url,httpClient));
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        BigInteger blockNumber = ethBlockNumber.getBlockNumber();
        System.out.println(blockNumber);
    }
}