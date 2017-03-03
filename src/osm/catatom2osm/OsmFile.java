package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class OsmFile {
	
	BufferedWriter mOutputBuffer=null;
	
	ArrayList<OsmNode> mNodes=new ArrayList<OsmNode>();
	ArrayList<OsmWay> mWays=new ArrayList<OsmWay>();
	ArrayList<OsmRelation> mRelations=new ArrayList<OsmRelation>();

	public OsmFile(File dir, String fileName) {
		
		File outFile=new File(dir, fileName);
		
		FileWriter writer;
		
		try {
			writer = new FileWriter(outFile);
			
		} catch (IOException e) {
			
			Log.error("Error in FileWriter(): "+e.getMessage());
			return;
		}
		
		mOutputBuffer=new BufferedWriter(writer);
		
		try {
			
			mOutputBuffer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			mOutputBuffer.write("<osm version=\"0.6\" generator=\""+Main.mAppName+"\">\n");
			
		} catch (IOException e) {
			
			Log.error("Error in mOutputBuffer.writer(): "+e.getMessage());
			return;				
		}
		
		Log.info("Creating output file <"+outFile.getAbsolutePath()+">");
	}
	
	void addAddresses(AddressList addresses) {
		
		Iterator<Address> iterator=addresses.iterator();
		
		while(iterator.hasNext()) {
			
			Address address=iterator.next();
			
			if (!address.isOk())
				continue;
			
			TextCoord pos=address.getPos();
			
			OsmNode newNode=new OsmNode(pos.mLon, pos.mLat);
			
			newNode.setTag("addr:housenumber", address.mDesignator);
			
			if (address.mStreet!=null)
				newNode.setTag("addr:street", address.mStreet);
			
			if (address.mPostalCode!=null)
				newNode.setTag("addr:postcode", address.mPostalCode);
			
			if (address.mAdminUnit!=null)
				newNode.setTag("addr:city", address.mAdminUnit);
			
			addNode(newNode);
		}
	}
	
	void addRings(OsmTags tags, ArrayList<TextRing> rings) {
		
		if (rings.size()==0) {
			
			Log.warning("addRings() Element has no rings!!");
		}
		else if (rings.size()>1) {
		
			// The element contains more than 1 ring.
			// Create a multipolygon relation
			
			tags.put("type", "multipolygon");
			
			OsmRelation relation=new OsmRelation(tags);
			
			Iterator<TextRing> ringIter=rings.iterator();
			
			while(ringIter.hasNext()) {
				
				TextRing ring=ringIter.next();
				
				String role=null;
				
				switch(ring.mRingType) {
				
				case TextRing.RING_TYPE_EXTERIOR:
					role="outer";
					break;
					
				case TextRing.RING_TYPE_INTERIOR:
					role="inner";
					break;
					
				default:
					role=null;
					break;
				}
				
				if (role==null) {
					
					Log.warning("addRings() incorrect RingType");
				}
				else {
					
					OsmWay way=new OsmWay(ring);
					
					relation.addWay(role, way);
				}
			}
			
			
			addRelation(relation);
		}
		else {
			
			// The building has a single ring. Create a way
			
			OsmWay way=new OsmWay(tags, rings.get(0));
			
			addWay(way);
		}
	}
	
	void addBuildings(BuildingList buildings) {
		
		Iterator<Building> buildingIter=buildings.iterator();
		
		while(buildingIter.hasNext()) {
			
			Building building=buildingIter.next();
			
			if (!building.isOk())
				continue;
			
			ArrayList<TextRing> rings=building.getRings();
			
			OsmTags tags=new OsmTags();
			tags.put("building", "yes");
			
			addRings(tags, rings);
		}
	}
	
	void addBuildingParts(BuildingPartList buildingParts) {
		
		Iterator<BuildingPart> buildingPartIter=buildingParts.iterator();
		
		while(buildingPartIter.hasNext()) {
			
			BuildingPart buildingPart=buildingPartIter.next();
			
			if (!buildingPart.isOk())
				continue;
			
			ArrayList<TextRing> rings=buildingPart.getRings();
			
			OsmTags tags=new OsmTags();
			tags.put("building:part", "yes");
			
			tags.put("building:levels",
					String.format("%d", buildingPart.mNumFloorsAboveGround));
			
			if (buildingPart.mNumFloorsBelowGround>0) {
				
				tags.put("building:levels:underground",
					String.format("%d", buildingPart.mNumFloorsBelowGround));
			}
			
			addRings(tags, rings);
		}
	}
	
	void addZones(ZoneList zones) {
		
		Iterator<Zone> zoneIter=zones.iterator();
		
		while(zoneIter.hasNext()) {
			
			Zone zone=zoneIter.next();
			
			if (!zone.isOk())
				continue;
			
			ArrayList<TextRing> rings=zone.getRings();
			
			OsmTags tags=new OsmTags();
			tags.put("area", "yes");
			
			addRings(tags, rings);
		}
	}
	
	boolean addNode(OsmNode node) {
		
		int id=-(mNodes.size()+1);
		
		node.setId(id);
		
		mNodes.add(node);
		
		return true;
	}
	
	boolean addWay(OsmWay way) {
		
		int id=-(mWays.size()+1);
		
		way.setId(id);
		
		mWays.add(way);
		
		return true;
	}
	
	boolean addRelation(OsmRelation relation) {
		
		int id=-(mRelations.size()+1);
		
		relation.setId(id);
		
		mRelations.add(relation);
		
		return true;
	}
	
	/*
	private boolean addMultipolygon(OsmTags tags,
			ArrayList<TextRing> exteriorRings,
			ArrayList<TextRing> interiorRings) {
		
		if ((exteriorRings.size()>1) && 
			(interiorRings.size()>0)) {
				
			Log.warning("addArea() with "+exteriorRings.size()+
					" external zones and "+interiorRings.size()+
					" internal zones. Unhandled situation");
			
			return false;
		}
		
		if (exteriorRings.size()<1) {
			
			Log.warning("addArea() with no exterior rings!!");
			return false;
		}
		
		if (interiorRings.size()>0) {
			
			//addMultipolygon(tags, exteriorRings.get(0), interiorRings);
		}
		else {
			
			addWay(tags, exteriorRings);
		}
		
		return true;
	}
	*/
	
	/*
	OsmWay generateWay(OsmTags tags, ArrayList<TextRing> exteriorRings) {
		
		OsmWay way=new OsmWay();
		
		
		
		
		
		return way;
	}
	*/
	
	boolean writeDataToFile() {
		
		// Make sure that all the relations, ways and nodes have an Id
		// by adding all of them to the OsmFile
		assignIds();
		
		Iterator<OsmNode> nodeIter=mNodes.iterator();
		
		while(nodeIter.hasNext()) {
			
			OsmNode node=nodeIter.next();
			
			if (!node.writeToFile(mOutputBuffer)) {
				
				break;
			}
		}
		
		Iterator<OsmWay> wayIter=mWays.iterator();
		
		while(wayIter.hasNext()) {
			
			OsmWay way=wayIter.next();
			
			if (!way.writeToFile(mOutputBuffer)) {
				
				break;
			}
		}
		
		Iterator<OsmRelation> relationIter=mRelations.iterator();
		
		while(relationIter.hasNext()) {
			
			OsmRelation relation=relationIter.next();
			
			if (!relation.writeToFile(mOutputBuffer)) {
				
				break;
			}
		}
		
		Log.info("Written to disk "+mNodes.size()+" nodes, "+mWays.size()+
				" ways and "+mRelations.size()+" relations");
		
		return true;
	}

	public void close() {
		
		try {
			mOutputBuffer.write("</osm>\n");
			mOutputBuffer.close();
			
		} catch (IOException e) {
			
			Log.error("Error in mOutputBuffer.writer(): "+e.getMessage());
		}		
	}
	
	void assignIds() {
		
		// Check for unassigned ids in ways and nodes of the relations
		
		Iterator<OsmRelation> relationIter=mRelations.iterator();
		
		while(relationIter.hasNext()) {
			
			OsmRelation relation=relationIter.next();
			
			ArrayList<OsmNode> nodes=relation.getNodes();
			
			// Add all the nodes of the relation
			
			Iterator<OsmNode> nodeIter=nodes.iterator();
			
			while(nodeIter.hasNext()) {
				
				OsmNode node=nodeIter.next();
				
				if (node.mId==0) {
					addNode(node);
				}
			}
			
			// Add all the ways of the relation
			ArrayList<OsmWay> ways =relation.getWays();
			
			Iterator<OsmWay> wayIter=ways.iterator();
			
			while(wayIter.hasNext()) {
				
				OsmWay way=wayIter.next();
				
				if (way.mId==0) {
					addWay(way);
				}
			}
		}
		
		// Check for unassigned ids in nodes of the ways
		
		Iterator<OsmWay> wayIter=mWays.iterator();
		
		while(wayIter.hasNext()) {
			
			OsmWay way=wayIter.next();
			
			ArrayList<OsmNode> nodes=way.getNodes();
			
			// Add all the nodes of the way
			
			Iterator<OsmNode> nodeIter=nodes.iterator();
			
			while(nodeIter.hasNext()) {
				
				OsmNode node=nodeIter.next();
				
				if (node.mId==0) {
					addNode(node);
				}
			}
		}
	}
}
