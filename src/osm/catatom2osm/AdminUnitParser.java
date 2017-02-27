package osm.catatom2osm;

import java.io.InputStream;

import org.xml.sax.Attributes;

public class AdminUnitParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="AD:AdminUnitName";
	
	public AdminUnit mAdminUnit=null;
	
	private AdminUnitList mAdminUnitList=new AdminUnitList();
	
	protected AdminUnitParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	AdminUnitList getAdminUnits() {
		
		return mAdminUnitList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mAdminUnit!=null) {
			
			Log.warning("AdminUnit starts and there's one already created!!");
		}
		
		mAdminUnit=new AdminUnit(id);
		
		mElement=mAdminUnit;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mAdminUnit==null) {
			
			Log.warning("AdminUnit ends and there's no one opened!!");
		}
		else {
			
			if (mAdminUnit.check()) {
				
				mAdminUnitList.add(mAdminUnit);
			}
			else {
				
				Log.warning("AdminUnit.check() returned false!!");
			}
			
			mAdminUnit=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
		
		if (tagName.equalsIgnoreCase("GN:text")) {
			
			mAdminUnit.mName=data.trim();
		}
	}
	
	boolean checkAdminUnitList() {
		
		return false;
	}
}
