package osm.catatom2osm;

import java.sql.Time;

public class Log {
	
	private static final int TYPE_ERROR=0;
	private static final int TYPE_INFO=1;
	private static final int TYPE_WARNING=2;
	private static final int TYPE_DEBUG=3;
	
	static private int mErrorCount=0;
	static private int mWarningCount=0;
	
	public static int getErrorCount() {
		
		return mErrorCount;
	}
	
	public static int getWarningCount() {
		
		return mWarningCount;
	}
	
	synchronized static void log(int errorType, String message) {
		
		Time time=new Time(System.currentTimeMillis());
		
		String text=time.toString();
		
		switch(errorType) {
		
		case TYPE_ERROR:
			text+=" [**ERROR**] ";
			break;
			
		case TYPE_WARNING:
			text+=" [**WARN**] ";
			break;
			
		case TYPE_INFO:
			text+=" [INFO] ";
			break;
			
		case TYPE_DEBUG:
			text+=" [**DEBUG**] ";
			break;
			
		default:
			text+=" [**UNKNW**] ";
			break;
		}
		
		System.out.println(text+message);
	}
	
	public static void error(String message) {
		log(TYPE_ERROR, message);
		
		mErrorCount++;
	}
	
	public static void info(String message) {
		log(TYPE_INFO, message);
	}
	
	public static void warning(String message) {
		log(TYPE_WARNING, message);
		
		mWarningCount++;
	}
	
	public static void debug(String message) {
		log(TYPE_DEBUG, message);
	}
}
