package osm.catatom2osm;

import java.io.InputStream;

import org.xml.sax.Attributes;

public class ParcelParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="cp:CadastralParcel";
	
	public Parcel mParcel=null;
	
	private ParcelList mParcelList=new ParcelList();
	
	protected ParcelParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	ParcelList getParcels() {
		
		return mParcelList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mParcel!=null) {
			
			Log.warning("Parcel starts and there's one already created!!");
		}
		
		mParcel=new Parcel(id);
		
		mElement=mParcel;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mParcel==null) {
			
			Log.warning("Parcel ends and there's no one opened!!");
		}
		else {
			
			if (mParcel.check()) {
				
				mParcelList.add(mParcel);
			}
			else {
				
				Log.warning("Parcel.check() returned false!!");
			}
			
			mParcel=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
	}
	
	boolean checkParcelList() {
		
		return false;
	}
}
