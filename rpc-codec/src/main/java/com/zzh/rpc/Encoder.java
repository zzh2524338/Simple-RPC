package com.zzh.rpc;

/**
 * 序列化
 */
public interface Encoder {
    byte[] encode(Object obj);
}
