package com.pinelabs.proxyServerService;

import java.nio.ByteBuffer;
import java.time.Duration;

import org.apache.thrift.TException;
import com.pinelabs.proxyServerService.logger.LoggerClass;
import com.pinelabs.socket.SocketClient;
import com.pinelabs.socket.SocketPool;

public class ProxyServer implements SendReceivePacketService.Iface{
	
	private SocketPool socketPool;
	
	public ProxyServer(SocketPool socketPool) {
		this.socketPool = socketPool;
	}
	
	@Override
	public ByteBuffer sendReceivePacket(ByteBuffer messageWritten) throws TException {
		ByteBuffer receivedBuff = forwardRequest(messageWritten.array());		
		return receivedBuff;
	}

	private ByteBuffer forwardRequest(byte[] messageWritten) {
		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Inside forwardRequest");
		
		byte[] data = new byte[0];
		
		try {		
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "active pool object before borrow "+ socketPool.getNumActive());
			SocketClient socketClient = socketPool.borrowObject();
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "active pool object after borrow "+ socketPool.getNumActive());
			data = socketClient.sendRequest(messageWritten);
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "active pool object before return "+ socketPool.getNumActive());
			socketPool.returnObject(socketClient);
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "active pool object after borrow "+ socketPool.getNumActive());
		} catch (Exception e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
		}
		
		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Outside forwardRequest");
		return ByteBuffer.wrap(data);
	}
	
}
