package osm.catatom2osm;

public class Building extends BaseElement {
	
	public boolean mAssigned=false;
	
	protected Building(String id) {
		super(id);
	}
	
	@Override
	protected boolean check() {
		
		if (!extractTextRings()) {
			
			Log.warning("Building <"+getId()+"> extractTextRings() failed!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mExteriorRingsCount==0) {
			
			Log.warning("Building <"+getId()+"> has no external rings!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
