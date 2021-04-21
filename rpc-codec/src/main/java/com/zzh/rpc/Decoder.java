package com.zzh.rpc;

/**
 * 反序列化
 *
 */
public interface Decoder {
    <T> T decode(byte[] bytes, Class<T> clazz);
}
