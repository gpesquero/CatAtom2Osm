package osm.catatom2osm;

import java.io.InputStream;
import java.util.Iterator;

import org.xml.sax.Attributes;

public class ZoneParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="cp:CadastralZoning";
	
	private Zone mZone=null;
	
	ZoneList mZoneList=new ZoneList();
	
	protected ZoneParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
		
	}
	
	ZoneList getZones() {
		
		return mZoneList;
	}
	
	ZoneList getSpecificZones(String zoneType) {
		
		ZoneList result=new ZoneList();
		
		Iterator<Zone> iter=mZoneList.iterator();
		
		while(iter.hasNext()) {
			
			Zone zone=iter.next();
			
			if (zone.mZoneType.equalsIgnoreCase(zoneType)) {
				
				result.add(zone);
			}
		}
		
		return result;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mZone!=null) {
			
			Log.warning("Zone starts and there's one already created!!");
		}
		
		mZone=new Zone(id);
		
		mElement=mZone;
	}
	
	@Override
	protected void onElementEnd() {
		
		if (mZone==null) {
					
			Log.warning("Zone ends and there's no one opened!!");
		}
		else {
			
			if (mZone.check()) {
				
				mZoneList.add(mZone);
			}
			else {
				
				Log.warning("Zone.check() returned false!!");
			}
			
			mZone=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
		
		if (tagName.equalsIgnoreCase("gmd:LocalisedCharacterString")) {
			
			if (data.startsWith("MANZANA")) {
				
				mZone.mZoneType="MANZANA";
			}
			else if (data.startsWith("POLIGONO")) {
				
				mZone.mZoneType="POLIGONO";
			}
			else {
				Log.warning("gmd:LocalisedCharacterString unknown data <"+
						data+">");
				mZone.mZoneType=null;
			}
		}
	}
}
