package com.zzc;

import com.zzc.main.config.CallTypeEnum;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//		try {
//			IoAcceptor acceptor = new NioSocketAcceptor();
//			acceptor.getSessionConfig().setReadBufferSize(2048);//设置缓冲区大小
//			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);//设置多长时间进入空闲
//			acceptor.bind(new InetSocketAddress(88425));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        System.out.println(CallTypeEnum.valueOf("3"));
    }
}