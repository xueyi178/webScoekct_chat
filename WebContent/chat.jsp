<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>聊天的页面</title>
<script type="text/javascript" src="jquery-1.4.4.min.js"></script>
<script type="text/javascript">
	//获取登录的姓名
	var username = "${sessionScope.username}"

	var ws;  //一个ws对象就是一个通信管道
	//target: 表示服务端的webScoekct的地址,你请求那个就连接那个
	var target = "ws://localhost:8080/chat/chatSocket?username="+username;
	window.onload = function(){
		//进入聊天的界面,就打开链接通道
		  if ('WebSocket' in window) {
      	  ws = new WebSocket(target);
  	  } else if ('MozWebSocket' in window) {
     	   ws = new MozWebSocket(target);
  	  } else {
       	 alert('WebSocket is not supported by this browser.');
       	 return;
    	}
		
		  ws.onmessage=function(event){
			  eval("var msg ="+event.data);
				
				if(msg.welcome != undefined){
					$("#content").append(msg.welcome+"<br/>");
				}
				
				if(msg.usernames != undefined){
					$("#userList").html("");
					$(msg.usernames).each(function(){
						$("#userList").append("<input type=checkbox value='"+this+"'/>"+this+"<br/>");
					});
				}
				
				if(msg.content != undefined){
					$("#content").append(msg.content+"<br/>");
				}
		}
	}
	
	function send(){
		/*
		群聊广播的信息
		var val = $("#msg").val();
		ws.send(val);
		$("#msg").val("");*/
		
		//私聊
		var obj = null;
		var val = $("#msg").val();
		var ss = $("#userList :checkbox");
		if(ss.size() == 0){
			 obj = {
				msg: val,
				type: 1  //1: 表示为广播   2: 表示为私聊
			}
		}else{
			var to = $("#userList :checkbox").val();
			 obj = {
					to: to,
					msg: val,
					type: 2  //1: 表示为广播   2: 表示为私聊
			}
		}
		
		var str = JSON.stringify(obj);
		ws.send(str);
		$("#msg").val("");
	}
</script>
</head>
<body>
	<h3>欢迎 ${sessionScope.username } 使用本系统！！</h3>

	<div id="content"
		style="border: 1px solid black; width: 400px; height: 300px; float: left;"></div>
	<div id="userList"
		style="border: 1px solid black; width: 100px; height: 300px; float: left;"></div>

	<div style="clear: both;">
		<input id="msg" />
		<button onclick="send()">send</button>
	</div>
</body>
</html>