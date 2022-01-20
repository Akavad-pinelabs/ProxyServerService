package com.pinelabs.socket;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SocketFactory implements PooledObjectFactory<SocketClient> {

	private final String host;
    private final int port;
	
	public SocketFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
	public SocketClient create() throws Exception {
        SocketClient socketClient = new SocketClient(host, port);
        return socketClient;
    }
	
    public PooledObject<SocketClient> wrap(SocketClient t) {
        return new DefaultPooledObject<>(t);
    }
    
    @Override
	public PooledObject<SocketClient> makeObject() throws Exception {
		// TODO Auto-generated method stub
		return this.wrap(this.create());
	}
    
	@Override
	public void activateObject(PooledObject<SocketClient> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyObject(PooledObject<SocketClient> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(PooledObject<SocketClient> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validateObject(PooledObject<SocketClient> p) {
		// TODO Auto-generated method stub
		return false;
	}

}
