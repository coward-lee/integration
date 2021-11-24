package org.lee.nettt_in_cation.chapter13_udp.broader;

import chapter13_udp.LogEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroader {
	private final EventLoopGroup group;
	private final Bootstrap bootstrap;
	private final File file;

	public LogEventBroader(InetSocketAddress address, File file) {
		bootstrap = new Bootstrap();
		group = new NioEventLoopGroup();
		bootstrap.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST,true)
				.handler(new LogEventEncoder(address));
		this.file = file;
	}

	public void run() throws Exception {
		Channel ch = bootstrap.bind(0).sync().channel();
		long pointer = 0L;
		for (;;){
			long len = file.length();
			if (len < pointer){
				pointer = len;
			}else if (len>pointer){
				RandomAccessFile raf = new RandomAccessFile(file,"r");
				raf.seek(pointer);
				String line;
				while((line=raf.readLine())!=null){
					LogEvent logEvent = new LogEvent(null,-1,file.getAbsolutePath(),line);
					System.out.println("发送了："+logEvent);
					ch.writeAndFlush(logEvent);
				}
				pointer = raf.getFilePointer();
				raf.close();
				Thread.sleep(1000);
			}
		}
	}

	public void stop(){
		group.shutdownGracefully();
	}

	public static void main(String[] args) {
//		if (args.length!=2)
		File file = new File("file.txt");
		String path =file.getAbsolutePath();
		LogEventBroader broader = new LogEventBroader(
				new InetSocketAddress("255.255.255.255",8080),
				file);
		try {
			broader.run();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			broader.stop();
		}
	}
}
