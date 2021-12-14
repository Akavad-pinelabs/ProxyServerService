package com.pinelabs.proxyServerService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Value;

import com.pinelabs.proxyServerService.logger.LoggerClass;
import com.pinelabs.proxyServerService.utils.Utils;

public class ProxyServer implements SendReceivePacketService.Iface{
	
	private String HSMControllerIp;
	
	private int HSMControllerPort;
	
	public ProxyServer(String HSMControllerIp, int HSMControllerPort) {
		this.HSMControllerIp = HSMControllerIp;
		this.HSMControllerPort = HSMControllerPort;
	}
	@Override
	public String sendReceivePacket(String messageWritten) throws TException {
		// TODO Auto-generated method stub
		String receivedMsg = forwardRequest(messageWritten);
		return receivedMsg;
	}

	public String forwardRequest(String messageWritten) {
		String receivedMsg="";
		try
        {        
        Socket socket = new Socket(HSMControllerIp, HSMControllerPort);
        OutputStream out = new DataOutputStream(socket.getOutputStream());
        InputStream in = new DataInputStream(socket.getInputStream());
	  
   	    out.write(Utils.str2Bcd(messageWritten));
					   
		//takes input from socket	
	    byte[] data = new byte[1024];
	    in.read(data);
	    receivedMsg= Utils.bcd2Str(data);
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
		return receivedMsg;
	}
}
