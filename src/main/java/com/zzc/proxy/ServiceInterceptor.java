package com.zzc.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.mina.core.session.IoSession;

import com.zzc.result.Result;
import com.zzc.result.ResultObserver;

/**
 * cglib 生成动态代理的时候，统一的拦截方法
 * 此方法就是我们拦截方法调用，执行rpc调用的地方
 * 此方法是线程安全的
 * @author ying
 *
 */
public class ServiceInterceptor implements MethodInterceptor,ResultObserver {
	private static AtomicLong incrementLong = new AtomicLong();//自增的id
	
	private final String token = String.valueOf(incrementLong.incrementAndGet());//token值
	private volatile Object result;//调用返回值
	private CountDownLatch gate = new CountDownLatch(1);//声明一个大门 闭锁 用来协调等待异步返回

	private IoSession ioSession;
	private Class<?> itf;
	
	/**
	 * 
	 * @param ioSession
	 * @param itf 代理的目标接口类
	 */
	public ServiceInterceptor(IoSession ioSession,Class<?> itf){
		this.ioSession = ioSession;
		this.itf = itf;
	}
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2,
			MethodProxy arg3) throws Throwable {
		
		//参数类型列表
		List<Class<?>> argumentsType = new ArrayList<Class<?>>();
		for(Object tmp : arg2){
			argumentsType.add(tmp.getClass());
		}
		
		Class[] a = {};
		//构建请求对象
		Invocation invocation = new RpcInvocatioin(arg1.getName(), argumentsType.toArray(a), arg2, this.itf);
		//发送请求
		ioSession.write(invocation);
		
		//等待返回
		gate.await();
//		gate.await(timeout, unit)
		
		return result;
	}
	
	@Override
	public void setResult(Result result) {
		this.result = result;
		gate.countDown();
	}

	@Override
	public String getToken() {
		return this.token;
	}

}
