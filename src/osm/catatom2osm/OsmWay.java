package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;

public class OsmWay extends OsmItem {
	
	private ArrayList<OsmNode> mNodes=new ArrayList<OsmNode>();
	
	public OsmWay() {
		
	}
	
	/*
	public OsmWay(TextRing ring) {
		
		addRing(ring);
	}
	*/
	
	public OsmWay(OsmTags tags/*, TextRing ring*/) {
		
		mTags=tags;
		
		//addRing(ring);
	}
	
	public boolean isClosedWay() {
		
		OsmNode firstNode=mNodes.get(0);
		OsmNode lastNode=mNodes.get(mNodes.size()-1);
		
		if (!firstNode.mLon.equals(lastNode.mLon))
			return false;
		
		if (!firstNode.mLat.equals(lastNode.mLat))
			return false;
		
		return true;
	}
	
	/*
	void addRing(TextRing ring) {
		
		Iterator<TextCoord> iter=ring.iterator();
		
		while(iter.hasNext()) {
			
			TextCoord coord=iter.next();
			
			OsmNode node=new OsmNode(coord.mLon, coord.mLat);
			
			addNode(node);
		}
	}
	*/
	
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
	
boolean containsNodeWithId(int nodeId) {
		
		Iterator<OsmNode> nodeIter=mNodes.iterator();
		
		while(nodeIter.hasNext()) {
			
			OsmNode node=nodeIter.next();
			
			if (node.mId==nodeId)
				return true;
		}
		return false;
	}
	
	double simplifyNode(int nodeId) {
		
		//Log.info("simplifyNode() nodeId="+nodeId);
		
		int numNodes=mNodes.size();
		
		if (numNodes<4) {
			
			// Only ways with more that 3 nodes can be simplified!!
			return -1.0;
		}
		
		boolean closedWay=isClosedWay();
		
		for(int i=0; i<numNodes; i++) {
			
			OsmNode node=mNodes.get(i);
			
			if (node.mId==nodeId) {
				
				// This is the node that we have to analyze
				
				int prevNodeId=-1;
				int nextNodeId=-1;
				
				// First, get the previous node in the way
				if (i==0) {
					
					// This is the first node of the way
					
					if (closedWay) {
						
						// This is a closed way.
						// The previous node is one of the end
						prevNodeId=numNodes-2;
					}
					else {
						
						// This is not a closed way
						// There s no previous node!!
						return -1.0;
					}
				}
				else {
					prevNodeId=i-1;
				}
				
				// Now, get the next node in the way
				if (i==(numNodes-1)) {
					
					// This is the last node of the way
					
					if (closedWay) {
						
						// This is a closed way.
						// The next node is one of the beginning
						nextNodeId=1;
					}
					else {
						
						// This is not a closed way
						// There s no next node!!
						return -1.0;
					}
				}
				else {
					nextNodeId=i+1;
				}
				
				if (prevNodeId<0) {
					
					Log.error("simplifyNode() prevNodeId<0");
					return -1.0;
				}
				
				if (nextNodeId<0) {
					
					Log.error("simplifyNode() nextNodeId<0");
					return -1.0;
				}
				
				OsmNode prevNode=mNodes.get(prevNodeId);
				OsmNode nextNode=mNodes.get(nextNodeId);
				
				double x0=Double.parseDouble(prevNode.mLon);
				double y0=Double.parseDouble(prevNode.mLat);
				
				double x1=Double.parseDouble(node.mLon);
				double y1=Double.parseDouble(node.mLat);
				
				double x2=Double.parseDouble(nextNode.mLon);
				double y2=Double.parseDouble(nextNode.mLat);
				
				Coordinate c0=new Coordinate(x0, y0);
				Coordinate c1=new Coordinate(x1, y1);
				Coordinate c2=new Coordinate(x2, y2);
				
				double angle=Angle.interiorAngle(c0, c1, c2);
				
				/*
				Log.info("simplifyNode() i="+i+", prevNode="+prevNodeId+
						", nextNode="+nextNodeId);
				
				Log.info("simplifyNode() angle="+
						String.format(Locale.US, "%.03f", Math.toDegrees(angle)));
				*/
				
				return angle;
			}
		}
		
		return -1.0;
	}
}
