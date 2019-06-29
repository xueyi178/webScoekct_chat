package com.socket.config;

import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
/**
 * 1.实现ServerApplicationConfig的接口
 * 项目名称：webScoekct_01 
 * 类名称：DemoConfig
 * 开发者：Lenovo
 * 开发时间：2019年6月28日下午12:40:03
 */
public class DemoConfig implements ServerApplicationConfig{

	/**
	 * 1.注解当时启动
	 * 2. scaned扫描服务服务端带webScoekct的类
	 */
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scaned) {
		System.out.println("webscoekct启动了"+ scaned.size());
		//返回值  提供了一个过滤的作用
		return scaned;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
		return null;
	}
}
