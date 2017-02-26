package osm.catatom2osm;

import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.Set;

public abstract class OsmItem {
	
	protected int mId=0;
	
	protected OsmTags mTags=new OsmTags(); 
	
	public void setId(int id) {
		
		mId=id;
	}
	
	void setTag(String key, String value) {
		
		mTags.put(key, value);
	}
	
	String getTagsString() {
		
		String line="";
		
		Set<String> keys=mTags.keySet();
		
		Iterator<String> keyIter=keys.iterator();
		
		while(keyIter.hasNext()) {
			
			String keyString=keyIter.next();
			
			String value=mTags.get(keyString);
			
			line+="  <tag k='"+keyString+"' v='"+value+"'/>\n";
		}
		
		return line;
	}
	
	
	
	public abstract boolean writeToFile(BufferedWriter writer);

}
