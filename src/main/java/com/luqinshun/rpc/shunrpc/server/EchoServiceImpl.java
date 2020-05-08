package com.luqinshun.rpc.shunrpc.server;

import com.luqinshun.rpc.shunrpc.api.common.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: shun-rpc
 * @description:
 * @author:luqinshun
 * @create: 2020-05-06 12:38
 **/
@Component
public class EchoServiceImpl implements EchoServer, ApplicationContextAware , InitializingBean {
    private ConcurrentHashMap<String,Object> serviceBeaMap=new ConcurrentHashMap<>();

    private int port=8888;

    @Override
    public void stop() {

    }

    @Override
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                //设置通道
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0,0,60));
                        //pipeline.addLast(new JSONEncoder());
                        pipeline.addLast(new NettyServerHandler(serviceBeaMap));

                    }
                });
    }

    @Override
    public void register(Class service, Class impl) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取到注解修饰的所有的接口对象
        Map<String, Object> rpcService = applicationContext.getBeansWithAnnotation(RpcService.class);
        for(Object serviceBean:rpcService.values()){
            Class<?> clazz = serviceBean.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for(Class<?> inter: interfaces){
                //接口的名称
                String name = inter.getName();
                serviceBeaMap.put(name,serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
