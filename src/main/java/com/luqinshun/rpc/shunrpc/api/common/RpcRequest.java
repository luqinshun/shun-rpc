package com.luqinshun.rpc.shunrpc.api.common;

import lombok.Data;

/**
 * @program: shun-rpc
 * @description:
 * @author:luqinshun
 * @create: 2020-05-06 14:56
 **/
@Data
public class RpcRequest {

    private String className;

    private String methodName;

    private Object[] params;

    private Class<?>[] paramTypes;
}
