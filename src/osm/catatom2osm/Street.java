package osm.catatom2osm;

public class Street extends BaseElement {
	
	public String mName=null;
	
	public Street(String id) {
		super(id);
	}

	@Override
	protected boolean check() {
		
		if (mName==null) {
			
			Log.warning("Street <"+getId()+"> mName==null!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
