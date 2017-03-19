package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class OsmRelation extends OsmItem {
	
	private ArrayList<String> mNodeRoles=new ArrayList<String>();
	private ArrayList<OsmNode> mNodes=new ArrayList<OsmNode>();
	
	private ArrayList<String> mWayRoles=new ArrayList<String>();
	private ArrayList<OsmWay> mWays=new ArrayList<OsmWay>();
	
	public OsmRelation() {
		
	}
	
	public OsmRelation(OsmTags tags) {
		
		mTags=tags;
	}
	
	public void addNode(String role, OsmNode node) {
		
		mNodeRoles.add(role);
		mNodes.add(node);
	}
	
	public void addWay(String role, OsmWay way) {
		
		mWayRoles.add(role);
		mWays.add(way);
	}
	
	public ArrayList<OsmNode> getNodes() {
		
		return mNodes;
	}
	
	public ArrayList<OsmWay> getWays() {
		
		return mWays;
	}
	
	public boolean writeToFile(BufferedWriter writer) {
		
		if (mId==0) {
			
			Log.warning("OsmRelation.writeToFile() mId==0");
			return false;
		}
		
		String line=String.format(Locale.US, " <relation id='%d'>\n", mId);
		
		line+=getNodesString();
		
		line+=getWaysString();
		
		line+=getTagsString();
		
		line+=" </relation>\n";
		
		try {
			writer.write(line);
		
		} catch (IOException e) {
			
			Log.error("OsmRelation.writeToFile write() error: "+e.getMessage());
			return false;	
		}
		
		return true;
	}
	
	String getNodesString() {
		
		String line="";
		
		for(int i=0; i<mNodes.size(); i++) {
			
			OsmNode node=mNodes.get(i);
			String role=mNodeRoles.get(i);
			
			if (node.mId==0) {
				
				Log.warning("OsmRelation.getNodesString() node.mId==0");
			}
			else if (role==null) {
				
				Log.warning("OsmRelation.getNodesString() role==null");
			}
			else {
				line+=String.format(Locale.US,
						"  <member type='node' ref='%d' role='%s' />\n",
						node.mId, role);
			}
		}
		
		return line;
	}
	
	String getWaysString() {
		
		String line="";
		
		for(int i=0; i<mWays.size(); i++) {
			
			OsmWay way=mWays.get(i);
			String role=mWayRoles.get(i);
			
			if (way.mId==0) {
				
				Log.warning("OsmRelation.getWaysString() way.mId==0");
			}
			else if (role==null) {
				
				Log.warning("OsmRelation.getWaysString() role==null");
			}
			else {
				line+=String.format(Locale.US,
						"  <member type='way' ref='%d' role='%s' />\n",
						way.mId, role);
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
}
