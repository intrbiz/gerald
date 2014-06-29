package com.intrbiz.gerald.polyakov.transport.lamplighter.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import org.apache.log4j.Logger;

import com.intrbiz.gerald.polyakov.transport.lamplighter.LamplighterConnection;
import com.intrbiz.gerald.polyakov.transport.lamplighter.LamplighterListener;

public class LamplighterClientHandler extends SimpleChannelInboundHandler<Object>
{
    private final WebSocketClientHandshaker handshaker;
    
    private final LamplighterConnection connection;
    
    private final LamplighterListener listener;
    
    private volatile boolean handshaked = false;
    
    private Logger logger = Logger.getLogger(LamplighterClientHandler.class);

    public LamplighterClientHandler(WebSocketClientHandshaker handshaker, LamplighterConnection connection, LamplighterListener listener)
    {
        this.handshaker = handshaker;
        this.connection = connection;
        this.listener = listener;
    }
    
    public boolean isHandshaked()
    {
        return this.handshaked;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        if (logger.isTraceEnabled()) logger.trace("Disconnected from Lamplighter StationX");
        if (this.listener != null) this.listener.onDisconnect(this.connection);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete())
        {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            this.handshaked = true;
            if (logger.isTraceEnabled()) logger.trace("Connected to Lamplighter StationX");
            if (this.listener != null) this.listener.onConnect(this.connection);
        }
        else
        {
            if (msg instanceof WebSocketFrame)
            {
                if (msg instanceof TextWebSocketFrame)
                {
                    TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
                    if (logger.isTraceEnabled()) logger.trace("Got message from Lamplighter StationX");
                    if (this.listener != null) this.listener.onMessage(this.connection, textFrame.text());
                }
                else if (msg instanceof CloseWebSocketFrame)
                {
                    ch.close();
                }
            }
            else
            {
                throw new IllegalStateException("Unexpected message: " + msg);       
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        if (logger.isTraceEnabled()) logger.trace("Error communicating with Lamplighter StationX, closing connection");
        if (this.listener != null) this.listener.onError(this.connection, cause);
        ctx.channel().close();
    }
}
