package com.pinelabs.proxyServerService.services;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pinelabs.proxyServerService.logger.LoggerClass;
import com.pinelabs.socket.SocketClient;
import com.pinelabs.socket.SocketFactory;
import com.pinelabs.socket.SocketPool;

@Service
public class SocketPoolService {

	@Value("${hsm.ip}")
	private String HSMControllerIp;

	@Value("${hsm.port}")
	private int HSMControllerPort;
	
	private SocketPool socketPool;
	
	public void createSocketPool() {
		SocketFactory factory = new SocketFactory(HSMControllerIp, HSMControllerPort);
        GenericObjectPoolConfig<SocketClient> config = new GenericObjectPoolConfig<SocketClient>();
        config.setMaxTotal(10);
        
        socketPool = new SocketPool(factory, config);
        try {
			socketPool.preparePool();
		} catch (Exception e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_ERROR, e.getMessage());
		}
	}
	
	public SocketPool getSocketPool() {
		return socketPool;
	}
}
