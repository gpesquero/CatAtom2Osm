package osm.catatom2osm;

import java.io.InputStream;

import org.xml.sax.Attributes;

public class StreetParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="AD:ThoroughfareName";
	
	public Street mStreet=null;
	
	private StreetList mStreetList=new StreetList();
	
	protected StreetParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	StreetList getStreets() {
		
		return mStreetList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mStreet!=null) {
			
			Log.warning("Street starts and there's one already created!!");
		}
		
		mStreet=new Street(id);
		
		mElement=mStreet;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mStreet==null) {
			
			Log.warning("Street ends and there's no one opened!!");
		}
		else {
			
			if (mStreet.check()) {
				
				mStreetList.add(mStreet);
			}
			else {
				
				Log.warning("Street.check() returned false!!");
			}
			
			mStreet=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
		
		if (tagName.equalsIgnoreCase("GN:text")) {
			
			mStreet.mName=data.trim();
		}
	}
	
	boolean checkBuildingList() {
		
		return false;
	}

}
