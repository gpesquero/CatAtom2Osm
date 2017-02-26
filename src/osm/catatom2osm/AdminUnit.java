package osm.catatom2osm;

public class AdminUnit extends BaseElement {
	
	public String mName=null;
	
	public AdminUnit(String id) {
		super(id);
		
	}

	@Override
	protected boolean check() {
		
		if (mName==null) {
			
			Log.warning("AdminUnit <"+getId()+"> mName==null!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
