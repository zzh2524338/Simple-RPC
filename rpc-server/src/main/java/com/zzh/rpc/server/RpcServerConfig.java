package com.zzh.rpc.server;

import com.zzh.rpc.Decoder;
import com.zzh.rpc.Encoder;
import com.zzh.rpc.JSONDecoder;
import com.zzh.rpc.JSONEncoder;
import com.zzh.rpc.transport.HTTPTransportServer;
import com.zzh.rpc.transport.TransportServer;
import lombok.Data;

/**
 * server配置
 */
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class<? extends Encoder> encodeClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 3000;
}
