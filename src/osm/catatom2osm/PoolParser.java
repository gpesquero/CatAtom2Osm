package osm.catatom2osm;

import java.io.InputStream;

import org.xml.sax.Attributes;

public class PoolParser extends BaseGmlParser {
	
	final static String ELEMENT_TAG="bu-ext2d:OtherConstruction";
	
	public Pool mPool=null;
	
	private PoolList mPoolList=new PoolList();
	
	protected PoolParser(InputStream inputStream) {
		super(inputStream, ELEMENT_TAG);
	}
	
	PoolList getPools() {
		
		return mPoolList;
	}
	
	@Override
	protected void onElementStart(String id) {
		
		if (mPool!=null) {
			
			Log.warning("Pool starts and there's one already created!!");
		}
		
		mPool=new Pool(id);
		
		mElement=mPool;
	}	
	
	@Override
	protected void onElementEnd() {
		
		if (mPool==null) {
			
			Log.warning("Pool ends and there's no one opened!!");
		}
		else {
			
			if (mPool.check()) {
				
				mPoolList.add(mPool);
			}
			else {
				
				Log.warning("Pool.check() returned false!!");
			}
			
			mPool=null;
		}
	}
	
	@Override
	protected void onTag(String tagName, Attributes attributes) {
		
	}
	
	@Override
	protected void onData(String tagName, String data) {
	}
}
