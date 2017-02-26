package osm.catatom2osm;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseElement {
	
	protected boolean mIsOk=false;
	
	private String mId=null;
	
	private String mEPSG=null;
	
	private TextCoord mPos=null;
	
	private ArrayList<TextRing> mRings=new ArrayList<TextRing>();
	
	int mExteriorRingsCount=0;
	int mInteriorRingsCount=0;
	
	private TextRing mEnvelope=null;
	
	protected BaseElement(String id) {
		
		mId=id;
	}
	
	public boolean isOk() {
		
		return mIsOk;
	}
	
	public String getId() {
		
		return mId;
	}
	
	public TextCoord getPos() {
		
		return mPos;
	}
	
	public void setPos(TextCoord pos) {
		
		mPos=pos;
	}
	
	public void setEPSG(String epsg) {
	
		mEPSG=epsg;
	}
	
	public TextRing getEnvelope() {
		
		return mEnvelope;
	}
	
	public void setEnvelope(TextRing envelope) {
		
		mEnvelope=envelope;
	}
	
	public void addRing(TextRing ring) {
		
		mRings.add(ring);
	}
	
	public ArrayList<TextRing> getRings() {
		
		return mRings;
	}
	
	public boolean extractEnvelope() {
		
		if (mEnvelope==null) {
			Log.warning("extractEnvelope() mEnvelope==null");
			return false;
		}
		
		if (!mEnvelope.calculateEnvelope()) {
			Log.warning("extractEnvelope() calculateEnvelope failed!!");
			return false;
		}
		
		return true;
	}
	
	public boolean extractTextCoord() {
		
		if (mEPSG==null) {
			Log.warning("extractTextCoord() mEPSG==null");
			return false;
		}
		
		if (mPos==null) {
			Log.warning("extractTextCoord() mPos==null");
			return false;
		}
		
		if (!mPos.calculateCoordinates(mEPSG)) {
			
			Log.warning("extractTextCoord() calculateCoordinates() failed!!");
			return false;
		}
		
		return true;
	}
	
	public boolean extractTextRings() {
		
		if (mEPSG==null) {
			Log.warning("extractTextRings() mEPSG==null");
			return false;
		}
		
		boolean result=true;
		
		mExteriorRingsCount=0;
		mInteriorRingsCount=0;
		
		Iterator<TextRing> iterator=mRings.iterator();
		
		while(iterator.hasNext()) {
			
			TextRing ring=iterator.next();
			
			if (!ring.calculateCoordinates(mEPSG)) {
				Log.warning("extractTextRings() calculateCoordinates() failed!!");
				result=false;
			}
			
			switch(ring.mRingType) {
			case TextRing.RING_TYPE_EXTERIOR:
				mExteriorRingsCount++;
				break;
				
			case TextRing.RING_TYPE_INTERIOR:
				mInteriorRingsCount++;
				break;
			
			default:
				Log.warning("extractTextRings() Unknown RingType!!");
				break;
			}
		}
		
		return result;
	}
	
	abstract protected boolean check();
}
