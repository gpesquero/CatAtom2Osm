package osm.catatom2osm;

public class Parcel extends BaseElement {
	
	public boolean mAssigned=false;
	
	protected Parcel(String id) {
		super(id);
	}
	
	@Override
	protected boolean check() {
		
		if (!extractTextRings()) {
			
			Log.warning("Parcel <"+getId()+"> extractTextRings() failed!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mExteriorRingsCount==0) {
			
			Log.warning("Parcel <"+getId()+"> has no external rings!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
