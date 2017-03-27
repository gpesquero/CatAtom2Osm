package osm.catatom2osm;

public class Building extends BaseElement {
	
	public boolean mAssigned=false;
	
	public String mCondition=null;
	public String mUse=null;
	
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
		
		if (mCondition==null) {
			
			Log.warning("Building <"+getId()+"> has no condition!!");
			
			//mIsOk=false;
			//return false;
		}
		
		if (mUse==null) {
			
			Log.warning("Building <"+getId()+"> has no use!!");
			
			//mIsOk=false;
			//return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
