package osm.catatom2osm;

public class BuildingPart extends BaseElement {
	
	public int mNumFloorsAboveGround=-1;
	public int mNumFloorsBelowGround=-1;
	
	protected BuildingPart(String id) {
		super(id);
	}
	
	@Override
	protected boolean check() {
		
		if (!extractTextRings()) {
			
			Log.warning("BuildingPart <"+getId()+"> extractTextRings() failed!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mExteriorRingsCount==0) {
			
			Log.warning("BuildingPart <"+getId()+"> has no external rings!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mExteriorRingsCount>1) {
			
			Log.warning("BuildingPart <"+getId()+
					"> has more than 1 external ring!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mNumFloorsAboveGround<0) {
			
			Log.warning("BuildingPart <"+getId()+
					"> mNumFloorsAboveGround<0!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mNumFloorsBelowGround<0) {
			
			Log.warning("BuildingPart <"+getId()+
					"> mNumFloorsBelowGround<0!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}

}
