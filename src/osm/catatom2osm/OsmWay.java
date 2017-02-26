package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class OsmWay extends OsmItem {
	
	private ArrayList<OsmNode> mNodes=new ArrayList<OsmNode>();
	
	public OsmWay() {
		
	}
	
	public OsmWay(TextRing ring) {
		
		addRing(ring);
	}
	
	public OsmWay(OsmTags tags, TextRing ring) {
		
		mTags=tags;
		
		addRing(ring);
	}
	
	void addRing(TextRing ring) {
		
		Iterator<TextCoord> iter=ring.iterator();
		
		while(iter.hasNext()) {
			
			TextCoord coord=iter.next();
			
			OsmNode node=new OsmNode(coord.mLon, coord.mLat);
			
			addNode(node);
		}
	}
	
	void addNode(OsmNode node) {
		
		mNodes.add(node);
	}
	
	ArrayList<OsmNode> getNodes() {
		
		return mNodes;
	}
	
	public boolean writeToFile(BufferedWriter writer) {
		
		if (mId==0) {
			
			Log.warning("OsmWay.writeToFile() mId==0");
			return false;
		}
		
		if (mNodes.size()<2) {
			
			Log.warning("OsmWay.writeToFile() way has only "+mNodes.size()+" node(s)");
			return false;
		}
		
		/*
		if (mTimeStamp==null) {
			
			Log.error("Error in node.writeToFile mTimeStamp==null");
			return false;
		}
		
		String line=String.format(Locale.US,
			" <node id=\"%d\" timestamp=\"%s\" version=\"6\" lat=\"%.07f\" lon=\"%.07f\">\n",
			mId, mTimeStamp, mLat, mLon);
		*/
		
		String line=String.format(Locale.US, " <way id='%d'>\n", mId);
		
		line+=getNodesString();
		
		line+=getTagsString();
		
		line+=" </way>\n";
		
		try {
			writer.write(line);
		
		} catch (IOException e) {
			
			Log.error("OsmWay.writeToFile write() error: "+e.getMessage());
			return false;	
		}
		
		return true;
	}
	
	String getNodesString() {
		
		String line="";
		
		Iterator<OsmNode> iterator=mNodes.iterator();
		
		while(iterator.hasNext()) {
			
			OsmNode node=iterator.next();
			
			if (node.mId==0) {
				
				Log.warning("OsmWay.getNodesString() node.mId==0");
			}
			else {
				line+=String.format(Locale.US, "  <nd ref='%d' />\n", node.mId);
			}
		}
		
		return line;
	}

}
