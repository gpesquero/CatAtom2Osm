package osm.catatom2osm;

public class Address extends BaseElement {
	
	public String mDesignator=null;
	
	public boolean mAssigned=false;
	
	public String mPostalCodeId=null;
	public String mStreetId=null;
	public String mAdminUnitId=null;
	
	public String mPostalCode=null;
	public String mStreet=null;
	public String mAdminUnit=null;
	
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
		
		if (mPostalCodeId==null) {
			
			Log.warning("Address <"+getId()+"> mPostalCodeId==null!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mStreetId==null) {
			
			Log.warning("Address <"+getId()+"> mStreetId==null!!");
			
			mIsOk=false;
			return false;
		}
		
		if (mAdminUnitId==null) {
			
			Log.warning("Address <"+getId()+"> mAdminUnitId==null!!");
			
			mIsOk=false;
			return false;
		}
		
		mIsOk=true;
		
		return mIsOk;
	}
}
