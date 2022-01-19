package com.pinelabs.proxyServerService.services;

import java.time.Duration;

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
	
	@Value("${hsm.socketPool.maxSocketPool:10}")
	private int maxSocketPool;
	
	@Value("${hsm.socketPool.maxWaitTime:20000}")
	private int maxWaitTime;
	
	@Value("${hsm.socketPool.minEvictableIdleTime:450000}")
	private int minEvictableIdleTime;
	
	@Value("${hsm.socketPool.timeBetweenEvictionRuns:5000}")
	private int timeBetweenEvictionRuns;
	
	private SocketPool socketPool;
	
	public void createSocketPool() {
		SocketFactory factory = new SocketFactory(HSMControllerIp, HSMControllerPort);
        GenericObjectPoolConfig<SocketClient> config = new GenericObjectPoolConfig<SocketClient>();

        config.setMaxTotal(maxSocketPool);
        
        Duration duration = Duration.ofMillis(maxWaitTime);
        config.setMaxWait(duration); // max wait time to get the socket from the socket pool.
        
        duration = Duration.ofMillis(minEvictableIdleTime);
        config.setMinEvictableIdleTime(duration); // evict if socket is idle for defined minimum Evictable Idle Time.
        
        duration = Duration.ofMillis(timeBetweenEvictionRuns); 
        config.setTimeBetweenEvictionRuns(duration); // eviction thread runs in the interval of defined time Between Eviction Runs.
        
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
