package com.yale.test.net.server.rmi.server;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.yale.test.net.server.rmi.shared.WorldClock;

public class WorldClockService implements WorldClock{
	@Override
	public LocalDateTime getLocalDateTime(String zoneId) throws RemoteException {
		return LocalDateTime.now(ZoneId.of(zoneId)).withNano(0);
	}
}
