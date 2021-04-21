package com.zzh.rpc.server;

import com.zzh.rpc.Decoder;
import com.zzh.rpc.Encoder;
import com.zzh.rpc.Request;
import com.zzh.rpc.Response;
import com.zzh.rpc.common.utils.ReflectionUtils;
import com.zzh.rpc.transport.RequestHandler;
import com.zzh.rpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class RpcServer {
    private RpcServerConfig config;
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInvoker serviceInvoker;

    public RpcServer() {
        this(new RpcServerConfig());
    }

    public RpcServer(RpcServerConfig config){
        this.config = config;

        // net
        this.net = ReflectionUtils.newInstance(
                config.getTransportClass()
        );
        this.net.init(config.getPort(), this.handler);

        // codec
        this.encoder = ReflectionUtils.newInstance(
                config.getEncodeClass()
        );
        this.decoder = ReflectionUtils.newInstance(
                config.getDecoderClass()
        );

        // service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();

    }

    public <T> void register(Class<T> interfaceClass, T bean){
        serviceManager.register(interfaceClass, bean);
    }

    public void start(){
        this.net.start();
    }

    public void stop(){
        this.net.stop();
    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequest(InputStream receive, OutputStream toResp) {
            Response resp = new Response();
            try {
                byte[] inBytes = IOUtils.readFully(receive, receive.available());
                Request request = decoder.decode(inBytes, Request.class);
                log.info("get request: {}", request);

                ServiceInstance sis = serviceManager.lookup(request);
                Object ret = serviceInvoker.invoke(sis, request);
                resp.setData(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                resp.setCode(1);
                resp.setMessage("RpcServer got error: "
                        + e.getClass().getName()
                        + " : " + e.getMessage());
            } finally {
                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);

                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    };
}
