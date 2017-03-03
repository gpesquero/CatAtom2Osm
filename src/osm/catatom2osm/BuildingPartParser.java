package osm.catatom2osm;

import java.io.InputStream;

import org.xml.sax.Attributes;

public class BuildingPartParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="bu-ext2d:BuildingPart";
	
	public BuildingPart mBuildingPart=null;
	
	private BuildingPartList mBuildingPartList=new BuildingPartList();
	
	protected BuildingPartParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	BuildingPartList getBuildingParts() {
		
		return mBuildingPartList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mBuildingPart!=null) {
			
			Log.warning("BuildingPart starts and there's one already created!!");
		}
		
		mBuildingPart=new BuildingPart(id);
		
		mElement=mBuildingPart;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mBuildingPart==null) {
			
			Log.warning("BuildingPart ends and there's no one opened!!");
		}
		else {
			
			if (mBuildingPart.check()) {
				
				mBuildingPartList.add(mBuildingPart);
			}
			else {
				
				Log.warning("Building.check() returned false!!");
			}
			
			mBuildingPart=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
		
		if (tagName.equalsIgnoreCase("bu-ext2d:numberOfFloorsAboveGround")) {
			
			try {
				mBuildingPart.mNumFloorsAboveGround=Integer.parseInt(data.trim());
			}
			catch(NumberFormatException e) {
				
				Log.warning("BuildingPartParser.onData() mNumFloorsAboveGround parseInt() error!!");
				
				mBuildingPart.mNumFloorsAboveGround=-1;
			}
		}
		else if (tagName.equalsIgnoreCase("bu-ext2d:numberOfFloorsBelowGround")) {
			
			try {
				mBuildingPart.mNumFloorsBelowGround=Integer.parseInt(data.trim());
			}
			catch(NumberFormatException e) {
				
				Log.warning("BuildingPartParser.onData() mNumFloorsBelowGround parseInt() error!!");
				
				mBuildingPart.mNumFloorsBelowGround=-1;
			}
		}
	}
}
