package com.luqinshun.rpc.shunrpc.server;

/**
 * 服务器
 */
public interface EchoServer {

    void stop();

    void start();

    void register(Class service,Class impl);

}
