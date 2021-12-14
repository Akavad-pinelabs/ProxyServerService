package com.pinelabs.proxyServerService.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerClass {

	private static final Logger logger = LogManager.getLogger(LoggerClass.class);
	
	public enum eMessageType {

		MT_ERROR(0), MT_INFORMATION(1);

		private final int value;

		eMessageType(final int newValue) {
			value = newValue;
		}

		public int getValue() {
			return value;
		}
	}
		
	public static void LogMessage(eMessageType objMessageType, String message) {
		
		if (objMessageType == eMessageType.MT_INFORMATION)
			logger.info(message);
		else
			logger.error(message);

	}
}
