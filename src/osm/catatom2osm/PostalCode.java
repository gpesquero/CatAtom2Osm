package osm.catatom2osm;

public class PostalCode extends BaseElement {
	
	public String mCode=null;

	protected PostalCode(String id) {
		super(id);
	}

	@Override
	protected boolean check() {
		
		if (mCode==null) {
			
			Log.warning("PostalCode <"+getId()+"> mCode==null!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
