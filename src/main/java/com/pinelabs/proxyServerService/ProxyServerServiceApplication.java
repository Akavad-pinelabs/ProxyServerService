package com.pinelabs.proxyServerService;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

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
		} catch (TException e) {
			e.printStackTrace();
		}
	    
	}
}
