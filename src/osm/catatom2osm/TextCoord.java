package osm.catatom2osm;

import java.util.Locale;

import com.vividsolutions.jts.geom.Coordinate;

public class TextCoord {
	
	public String mSourceData=null;
	
	public String mLon=null;
	public String mLat=null;
	
	public String mEPSG=null;
	
	public TextCoord() {
		
	}
	
	public TextCoord(String sourceData) {
		
		mSourceData=sourceData;
	
	}
	
	public boolean calculateCoordinates(String epsg) {
		
		if (mSourceData==null) {
			
			Log.warning("TextCoord::calculateCoordinates() mSourceData==null");
			return false;
		}
		
		mSourceData=mSourceData.trim();
		
		int spacePos=mSourceData.indexOf(" ");
		
		if (spacePos<0) {
			
			Log.warning("TextCoord::calculateCoordinates() spacePos<0");
			return false;
		}
		
		double lon, lat;
		
		try {
			
			lon=Double.parseDouble(mSourceData.substring(0, spacePos).trim());
			lat=Double.parseDouble(mSourceData.substring(spacePos).trim());
		}
		catch(NumberFormatException e) {
			
			Log.warning("Address::calculateCoordinate() parseDouble: "+
					e.getMessage());
			
			return false;
		}
		
		Coordinate coord=Main.mTransformer.transform(
				new Coordinate(lon, lat), epsg);
		
		if (coord==null) {
			
			Log.warning("Error in transform coordinate");
			return false;
		}
		
		mLon=String.format(Locale.US, "%.07f", coord.y);
		mLat=String.format(Locale.US, "%.07f", coord.x);
		
		return true;
	}

}
