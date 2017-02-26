package osm.catatom2osm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;

public class AddressParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="AD:Address";
	
	public Address mAddress=null;
	public Street mStreet=null;
	public PostalCode mPostal=null;
	public AdminUnit mAdminUnit=null;
	
	private AddressList mAddressList=new AddressList();
	
	public List<Street> mStreetList=new ArrayList<Street>();
	public List<PostalCode> mPostalList=new ArrayList<PostalCode>();
	public List<AdminUnit> mAdminUnitList=new ArrayList<AdminUnit>();
	
	String mCurrentTag=null;
	
	protected AddressParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	AddressList getAddressList() {
		
		return mAddressList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mAddress!=null) {
			
			Log.warning("Address starts and there's one already created!!");
		}
		
		mAddress=new Address(id);
		
		mElement=mAddress;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mAddress==null) {
			
			Log.warning("Address ends and there's no one opened!!");
		}
		else {
			
			if (mAddress.check()) {
				
				mAddressList.add(mAddress);
			}
			else {
				
				Log.warning("Address.check() returned false!!");
			}
			
			mAddress=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
		
		if (tagName.equalsIgnoreCase("AD:designator")) {
			
			// Detect some data with just new lines and empty spaces
			data=data.replaceAll("\n", "").trim();
			
			if (!data.isEmpty()) {
			
				mAddress.mDesignator=data;
			}
		}
	}
}
