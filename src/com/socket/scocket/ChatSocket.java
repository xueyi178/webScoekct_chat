package com.socket.scocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.socket.vo.ContnetVo;
import com.socket.vo.Message;

/**
 * 1.与webSocket建立连接
 * 项目名称：chat 
 * 类名称：ChatSocket
 * 开发者：Lenovo
 * 开发时间：2019年6月29日下午5:09:44
 */
@ServerEndpoint("/chatSocket")
public class ChatSocket {


	private String username;

	//创建一个session集合
	private static List<Session> sessions = new ArrayList<Session>();

	//登录人的集合
	private static List<String> names = new ArrayList<String>();

	//单聊的数据结构
	private static Map<String, Session> map = new HashMap<>();


	/**
	 * 1.打开链接
	 * @param session
	 */
	@OnOpen
	public void open(Session session) {

		sessions.add(session);

		// 当前webSocket的Session对象,不是servlet中的session
		//getQueryString: 获取?后面的所有参数
		String queryString = session.getQueryString();
		System.out.println("参数为====="+ queryString);
		//先用split('.')方法将字符串以"."开割形成一个字符串数组，然后再通过索引[1]取出所得数组中的第二个元素的值。
		username = queryString.split("=")[1];
		System.out.println("用户名信息==="+ username);

		//把链接管道的session链接加入集合中
		names.add(username);
		this.map.put(this.username, session);

		String msg = "欢迎"+this.username+"进入聊天室";

		Message message = new Message();
		message.setWelcome(msg);
		message.setUsernames(names);

		this.broadcast(sessions, message.toJson());
	}


	private void  broadcast(List<Session>  ss, String msg){
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			Session session = (Session) iterator.next();
			try {
				session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 2. 释放资源
	 * @param session
	 */
	@OnClose
	public void close(Session session) {
		this.sessions.remove(session);
		this.names.remove(username);

		String msg = "欢送"+this.username+"退出聊天室";

		Message message = new Message();
		message.setWelcome(msg);
		message.setUsernames(names);

		this.broadcast(sessions, message.toJson());
	}

	/**
	 * 3. 接收消息
	 * @param session
	 * @param msg
	 */
	//@OnMessage 群聊
	public void message(Session session, String msg) {


		//群聊的信息
		Message message = new Message();
		message.setContent(this.username, msg);

		this.broadcast(sessions, message.toJson());


	}

	private static Gson gson = new Gson();

	/**
	 * 4. 私聊
	 * @param session
	 * @param msg
	 */
	@OnMessage 
	public void messages(Session session, String json) {
		//群聊的信息
		ContnetVo vo = gson.fromJson(json, ContnetVo.class);
		//vo.getType() == 1表示为广播
		if(vo.getType() == 1) {
			//群聊的信息
			Message message = new Message();
			message.setContent(this.username, "群聊" + vo.getMsg());

			this.broadcast(sessions, message.toJson());
		}else {
			String to = vo.getTo();
			Session sessions =  this.map.get(to);
			
			Message message = new Message();
			message.setContent(this.username,"私聊"+ vo.getMsg());
			try {
				sessions.getBasicRemote().sendText(message.toJson());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
