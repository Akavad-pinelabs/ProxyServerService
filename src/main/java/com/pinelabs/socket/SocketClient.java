package com.pinelabs.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.pinelabs.proxyServerService.logger.LoggerClass;

public class SocketClient {

	private Socket socket;
    private OutputStream out;
    private InputStream in;
    private final int TIMEOUT_IN_MS = 30 * 1000;
    private final int RES_HEADER_LEN = 8;
    
    public SocketClient(String HSMControllerIp, int HSMControllerPort) {
    	try {
			socket = new Socket(HSMControllerIp, HSMControllerPort);
			socket.setSoTimeout(TIMEOUT_IN_MS);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
		} catch (IOException e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
		}
    }
    
    public byte[] sendRequest(byte[] messageWritten) {
    	byte[] data = new byte[0];
        try{
        	out.write(messageWritten);
        	byte[] uchHeaderBuff = in.readNBytes(RES_HEADER_LEN);
			if (uchHeaderBuff != null && uchHeaderBuff.length == RES_HEADER_LEN) {
				int iDataLen = (uchHeaderBuff[6] << 8) & 0xFF00;
				iDataLen |= uchHeaderBuff[7] & 0x00FF;
				iDataLen += 1;
				
				byte[] uchBodyNTrailBuff = in.readNBytes(iDataLen);
				
				data = new byte[RES_HEADER_LEN + iDataLen];
				System.arraycopy(uchHeaderBuff, 0, data, 0, RES_HEADER_LEN);
				System.arraycopy(uchBodyNTrailBuff, 0, data, RES_HEADER_LEN, iDataLen);
			}
			else {
				LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Invalid Data Received");
			}
			
        }catch(IOException e){
        	LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
        }
        
        return data;
    }
    
    public boolean isConnected() {
    	return socket.isConnected();
    }
    
    public boolean isClosed() {
    	return socket.isClosed();
    }
}
