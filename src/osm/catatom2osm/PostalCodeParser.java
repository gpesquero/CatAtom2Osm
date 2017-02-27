package osm.catatom2osm;

import java.io.InputStream;

import org.xml.sax.Attributes;

public class PostalCodeParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="AD:PostalDescriptor";
	
	public PostalCode mPostalCode=null;
	
	private PostalCodeList mPostalCodeList=new PostalCodeList();
	
	protected PostalCodeParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	PostalCodeList getPostalCodes() {
		
		return mPostalCodeList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mPostalCode!=null) {
			
			Log.warning("PostalCode starts and there's one already created!!");
		}
		
		mPostalCode=new PostalCode(id);
		
		mElement=mPostalCode;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mPostalCode==null) {
			
			Log.warning("PostalCode ends and there's no one opened!!");
		}
		else {
			
			if (mPostalCode.check()) {
				
				mPostalCodeList.add(mPostalCode);
			}
			else {
				
				Log.warning("PostalCode.check() returned false!!");
			}
			
			mPostalCode=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
		
		if (tagName.equalsIgnoreCase("AD:postCode")) {
			
			mPostalCode.mCode=data.trim();
		}
	}
}