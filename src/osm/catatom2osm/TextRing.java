package osm.catatom2osm;

import java.util.ArrayList;

public class TextRing extends ArrayList<TextCoord> {
	
	static final public int RING_TYPE_NULL=0;
	static final public int RING_TYPE_EXTERIOR=1;
	static final public int RING_TYPE_INTERIOR=2;

	private static final long serialVersionUID = 1L;
	
	String mSourceData=null;
	
	int mRingType=RING_TYPE_NULL;
	
	public String mEPSG=null;
	
	public TextRing() {
		
	}
	
	public TextRing(String sourceData) {
		
		mRingType=RING_TYPE_NULL;
		mSourceData=sourceData;
	}
	
	public TextRing(int ringType, String sourceData) {
		
		mRingType=ringType;
		mSourceData=sourceData;
	}
	
	
	public void setEPSG(String epsg) {
		
		mEPSG=epsg;
	}
	
	public boolean calculateCoordinates(String epsg) {
		
		if (mSourceData==null) {
			
			Log.warning("calculateCoordinates() mSourceData==null");
			return false;
		}
		
		while(!mSourceData.trim().isEmpty()) {
			
			mSourceData=mSourceData.trim();
			
			int firstSpacePos=mSourceData.indexOf(" ");
			
			if (firstSpacePos<0) {
				
				Log.warning("calculateCoordinates() firstSpacePos<0");
				return false;
			}
			
			String lon=mSourceData.substring(0, firstSpacePos).trim();
			
			if (lon.isEmpty()) {
				Log.warning("calculateCoordinates() lon is empty");
				return false;
			}
			
			mSourceData=mSourceData.substring(firstSpacePos).trim();
			
			int secondSpacePos=mSourceData.indexOf(" ");
			
			String lat;
			
			if (secondSpacePos<0) {
				
				lat=mSourceData;
				
				mSourceData="";
			}
			else {
				
				lat=mSourceData.substring(0, secondSpacePos).trim();
				
				mSourceData=mSourceData.substring(secondSpacePos).trim();
			}
			
			
			if (lat.isEmpty()) {
				Log.warning("calculateCoordinates() lat is empty");
				return false;
			}
			
			TextCoord coord=new TextCoord(lon+" "+lat);
			
			if (!coord.calculateCoordinates(epsg)) {
				
				return false;
			}
			else {
				
				add(coord);
			}
		}
		
		return true;
	}
	
	public boolean calculateEnvelope() {
		
		if (mEPSG==null) {
			
			Log.warning("calculateEnvelope() mEPSG==null");
			return false;
		}
		
		if (mSourceData==null) {
			
			Log.warning("calculateEnvelope() mSourceData==null");
			return false;
		}
		
		mSourceData=mSourceData.trim();
		
		int firstSpacePos=mSourceData.indexOf(" ");
		
		if (firstSpacePos<0) {
			
			Log.warning("calculateEnvelope() firstSpacePos<0");
			return false;
		}
		
		String lon1=mSourceData.substring(0, firstSpacePos).trim();
		
		if (lon1.isEmpty()) {
			Log.warning("calculateEnvelope() lon1 is empty");
			return false;
		}
		
		mSourceData=mSourceData.substring(firstSpacePos).trim();
		
		int secondSpacePos=mSourceData.indexOf(" ");
		
		String lat1=mSourceData.substring(0, secondSpacePos).trim();
		
		if (lat1.isEmpty()) {
			Log.warning("calculateEnvelope() lat is empty");
			return false;
		}
		
		mSourceData=mSourceData.substring(secondSpacePos).trim();
		
		int thirdSpacePos=mSourceData.indexOf(" ");
		
		if (thirdSpacePos<0) {
			
			Log.warning("calculateEnvelope() thirdSpacePos<0");
			return false;
		}
		
		String lon2=mSourceData.substring(0, thirdSpacePos).trim();
		
		if (lon2.isEmpty()) {
			Log.warning("calculateEnvelope() lon2 is empty");
			return false;
		}
		
		mSourceData=mSourceData.substring(thirdSpacePos).trim();
		
		int fourthSpacePos=mSourceData.indexOf(" ");
		
		String lat2;
		
		if (fourthSpacePos<0) {
			
			lat2=mSourceData;
			
			mSourceData="";
		}
		else {
			
			lat2=mSourceData.substring(0, secondSpacePos).trim();
			
			mSourceData=mSourceData.substring(secondSpacePos).trim();
		}
		
		
		if (lat2.isEmpty()) {
			Log.warning("calculateCoordinates() lat2 is empty");
			return false;
		}
		
		TextCoord coords[]=new TextCoord[5];
		
		coords[0]=new TextCoord(lon1+" "+lat1);
		coords[1]=new TextCoord(lon2+" "+lat1);
		coords[2]=new TextCoord(lon2+" "+lat2);
		coords[3]=new TextCoord(lon1+" "+lat2);
		coords[4]=new TextCoord(lon1+" "+lat1);
		
		for (int i=0; i<5; i++) {
			
			if (!coords[i].calculateCoordinates(mEPSG)) {
				
				return false;
			}
			else {
				
				add(coords[i]);
			}
		}
		
		return true;
	}
}
