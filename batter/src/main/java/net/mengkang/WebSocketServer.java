package net.mengkang;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.mengkang.entity.Client;
import net.mengkang.entity.RoomInfo;
import net.mengkang.manager.ClassRoomMgr;
import net.mengkang.manager.ClientCodeEnum;
import net.mengkang.manager.MessMgr;
import net.mengkang.service.HearBeat;


public final class WebSocketServer {

    private static final int PORT = 8095;



    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);
        HearBeat hearBeat = new HearBeat();
        hearBeat.startBeating();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer());

            Channel ch = b.bind(PORT).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
