package com.pinelabs.proxyServerService;

import java.nio.ByteBuffer;

import com.pinelabs.proxyServerService.logger.LoggerClass;
import com.pinelabs.socket.SocketClient;
import com.pinelabs.socket.SocketPool;

public class ProxyServer implements SendReceivePacketService.Iface {
	
	private SocketPool socketPool;
	
	public ProxyServer(SocketPool socketPool) {
		this.socketPool = socketPool;
	}
	
	@Override
	public ByteBuffer sendReceivePacket(ByteBuffer messageWritten) {
		ByteBuffer receivedBuff = null;
		if (messageWritten != null) {
			receivedBuff = forwardRequest(messageWritten.array());
		}
		return receivedBuff;
	}

	private ByteBuffer forwardRequest(byte[] messageWritten) {
		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Inside forwardRequest");
		
		byte[] data = new byte[0];
		
		try {
			SocketClient socketClient = socketPool.borrowObject();			
			data = socketClient.sendRequest(messageWritten);
			socketPool.returnObject(socketClient);
		}
		catch (Exception e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
		}
		
		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Outside forwardRequest");
		return ByteBuffer.wrap(data);
	}
	
}
