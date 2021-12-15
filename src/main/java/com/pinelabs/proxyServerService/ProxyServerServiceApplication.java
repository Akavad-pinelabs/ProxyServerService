package com.pinelabs.proxyServerService;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.pinelabs.proxyServerService.logger.LoggerClass;

@SpringBootApplication
public class ProxyServerServiceApplication {

	@Autowired
	ProxyServerService proxyServerService;

	public static void main(String[] args) {
		SpringApplication.run(ProxyServerServiceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startProxyServer() {
		try {
			proxyServerService.startProxyServer();
		} 
		catch (TException e) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "startProxyServer Exception: " + e.getMessage());
		}
		catch(Exception ex) {
			LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "startProxyServer Exception: " + ex.getMessage());
		}
	}
}
