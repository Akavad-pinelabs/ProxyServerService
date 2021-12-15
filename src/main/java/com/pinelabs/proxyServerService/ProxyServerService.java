package com.pinelabs.proxyServerService;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pinelabs.proxyServerService.logger.LoggerClass;

@Service
public class ProxyServerService {

	@Value("${ssl.keystore.path}")
	private String keystorePath; 

	@Value("${ssl.cert.password}")
	private String certPswd;

	@Value("${ssl.port}")
	private int sslPort;

	@Value("${hsm.ip}")
	private String HSMControllerIp;

	@Value("${hsm.port}")
	private int HSMControllerPort;

	public void startProxyServer() throws TException {

		ProxyServer proxyServer = new ProxyServer(HSMControllerIp, HSMControllerPort);
		SendReceivePacketService.Processor<ProxyServer> processor = new SendReceivePacketService.Processor<ProxyServer>(proxyServer);
		
		TSSLTransportParameters params = new TSSLTransportParameters();
		params.setKeyStore(keystorePath, certPswd, null, null);

		TServerTransport serverTransport = TSSLTransportFactory.getServerSocket(sslPort, 0, null, params);
		
		TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

		LoggerClass.LogMessage(LoggerClass.eMessageType.MT_INFORMATION, "Starting the server on port: " + sslPort);
		server.serve();
	}

}
