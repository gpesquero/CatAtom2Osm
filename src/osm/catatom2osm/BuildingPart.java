package osm.catatom2osm;

public class BuildingPart extends BaseElement {
	
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
		
		/*
		if (!extractExteriorTextRings()) {
			
			mIsOk=false;
			return false;
		}
		
		if (mExteriorRings.size()==0) {
			
			// No exterior ring detected in the zone
			
			Log.warning("No exterior ring detected in the BuildingPart with id="+
					getId());
			
			mIsOk=false;
			return false;
		}
		else if ((mExteriorRings.size()>1) &&
				 (mInteriorRings.size()>0)) {
				
			Log.warning("BuildingPart with id <"+getId()+"> has "+
					mExteriorRings.size()+" exterior zones and "+
					mInteriorRings.size()+" interior zones");
		}
		*/
		
		mIsOk=true;
		
		return mIsOk;
	}

}
