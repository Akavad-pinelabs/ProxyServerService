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
	
	public ProxyServer(String HSMControllerIp, int HSMControllerPort) {
		this.HSMControllerIp = HSMControllerIp;
		this.HSMControllerPort = HSMControllerPort;
	}
	
	@Override
	public ByteBuffer sendReceivePacket(ByteBuffer messageWritten) throws TException {
		ByteBuffer receivedBuff = forwardRequest(messageWritten.array());
		return receivedBuff;
	}

	public ByteBuffer forwardRequest(byte[] messageWritten) {
		byte[] data = new byte[1024];;
		try
        {        
        Socket socket = new Socket(HSMControllerIp, HSMControllerPort);
        OutputStream out = new DataOutputStream(socket.getOutputStream());
        InputStream in = new DataInputStream(socket.getInputStream());
	  
   	    out.write(messageWritten);
					   
		//takes input from socket		    
	    in.read(data);
        socket.close();
        }
        catch(UnknownHostException u)
        {
        	LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, u.getMessage());
        }
        catch(IOException i)
        {
        	LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, i.getMessage());;
        }
		return ByteBuffer.wrap(data);
	}
	
}
