package com.yale.test.springmvc.websocket.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

@Component
public class ChatHistory {
	final int maxMessages = 100;
	final List<ChatMessage> chatHistory = new ArrayList<>(100);
	final Lock readLock;
	final Lock writeLock;
	
	public ChatHistory() {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		this.readLock = lock.readLock();
		this.writeLock = lock.writeLock();
	}
	
	public List<ChatMessage> getHistory() {
		this.readLock.lock();
		try {
			//return List.copyOf(chatHistory);JDK8没有这个方法
			List<ChatMessage> chatList = new ArrayList<>();
			chatList.addAll(chatHistory);
			return chatList;
		} finally {
			this.readLock.unlock();
		}
	}
	
	public void addToHistory(ChatMessage message) {
		this.writeLock.lock();
		try {
			this.chatHistory.add(message);
			if (this.chatHistory.size() > maxMessages) {
				this.chatHistory.remove(0);
			}
		} finally {
			this.writeLock.unlock();
		}
	}
}
