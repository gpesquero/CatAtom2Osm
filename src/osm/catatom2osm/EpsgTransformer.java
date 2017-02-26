package osm.catatom2osm;

import java.util.HashMap;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

public class EpsgTransformer {
	
	final private String mTargetCode="EPSG:4326";		// "WGS84"
	
	public CoordinateReferenceSystem mTargetCRS=null;
	
	private HashMap<String, MathTransform> mTransforms=new HashMap<String, MathTransform>();
	
	public EpsgTransformer() {
		
		try {
			
			mTargetCRS=CRS.decode(mTargetCode);				
			Log.info("Target CRS.decode("+mTargetCode+"): Ok!");
				
		} catch (FactoryException e) {
				
			Log.error("Target CRS.decode("+mTargetCode+") failed: "+e.getMessage());
			mTargetCRS=null;
		}
	}
	
	public boolean isOk() {
		
		return (mTargetCRS!=null);
	}
	
	Coordinate transform(Coordinate srcCoord, String srcEPSG) {
		
		if (mTargetCRS==null) {
			
			Log.error("Target CRS==null");
			return null;
		}
		
		MathTransform transform=getTransform(srcEPSG);
		
		if (transform==null) {
			
			return null;
		}
		
		Coordinate dstCoord;
		
		try {
			dstCoord=JTS.transform(srcCoord, null, transform);
			
		} catch (TransformException e) {
			
			Log.error("Transform Exception: "+e.getMessage());
			return null;
		}
		
		return dstCoord;		
	}
	
	MathTransform getTransform(String EPSG) {
		
		MathTransform transform=mTransforms.get(EPSG);
		
		if (transform==null) {
			
			transform=createTransform(EPSG);
			
			if (transform!=null) {
				
				mTransforms.put(EPSG, transform);
			}
		}
		
		return transform;		
	}
	
	MathTransform createTransform(String srcEPSGCode) {
		
		CoordinateReferenceSystem sourceCRS;
		
		try {
			
			sourceCRS=CRS.decode(srcEPSGCode);
			Log.info("Source CRS.decode("+srcEPSGCode+"): Ok!");
			
		} catch (FactoryException e) {
				
			Log.error("Source CRS.decode("+srcEPSGCode+") failed: "+e.getMessage());
			return null;
		}
		
		MathTransform transform;
		
		try {
			transform=CRS.findMathTransform(sourceCRS, mTargetCRS, false);
			
		} catch (FactoryException e) {
			
			Log.error("FindMathTransform failed: "+e.getMessage());
			return null;
		}
		
		return transform;
	}
}
