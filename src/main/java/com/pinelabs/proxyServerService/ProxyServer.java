package com.pinelabs.proxyServerService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.thrift.TException;


import com.pinelabs.proxyServerService.logger.LoggerClass;

public class ProxyServer implements SendReceivePacketService.Iface{
	
	private String HSMControllerIp;
	private int HSMControllerPort;
	private final int RES_HEADER_LEN = 8;
	
	public ProxyServer(String HSMControllerIp, int HSMControllerPort) {
		this.HSMControllerIp = HSMControllerIp;
		this.HSMControllerPort = HSMControllerPort;
	}
	
	@Override
	public ByteBuffer sendReceivePacket(ByteBuffer messageWritten) throws TException {
		ByteBuffer receivedBuff = forwardRequest(messageWritten.array());
		return receivedBuff;
	}

	private ByteBuffer forwardRequest(byte[] messageWritten) {
		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Inside forwardRequest");
		
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] data = null;
		
		try {
			socket = new Socket(HSMControllerIp, HSMControllerPort);
			socket.setSoTimeout(30 * 1000);
			
			out = new DataOutputStream(socket.getOutputStream());
			out.write(messageWritten);

			// takes input from socket
			in = new DataInputStream(socket.getInputStream());
			
			byte[] uchHeaderBuff = in.readNBytes(RES_HEADER_LEN);
			
			int iDataLen = (uchHeaderBuff[6] << 8) & 0xFF00;
			iDataLen |= uchHeaderBuff[7] & 0x00FF;
			
			byte[] uchBodyNTrailBuff = in.readNBytes(iDataLen);
			
			data = new byte[RES_HEADER_LEN + iDataLen];
			System.arraycopy(uchHeaderBuff, 0, data, 0, RES_HEADER_LEN);
			System.arraycopy(uchBodyNTrailBuff, 0, data, RES_HEADER_LEN, iDataLen);
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Outside forwardRequest");
		} 
		catch (UnknownHostException u) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, u.getMessage());
		}
		catch (IOException i) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, i.getMessage());
		}
		finally {
			try {
				if (socket != null) {
					socket.close();
				}
				if(out != null) {
					out.close();
				}
				if(in != null) {
					in.close();
				}
			}
			catch (IOException e) {
				LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
			}
		}
		return ByteBuffer.wrap(data);
	}
	
}
