package osm.catatom2osm;

public class Address extends BaseElement {
	
	public String mDesignator=null;
	
	public boolean mAssigned=false;
	
	
	public Address(String id) {
		super(id);
		
	}
	
	public void setDesignator(String designator) {
		
		mDesignator=designator;
	}
	
	public String getDesignator() {
		
		return mDesignator;
	}
	
	public boolean check() {
		
		if (!extractTextCoord()) {
			
			Log.warning("Address <"+getId()+"> extractTextCoord() failed!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mDesignator==null) {
			
			Log.warning("Address <"+getId()+"> designator==null!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
