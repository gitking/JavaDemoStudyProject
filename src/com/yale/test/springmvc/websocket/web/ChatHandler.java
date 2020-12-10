package com.yale.test.springmvc.websocket.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yale.test.springmvc.websocket.entity.User;

/*
 * 处理WebSocket连接
 * 和处理普通HTTP请求不同，没法用一个方法处理一个URL。Spring提供了TextWebSocketHandler和BinaryWebSocketHandler分别处理文本消息和二进制消息，
 * 这里我们选择文本消息作为聊天室的协议，因此，ChatHandler需要继承自TextWebSocketHandler：
 * 当浏览器请求一个WebSocket连接后，如果成功建立连接，Spring会自动调用afterConnectionEstablished()方法，任何原因导致WebSocket连接中断时，
 * Spring会自动调用afterConnectionClosed方法，因此，覆写这两个方法即可处理连接成功和结束后的业务逻辑：
 */
@Component
public class ChatHandler extends TextWebSocketHandler{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private AtomicInteger guestNumber = new AtomicInteger();

	@Autowired
	ChatHistory chatHistory;
	
	@Autowired
	ObjectMapper objectMapper;
	
    // 保存所有Client的WebSocket会话实例:
	private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();
	
	public void broadcastMessage(ChatMessage chat) throws IOException {
		List<ChatMessage> chatList = new ArrayList<>();
		chatList.add(chat);
		TextMessage message = toTextMessage(chatList);
		for (String id : clients.keySet()) {
			WebSocketSession session = clients.get(id);
			session.sendMessage(message);
		}
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String s = message.getPayload().trim();
		//JDK1.9才有strip(),另一个strip()方法也可以移除字符串首尾空白字符。它和trim()不同的是，类似中文的空格字符\u3000也会被移除：
//		String s = message.getPayload().strip();
		if (s.isEmpty()) {
			return;
		}
		
		String name = (String)session.getAttributes().get("name");
		ChatText chat = objectMapper.readValue(s, ChatText.class);
		ChatMessage msg = new ChatMessage(name, chat.text);
		chatHistory.addToHistory(msg);
		broadcastMessage(msg);
	}
	
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 新会话根据ID放入Map:
		clients.put(session.getId(), session);
		String name = null;
		User user = (User)session.getAttributes().get("__user__");
		if (user != null) {
			name = user.getName();
		} else {
			name = initGuestName();
		}
		session.getAttributes().put("name", name);
		logger.info("websocket connection established: id = {}, name = {}", session.getId(), name);
		//把历史消息发给新用户:
		List<ChatMessage> list = chatHistory.getHistory();
		session.sendMessage(toTextMessage(list));
		//添加系统消息并广播:
		ChatMessage msg = new ChatMessage("SYSTEM MESSAGE", name  + " joined the room.");
		chatHistory.addToHistory(msg);
		broadcastMessage(msg);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		clients.remove(session.getId());
		logger.info("websocket connection closed: id = {}, close-status = {}", session.getId(), status);
	}
	
	private TextMessage toTextMessage(List<ChatMessage> messages) throws IOException {
		String json = objectMapper.writeValueAsString(messages);
		return new TextMessage(json);
	}
	
	private String initGuestName() {
		return "Guest" + this.guestNumber.incrementAndGet();
	}
}
