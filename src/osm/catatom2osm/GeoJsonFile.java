package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class GeoJsonFile {
	
	private final static String HEAD1     ="{\n";
	private final static String HEAD2     ="  \"type\": \"FeatureCollection\",\n";
	private final static String HEAD3     ="  \"features\": [";
	
	private final static String FEAT_HEAD1="    {\n";
	private final static String FEAT_HEAD2="      \"type\": \"Feature\",\n";
	private final static String FEAT_HEAD3="      \"properties\": {},\n";
	private final static String FEAT_HEAD4="      \"geometry\": {\n";
	private final static String FEAT_HEAD5="        \"type\": \"Polygon\",\n";
	private final static String FEAT_HEAD6="        \"coordinates\": [\n";
	private final static String FEAT_HEAD7="          [\n";
	
	private final static String COORD1    ="            [\n";
	private final static String COORD2    ="              %s,\n";
	private final static String COORD3    ="              %s\n";
	private final static String COORD4    ="            ]";
	
	private final static String FEAT_END1 ="          ]\n";
	private final static String FEAT_END2 ="        ]\n";
	private final static String FEAT_END3 ="      }\n";
	private final static String FEAT_END4 ="    }";
	
	private final static String END1      ="  ]\n";
	private final static String END2      ="}\n";
	
	private BufferedWriter mOutputBuffer=null;
	
	private int mFeatureCount=0;
	
	public GeoJsonFile(File dir, String fileName) {
		
		File outFile=new File(dir, fileName);
		
		FileWriter writer;
		
		try {
			writer = new FileWriter(outFile);
		}
		catch (IOException e) {
			
			Log.error("Error in GeoJsonFile new FileWriter(): "+e.getMessage());
			return;
		}
		
		mOutputBuffer=new BufferedWriter(writer);
		
		try {
			mOutputBuffer.write(HEAD1);
			mOutputBuffer.write(HEAD2);
			mOutputBuffer.write(HEAD3);
			
		}
		catch (IOException e) {
			
			Log.error("Error in GeoJsonFile write(): "+e.getMessage());
			return;
		}
		
		Log.info("Creating output file <"+outFile.getAbsolutePath()+">");
	}
	
	boolean writeZones(ZoneList zones) {
		
		try {
			
			Iterator<Zone> zoneIter=zones.iterator();
			
			while(zoneIter.hasNext()) {
				
				Zone zone=zoneIter.next();
				
				if (!zone.isOk()) {
					Log.warning("Zone with id=<"+zone.getId()+"> is not Ok!!");
					continue;
				}
				
				Iterator<TextRing> ringIter=zone.getRings().iterator();
				
				while(ringIter.hasNext()) {
					
					TextRing ring=ringIter.next();
					
					if (ring.mRingType==TextRing.RING_TYPE_EXTERIOR) {
						
						writeFeature(ring);
					}
				}
			}
		
		mOutputBuffer.write("\n");
		}
		catch (IOException e) {
			
			Log.error("Error in GeoJsonFile write(): "+e.getMessage());
			return false;
		}
		
		return true;
	}
	
	void writeFeature(TextRing ring) throws IOException {
		
		if (mFeatureCount>0) {
			mOutputBuffer.write(",");
		}
		
		mOutputBuffer.write("\n");
		
		mOutputBuffer.write(FEAT_HEAD1);
		mOutputBuffer.write(FEAT_HEAD2);
		mOutputBuffer.write(FEAT_HEAD3);
		mOutputBuffer.write(FEAT_HEAD4);
		mOutputBuffer.write(FEAT_HEAD5);
		mOutputBuffer.write(FEAT_HEAD6);
		mOutputBuffer.write(FEAT_HEAD7);
		
		Iterator<TextCoord> coordIter=ring.iterator();
		
		int numCoords=0;
		
		while(coordIter.hasNext()) {
			
			if (numCoords>0) {
				
				mOutputBuffer.write(",\n");
			}
			
			TextCoord coord=coordIter.next();
			
			mOutputBuffer.write(COORD1);
			mOutputBuffer.write(String.format(COORD2, coord.mLon));
			mOutputBuffer.write(String.format(COORD3, coord.mLat));
			mOutputBuffer.write(COORD4);
			
			numCoords++;
		}
		
		mOutputBuffer.write("\n");
		
		mOutputBuffer.write(FEAT_END1);
		mOutputBuffer.write(FEAT_END2);
		mOutputBuffer.write(FEAT_END3);
		mOutputBuffer.write(FEAT_END4);
		
		mFeatureCount++;
	}
	
	public void close() {
		
		try {
			mOutputBuffer.write(END1);
			mOutputBuffer.write(END2);
			mOutputBuffer.close();
		}
		catch (IOException e) {
			
			Log.error("Error in GeoJsonFile close(): "+e.getMessage());
			return;
		}
		
		Log.info("Written "+mFeatureCount+" zones in GeoJSON zone file");
	}
}
