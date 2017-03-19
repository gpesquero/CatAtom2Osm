package osm.catatom2osm;

public class Main {
	
	static public final String mAppName="CatAtom2Osm";
	static public final String mVersion="2017-03-19";
	
	static public EpsgTransformer mTransformer=null; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Log.info("Starting "+mAppName+" (version: "+mVersion+")");
		
		long startTime=System.currentTimeMillis();
		
		if (args.length!=1) {
			
			Log.error("Invalid number of arguments");
			Log.info("Usage: "+mAppName+" <path>");
		}
		else {
			
			mTransformer=new EpsgTransformer();
			
			if (!mTransformer.isOk()) {
				Log.error("EpsgTransformer failed!! Quitting...");
				
			}
			else {
			
				String path=args[0];
			
				new CatAtom2Osm(path);
			}
		}
		
		long stopTime=System.currentTimeMillis();
		
		String elapsedTime=formatElapsedTime(stopTime-startTime);
		
		Log.info(mAppName+" finished in "+elapsedTime+" with "+
				Log.getErrorCount()+" error(s) and "+
				Log.getWarningCount()+" warning(s)");
		
	}
	
	static String formatElapsedTime(long time) {
		
		int hours=(int)Math.abs(time/(60*60*1000));
		
		time-=(hours*60*60*1000);
		
		int minutes=(int)Math.abs(time/(60*1000));
		
		time-=(minutes*60*1000);
		
		int seconds=(int)Math.abs(time/1000);
		
		return String.format("%dh %dm %ds", hours, minutes, seconds);
	}
}
