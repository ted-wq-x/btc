package com.go2going.okcoin.websocket;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    private MoniterTask moniter;
    private WebSocketService service ;
    private WebSocketBase webSocketBase;

    /**
     *
     * @param handshaker
     * @param service
     * @param moniter 用于更新时间
     */
    public WebSocketClientHandler(WebSocketClientHandshaker handshaker,WebSocketService service,MoniterTask moniter,WebSocketBase webSocketBase) {
        this.handshaker = handshaker;
        this.service = service;
        this.moniter = moniter;
        this.webSocketBase = webSocketBase;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.info("WebSocket Client disconnected!");
        webSocketBase.reConnect();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        moniter.updateTime();
        //手动的进行websocket协议升级
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            LOGGER.info("WebSocket Client connected!");
            handshakeFuture.setSuccess();
            return;
        }
        //如果是http返回的结果，抛出异常
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.getStatus() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
        //将消息装换成websocket帧
        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            service.onReceive(textFrame.text());
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame=(BinaryWebSocketFrame)frame;
            service.onReceive(decodeByteBuff(binaryFrame.content()));
        }else if (frame instanceof PongWebSocketFrame) {
            LOGGER.info("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            LOGGER.info("WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        LOGGER.error("ERROR:"+cause.getMessage());
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    /**
     * 解压数据
     * @param buf
     * @return
     * @throws IOException
     * @throws DataFormatException
     */
    public  String decodeByteBuff(ByteBuf buf) throws IOException, DataFormatException {

        byte[] temp = new byte[buf.readableBytes()];
        ByteBufInputStream bis = new ByteBufInputStream(buf);
        bis.read(temp);
        bis.close();
        Inflater decompresser = new Inflater(true);
        decompresser.setInput(temp, 0, temp.length);
        StringBuilder sb = new StringBuilder();
        byte[] result = new byte[1024];
        while (!decompresser.finished()) {
            int resultLength = decompresser.inflate(result);
            sb.append(new String(result, 0, resultLength, "UTF-8"));
        }
        decompresser.end();
        return sb.toString();
    }
}


