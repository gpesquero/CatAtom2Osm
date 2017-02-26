package osm.catatom2osm;

public class Zone extends BaseElement {
	
	public String mZoneType=null;
	
	public Zone(String id) {
		super(id);
		
	}
	
	@Override
	protected boolean check() {
		
		if (!extractTextRings()) {
			
			Log.warning("Zone <"+getId()+"> extractTextRings() failed!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mExteriorRingsCount==0) {
			
			Log.warning("Zone <"+getId()+"> has no external rings!!");
			
			mIsOk=false;
			return false;
		}
		
		if (!extractEnvelope()) {
			
			Log.warning("Zone <"+getId()+"> extractEnvelope() failed!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
