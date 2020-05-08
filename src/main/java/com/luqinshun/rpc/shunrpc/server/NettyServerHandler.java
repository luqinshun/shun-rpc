package com.luqinshun.rpc.shunrpc.server;

import com.luqinshun.rpc.shunrpc.api.common.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: shun-rpc
 * @description: 自定义处理Handler
 * @author:luqinshun
 * @create: 2020-05-06 14:08
 **/

public class NettyServerHandler extends ChannelInboundHandlerAdapter{
    private ConcurrentHashMap<String,Object> serviceBeaMap=new ConcurrentHashMap<>();

    NettyServerHandler(ConcurrentHashMap<String,Object> serviceBeaMap){
        this.serviceBeaMap=serviceBeaMap;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //序列化解析数据
        RpcRequest request= (RpcRequest) msg;
        String className = request.getClassName();
        // 将注册的对象获取到
        Object serviceBean = serviceBeaMap.get(className);
        if(serviceBean!=null){
            //实体类名
            Class<?> serviceBeanClass = serviceBean.getClass();
            //请求方法名称
            String methodName = request.getMethodName();
            //请求接口的参数类型
            Class<?>[] paramTypes = request.getParamTypes();
            //请求参数
            Object[] params = request.getParams();
            //获取到当前请求对象的方法
            Method method = serviceBeanClass.getMethod(methodName, paramTypes);
            //通过反射执行方法
            Object invoke = method.invoke(serviceBeanClass, params);
            //写会到数据
            ctx.writeAndFlush(invoke);
            return ;
        }
        super.channelRead(ctx, msg);
    }
}
