package org.lee;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.IMServerExchangeHandler;
import org.lee.decoder.MessageInHandler;
import org.lee.decoder.RegisterHandler;
import org.lee.domain.MessageProto.Message;
import org.lee.encoder.IMProtoEncoder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {


//    private final Logger log = LoggerFactory.getLogger(Server.class);
    Logger log = LogManager.getLogger("xx");

    private final Integer port;

    public Server(Integer port) {
        this.port = port;
    }

    public void runServer() {
        ServerBootstrap server = new ServerBootstrap();
        try {
            EventLoopGroup connectPool = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();
            server.group(connectPool, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IMProtoDecoder());
                    ch.pipeline().addLast(new RegisterHandler());
                    ch.pipeline().addLast(new MessageInHandler());
                    ch.pipeline().addLast(new IMServerExchangeHandler());

                    ch.pipeline().addLast(new IMProtoEncoder());
                }
            });

            ChannelFuture listen = server.bind().sync();
            log.info("启动成功，服务器端口为：{}", port);
            System.out.println("启动成功");

            // 关闭监听回调
            ChannelFuture close = listen.channel().closeFuture();
            close.sync();
            log.warn("服务器关闭");
        } catch (Exception e) {
            log.error(" 服务端出现错误", e);
        }
    }


    public static void main(String[] args) throws IOException {
//        LoggerContextRule
        CustomConfigurationFactory customConfigurationFactory = new CustomConfigurationFactory();

        Configuration configuration = customConfigurationFactory.getConfiguration(
                null,
                new ConfigurationSource(new FileInputStream(new File("D:\\code\\java\\integration\\im\\src\\main\\resources\\log4j2.xml")))
        );
        Configurator.initialize(customConfigurationFactory.getConfiguration());
        new Server(80).runServer();
        System.out.println("启动成功了嘛...........");

    }

    @Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
    @Order(50)
    public static class CustomConfigurationFactory extends ConfigurationFactory {
        static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
            builder.setConfigurationName(name);
            builder.setStatusLevel(Level.INFO);
            builder.add(
                    builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
                            .addAttribute("level", Level.INFO)
            );
            AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE")
                    .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                    .add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"))
                    .add(builder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL).addAttribute("marker", "FLOW"));
            builder.add(appenderBuilder)
                    .add(builder.newLogger("org.apache.logging.log4j", Level.INFO)
                            .add(builder.newAppenderRef("Stdout"))
                            .addAttribute("additivity", false))
                    .add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("Stdout")));
            builder.add(appenderBuilder)
                    .add(builder.newLogger("org.lee",Level.INFO));
            return builder.build();
        }

        @Override
        public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
            return getConfiguration(loggerContext, source.toString(), null);
        }

        @Override
        public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI con) {
            ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
            return createConfiguration(name, builder);
        }

        public Configuration getConfiguration() {
            return getConfiguration(null, null, null);
        }


        @Override
        protected String[] getSupportedTypes() {
            return new String[]{"*"};
        }
    }
}
