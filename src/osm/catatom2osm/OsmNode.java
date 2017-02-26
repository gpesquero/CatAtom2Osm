package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class OsmNode extends OsmItem {
	
	private String mLon=null;
	private String mLat=null;
	
	public OsmNode(String lon, String lat) {
		
		mLon=lon;
		mLat=lat;
	}
	
	public boolean writeToFile(BufferedWriter writer) {
		
		if (mId==0) {
			
			Log.warning("OsmNode.writeToFile() mId==0");
			return false;
		}
		
		if (mLon==null) {
			
			Log.warning("OsmNode.writeToFile() mLon==null");
			return false;
		}
		
		if (mLat==null) {
			
			Log.warning("OsmNode.writeToFile() mLat==null");
			return false;
		}
		
		/*
		if (mTimeStamp==null) {
			
			Log.error("Error in node.writeToFile mTimeStamp==null");
			return false;
		}
		
		String line=String.format(Locale.US,
			" <node id=\"%d\" timestamp=\"%s\" version=\"6\" lat=\"%.07f\" lon=\"%.07f\">\n",
			mId, mTimeStamp, mLat, mLon);
		*/
		
		String line=String.format(Locale.US,
			" <node id='%d' lat='%s' lon='%s'>\n",
			mId, mLat, mLon);
		
		line+=getTagsString();
		
		line+=" </node>\n";
		
		try {
			writer.write(line);
		
		} catch (IOException e) {
			
			Log.error("Error in node.writeToFile write(): "+e.getMessage());
			return false;	
		}
		
		return true;
	}
}
