package com.socket.vo;

import java.util.Date;
import java.util.List;
/**
 * 1.message的数据结构
 * 项目名称：chat 
 * 类名称：Message
 * 开发者：Lenovo
 * 开发时间：2019年6月29日下午5:43:31
 */

import com.google.gson.Gson;

public class Message {

	//欢迎
	private String welcome;
	
	private List<String> usernames;
	
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void setContent(String name, String msg) {
		this.content = name+" " + new Date() + ":<br/>" + msg + "<br/>";
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
	
	private static Gson gson = new Gson();
	
}
