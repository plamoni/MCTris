package com.codefreak.mc.mctris;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class MCTrisLogManager {
	public static Logger logger = Logger.getLogger("MCTris");
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Formatter formatter = new Formatter() {
		@Override
		public String format(LogRecord record) {
			StringBuilder entry = new StringBuilder();
			
			entry.append(MCTrisLogManager.dateFormatter.format(new Date(record.getMillis())));
			entry.append(" [MCTris] [");
			entry.append(record.getLevel().getLocalizedName());
			entry.append("] ");
			entry.append(record.getMessage());
			entry.append("\n");
			
			return entry.toString();
		}
	};

	public static void init() {
		logger.setUseParentHandlers(false);
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);
		
		//FOR DEBUGGING:
		logger.setLevel(Level.FINEST);
		handler.setLevel(Level.FINEST);
		
		logger.addHandler(handler);
	}
}