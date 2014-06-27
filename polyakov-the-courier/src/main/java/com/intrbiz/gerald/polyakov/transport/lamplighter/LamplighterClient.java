package com.intrbiz.gerald.polyakov.transport.lamplighter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.net.URI;

import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.io.PolyakovTranscoder;
import com.intrbiz.gerald.polyakov.transport.lamplighter.netty.LamplighterClientHandler;

public class LamplighterClient
{
    private static LamplighterClient DEFAULT_INSTANCE = null;
    
    public static LamplighterClient getDefaultInstance()
    {
        synchronized (LamplighterClient.class)
        {
            if (DEFAULT_INSTANCE == null) DEFAULT_INSTANCE = new LamplighterClient();
            return DEFAULT_INSTANCE;
        }
    }
    
    private EventLoopGroup group = new NioEventLoopGroup();
    
    public LamplighterClient()
    {
        super();
    }
    
    public void shutdown()
    {
        this.group.shutdownGracefully();
    }
    
    public LamplighterConnection connect(URI uri, String key, LamplighterListener listener) throws Exception
    {
        if (isSecure(uri)) throw new IllegalArgumentException("TLS not yet supported");
        return new DefaultLamplighterConnection(listener).connect(uri, key);
    }
    
    private static int getPort(URI uri)
    {
        if (uri.getPort() == -1)
        {
            if ("lamplighter".equals(uri.getScheme()))   return 8825;
            if ("http".equals(uri.getScheme()))  return 80;
            if ("https".equals(uri.getScheme())) return 443;
            if ("ws".equals(uri.getScheme()))    return 80;
            if ("wss".equals(uri.getScheme()))   return 443;
        }
        return uri.getPort();
    }
    
    private static boolean isSecure(URI uri)
    {
        return "lamplighter".equals(uri.getScheme()) || "wss".equals(uri.getScheme());
    }
    
    /*
     * Our connection object
     */
    private class DefaultLamplighterConnection implements LamplighterConnection
    {
        private final LamplighterListener listener;
        
        private LamplighterClientHandler handler;
        
        private Channel channel;
        
        private PolyakovTranscoder transcoder = new PolyakovTranscoder();
        
        public DefaultLamplighterConnection(LamplighterListener listener)
        {
            this.listener = listener;
        }
        
        public DefaultLamplighterConnection connect(URI uri, String key)
        {
            // headers
            HttpHeaders headers = new DefaultHttpHeaders();
            headers.add("User-Agent",            "Lamplighter-Client");
            headers.add("X-Lamplighter-Version", "1.0.0");
            headers.add("X-Lamplighter-Format",  "JSON");
            headers.add("X-Lamplighter-Key",     key == null ? "" : key);
            // the handler
            this.handler = new LamplighterClientHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, false, headers, 1 * 1024 * 1024),
                    this,
                    this.listener
            );
            // bootstrap
            Bootstrap b = new Bootstrap()
            .group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                protected void initChannel(SocketChannel ch)
                {
                    ch.pipeline().addLast(
                            new HttpClientCodec(), 
                            new HttpObjectAggregator(1 * 1024 * 1024), 
                            handler
                    );
                }
            });
            // start the connect
            final ChannelFuture connectFuture = b.connect(uri.getHost(), getPort(uri));
            this.channel = connectFuture.channel();
            // connect listener
            connectFuture.addListener(new GenericFutureListener<ChannelFuture>()
            {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception
                {
                    if (future.isDone() && (!future.isSuccess()))
                    {
                        if (listener != null) listener.onError(DefaultLamplighterConnection.this, future.cause());
                    }
                }
            });
            // ok we should be connecting
            return this;
        }
        
        @Override
        public boolean isConnected()
        {
            return this.handler.isHandshaked() && this.channel.isActive();
        }
        
        @Override
        public void send(Object message) throws IOException
        {
            if (! this.isConnected()) throw new IllegalStateException("The connection is not connected, cannot send");
            // encode parcel to JSON
            if (message instanceof Parcel)
            {
                this.channel.writeAndFlush(new TextWebSocketFrame(this.transcoder.encodeAsString(message)));
            }
        }

        @Override
        public void close()
        {
            if (this.channel.isActive()) this.channel.close();
        }
    }
}
