package com.pinelabs.proxyServerService.services;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pinelabs.proxyServerService.ProxyServer;
import com.pinelabs.proxyServerService.SendReceivePacketService;
import com.pinelabs.proxyServerService.SendReceivePacketService.Processor;
import com.pinelabs.proxyServerService.logger.LoggerClass;
import com.pinelabs.socket.SocketPool;

@Service
public class ProxyServerService {

	@Value("${ssl.keystore.path}")
	private String keystorePath; 

	@Value("${ssl.cert.password}")
	private String certPswd;

	@Value("${ssl.port}")
	private int sslPort;
	
	private TServerTransport serverTransport;

	public void startProxyServer(SocketPool socketPool) throws TException {

		ProxyServer proxyServer = new ProxyServer(socketPool);
		SendReceivePacketService.Processor<ProxyServer> processor = new SendReceivePacketService.Processor<ProxyServer>(proxyServer);
					  
		TSSLTransportParameters params = new TSSLTransportParameters();
		params.setKeyStore(keystorePath, certPswd, null, null);

		serverTransport = TSSLTransportFactory.getServerSocket(sslPort, 0, null, params);
		
		final TThreadPoolServer.Args args = new TThreadPoolServer
			    .Args(serverTransport)
			    .processorFactory(new TProcessorFactory(processor))
			    .minWorkerThreads(5)
			    .maxWorkerThreads(10);

		final TServer server = new TThreadPoolServer(args);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				server.serve();
			}
		}).start();
		
		/*TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
		server.serve();*/
		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Starting the server on port: " + sslPort);
	}

	public void stopProxyServer() throws TException {

		if(serverTransport != null) {
			serverTransport.close();
		}
	}
}
