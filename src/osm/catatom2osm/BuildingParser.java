package osm.catatom2osm;

import java.io.InputStream;
import org.xml.sax.Attributes;

public class BuildingParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="bu-ext2d:Building";
	
	public Building mBuilding=null;
	
	private BuildingList mBuildingList=new BuildingList();
	
	protected BuildingParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	BuildingList getBuildings() {
		
		return mBuildingList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mBuilding!=null) {
			
			Log.warning("Building starts and there's one already created!!");
		}
		
		mBuilding=new Building(id);
		
		mElement=mBuilding;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mBuilding==null) {
			
			Log.warning("Building ends and there's no one opened!!");
		}
		else {
			
			if (mBuilding.check()) {
				
				mBuildingList.add(mBuilding);
			}
			else {
				
				Log.warning("Building.check() returned false!!");
			}
			
			mBuilding=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
	}
	
	boolean checkBuildingList() {
		
		return false;
	}
}
