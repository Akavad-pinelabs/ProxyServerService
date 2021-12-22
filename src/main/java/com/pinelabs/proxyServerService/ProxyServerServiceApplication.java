package com.pinelabs.proxyServerService;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.pinelabs.proxyServerService.logger.LoggerClass;
import com.pinelabs.proxyServerService.services.ProxyServerService;
import com.pinelabs.proxyServerService.services.SocketPoolService;
import com.pinelabs.socket.SocketPool;

@SpringBootApplication
public class ProxyServerServiceApplication {

	@Autowired
	ProxyServerService proxyServerService;
	
	@Autowired
	SocketPoolService socketPoolService;
	
	public static void main(String[] args) {
		SpringApplication.run(ProxyServerServiceApplication.class, args);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Stopping Proxy Server Service!!!");
		}));
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startProxyServer() {
		try {
			socketPoolService.createSocketPool();
			SocketPool socketPool = socketPoolService.getSocketPool();
			proxyServerService.startProxyServer(socketPool);
		} 
		catch (TException e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "startProxyServer Exception: " + e.getMessage());
		}
		catch(Exception ex) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "startProxyServer Exception: " + ex.getMessage());
		}
	}
}
