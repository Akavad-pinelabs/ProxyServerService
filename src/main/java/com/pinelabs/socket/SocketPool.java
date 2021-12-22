package com.pinelabs.socket;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class SocketPool extends GenericObjectPool<SocketClient>{

	public SocketPool(PooledObjectFactory<SocketClient> factory, GenericObjectPoolConfig<SocketClient> config) {
		super(factory, config);
		// TODO Auto-generated constructor stub
	}

}
