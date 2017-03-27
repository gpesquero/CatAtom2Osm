package osm.catatom2osm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

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
			
			mOutputBuffer.write("<?xml version='1.0' encoding='UTF-8'?>\n");
			mOutputBuffer.write("<osm version='0.6' generator='"+Main.mAppName+"' upload='false'>\n");
			
		} catch (IOException e) {
			
			Log.error("Error in mOutputBuffer.writer(): "+e.getMessage());
			return;
		}
		
		Log.info("Creating output file <"+outFile.getAbsolutePath()+">");
	}
	
	OsmNode createNode(String lon, String lat) {
		
		// First, check if there's an existing node in the same place
		
		for(int i=0; i<mNodes.size(); i++) {
			
			OsmNode node=mNodes.get(i);
			
			if ((node.mLon.equals(lon)) &&
				(node.mLat.equals(lat))) {
				
				return node;
			}
		}
		
		// No existing node detected in this place. Create a new one
		
		OsmNode newNode=new OsmNode(lon, lat);
		
		int id=-(mNodes.size()+1);
		
		newNode.setId(id);
		
		mNodes.add(newNode);
		
		return newNode;
	}
	
	OsmWay createWay(OsmTags tags, TextRing ring) {
		
		if (tags==null) {
			tags=new OsmTags();
		}
		
		OsmWay newWay=new OsmWay(tags);
		
		Iterator<TextCoord> iter=ring.iterator();
		
		while(iter.hasNext()) {
			
			TextCoord coord=iter.next();
			
			OsmNode node=createNode(coord.mLon, coord.mLat);
			
			newWay.addNode(node);
		}
		
		int id=-(mWays.size()+1);
		
		newWay.setId(id);
		
		mWays.add(newWay);
		
		return newWay;
	}
	
	OsmRelation createRelation(OsmTags tags) {
		
		OsmRelation newRelation=new OsmRelation(tags);
		
		int id=-(mRelations.size()+1);
		
		newRelation.setId(id);
		
		mRelations.add(newRelation);
		
		return newRelation;
	}
	
	void addAddresses(AddressList addresses) {
		
		Log.info("addAddresses() starts...");
		
		//Iterator<Address> iterator=addresses.iterator();
		
		//while(iterator.hasNext()) {
		
		int size=addresses.size();
		
		for(int i=0; i<size; i++) {
			
			//Address address=iterator.next();
			Address address=addresses.get(i);
			
			if (!address.isOk())
				continue;
			
			TextCoord pos=address.getPos();
			
			//OsmNode newNode=new OsmNode(pos.mLon, pos.mLat);
			OsmNode node=createNode(pos.mLon, pos.mLat);
			
			node.setTag("addr:housenumber", address.mDesignator);
			
			if (address.mStreet!=null)
				node.setTag("addr:street", address.mStreet);
			
			if (address.mPostalCode!=null)
				node.setTag("addr:postcode", address.mPostalCode);
			
			if (address.mAdminUnit!=null)
				node.setTag("addr:city", address.mAdminUnit);
			
			//addNode(newNode);
			
			if (i%1000==0) {
				
				Log.info("addAddresses() progress="+String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("addAddresses() progress=100%");
	}
	
	void addRings(OsmTags tags, ArrayList<TextRing> rings) {
		
		if (rings.size()==0) {
			
			Log.warning("addRings() Element has no rings!!");
		}
		else if (rings.size()>1) {
		
			// The element contains more than 1 ring.
			// Create a multipolygon relation
			
			tags.put("type", "multipolygon");
			
			//OsmRelation relation=new OsmRelation(tags);
			OsmRelation relation=createRelation(tags);
			
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
					
					//OsmWay way=new OsmWay(ring);
					OsmWay way=createWay(null, ring);
					
					relation.addWay(role, way);
				}
			}
			
			//addRelation(relation);
		}
		else {
			
			// The building has a single ring. Create a way
			
			/*
			OsmWay way=new OsmWay(tags, rings.get(0));
			
			addWay(way);
			*/
			
			createWay(tags, rings.get(0));
		}
	}
	
	void addBuildings(BuildingList buildings) {
		
		Log.info("addBuildings() starts...");
		
		//Iterator<Building> buildingIter=buildings.iterator();
		
		//while(buildingIter.hasNext()) {
		
		int size=buildings.size();
		
		for(int i=0; i<size; i++) {
			
			//Building building=buildingIter.next();
			Building building=buildings.get(i);
			
			if (!building.isOk())
				continue;
			
			ArrayList<TextRing> rings=building.getRings();
			
			OsmTags tags=new OsmTags();
			//tags.put("building", "yes");
			
			if (building.mUse==null) {
				
				building.mUse="yes";
			}
			else {
				
				if (building.mUse.equalsIgnoreCase("1_residential")) {
					
					building.mUse="residential";
				}
				else if (building.mUse.equalsIgnoreCase("2_agriculture")) {
					
					building.mUse="barn";
				}
				else if (building.mUse.equalsIgnoreCase("3_industrial")) {
					
					building.mUse="industrial";
				}
				else if (building.mUse.equalsIgnoreCase("4_1_office")) {
					
					building.mUse="office";
				}
				else if (building.mUse.equalsIgnoreCase("4_2_retail")) {
					
					building.mUse="retail";
				}
				else if (building.mUse.equalsIgnoreCase("4_3_publicServices")) {
					
					building.mUse="public";
				}
				else {
					building.mUse="yes";
				}
			}
			
			if (building.mCondition==null) {
				
				building.mUse="functional";
			}
			
			if (building.mCondition.equalsIgnoreCase("ruin")) {
				
				tags.put("building", "ruins");
				tags.put("ruins:building", building.mUse);
			}
			else if (building.mCondition.equalsIgnoreCase("declined")) {
				
				tags.put("building", "ruins");
				tags.put("abandoned:building", building.mUse);
				
			}
			else {
				
				tags.put("building", building.mUse);
			}
			
			addRings(tags, rings);
			
			if (i%1000==0) {
				
				Log.info("addBuildings() progress="+String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("addBuildings() progress=100%");
	}
	
	void addBuildingParts(BuildingPartList buildingParts) {
		
		Log.info("addBuildingParts() starts...");
		
		//Iterator<BuildingPart> buildingPartIter=buildingParts.iterator();
		
		//while(buildingPartIter.hasNext()) {
		
		int size=buildingParts.size();
		
		for(int i=0; i<size; i++) {
			
			//BuildingPart buildingPart=buildingPartIter.next();
			BuildingPart buildingPart=buildingParts.get(i);
			
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
			
			if (i%1000==0) {
				
				Log.info("addBuildingParts() progress="+String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("addBuildingParts() progress=100%");
	}
	
	void addParcels(ParcelList parcels) {
		
		Log.info("addParcels() starts...");
		
		int size=parcels.size();
		
		for(int i=0; i<size; i++) {
			
			Parcel parcel=parcels.get(i);
			
			if (!parcel.isOk())
				continue;
			
			ArrayList<TextRing> rings=parcel.getRings();
			
			OsmTags tags=new OsmTags();
			tags.put("area", "parcel");
			
			addRings(tags, rings);
			
			if (i%1000==0) {
				
				Log.info("addParcels() progress="+String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("addParcels() progress=100%");
	}
	
	void addPools(PoolList pools) {
		
		Log.info("addPools() starts...");
		
		//Iterator<Pool> poolIter=pools.iterator();
		
		//while(poolIter.hasNext()) {
		
		int size=pools.size();
		
		for(int i=0; i<size; i++) {
			
			//Pool pool=poolIter.next();
			Pool pool=pools.get(i);
			
			if (!pool.isOk())
				continue;
			
			ArrayList<TextRing> rings=pool.getRings();
			
			OsmTags tags=new OsmTags();
			tags.put("leisure", "swimming_pool");
			
			addRings(tags, rings);
			
			if (i%1000==0) {
				
				Log.info("addPools() progress="+String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("addPools() progress=100%");
	}
	
	void addZones(ZoneList zones) {
		
		Log.info("addZones() starts...");
		
		//Iterator<Zone> zoneIter=zones.iterator();
		
		//while(zoneIter.hasNext()) {
		
		int size=zones.size();
		
		for(int i=0; i<size; i++)  {
		
			//Zone zone=zoneIter.next();
			Zone zone=zones.get(i);
			
			if (!zone.isOk())
				continue;
			
			ArrayList<TextRing> rings=zone.getRings();
			
			OsmTags tags=new OsmTags();
			tags.put("area", "zone");
			
			addRings(tags, rings);
			
			if (i%1000==0) {
				
				Log.info("addZones() progress="+
						String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("addZones() progress=100%");
	}
	
	/*
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
	*/
	
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
		//assignIds();
		
		// Simplify geometries
		simplifyGeometries();
		
		int numWrittenNodes=0;
		int numWrittenWays=0;
		int numWrittenRelations=0;
		
		Iterator<OsmNode> nodeIter=mNodes.iterator();
		
		while(nodeIter.hasNext()) {
			
			OsmNode node=nodeIter.next();
			
			if (node==null) {
				
				// Erased node. Skip...
				continue;
			}
			
			if (!node.writeToFile(mOutputBuffer)) {
				
				break;
			}
			
			numWrittenNodes++;
		}
		
		Iterator<OsmWay> wayIter=mWays.iterator();
		
		while(wayIter.hasNext()) {
			
			OsmWay way=wayIter.next();
			
			if (!way.writeToFile(mOutputBuffer)) {
				
				break;
			}
			
			numWrittenWays++;
		}
		
		Iterator<OsmRelation> relationIter=mRelations.iterator();
		
		while(relationIter.hasNext()) {
			
			OsmRelation relation=relationIter.next();
			
			if (!relation.writeToFile(mOutputBuffer)) {
				
				break;
			}
			
			numWrittenRelations++;
		}
		
		Log.info("Written to disk "+numWrittenNodes+" nodes, "+numWrittenWays+
				" ways and "+numWrittenRelations+" relations");
		
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
	
	/*
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
	*/
	
	// Simplify geometries
	void simplifyGeometries() {
		
		Log.info("simplifyGeometries() starting...");
		
		int numSimplifiedNodes=0;
		
		int size=mNodes.size();
		
		for(int i=0; i<size; i++)  {
			
			boolean keepProcessingNode=true;
			
			OsmNode node=mNodes.get(i);
			
			if (node==null) {
				
				// Invalid node. Skipping...
				continue;
			}
			
			Iterator<OsmRelation> relationIter=mRelations.iterator();
			
			while(relationIter.hasNext()) {
				
				OsmRelation relation=relationIter.next();
				
				if (relation.containsNodeWithId(node.mId)) {
					
					// This relation contains the node. Stop processing
					keepProcessingNode=false;
					break;
				}
			}
			
			if (!keepProcessingNode) {
				continue;
			}
			
			int numWaysContainingNode=0;
			
			double angle=-1.0;
			
			Iterator<OsmWay> wayIter=mWays.iterator();
			
			while(wayIter.hasNext()) {
				
				OsmWay way=wayIter.next();
				
				if (way.containsNodeWithId(node.mId)) {
					
					numWaysContainingNode++;
					
					angle=Math.toDegrees(way.simplifyNode(node.mId));
					
					if (angle<0.0) {
						
						// This node cannot be simplified. Skipping...
						keepProcessingNode=false;
						break;
					}
					
					if (Math.abs(angle-180.0)>1.0) {
						
						// This node cannot be simplified. Skipping...
						keepProcessingNode=false;
						break;
					}
				}
			}
			
			if ((numWaysContainingNode>0) && (keepProcessingNode)) {
				
				// Node can be simplified. Mark it!!
				node.setTag("angle", String.format(Locale.US, "%.3f", angle));
				
				if (Math.abs(angle-180.0)<1.0) {
					
					//node.setTag("highway", "service");
					node.setTag("note", "FIXME");
					node.setTag("fixme", "Node shall be deleted");
					
					numSimplifiedNodes++;
				}
			}
			
			if (i%5000==0) {
				Log.info("simplifyGeometries() progress="+
						String.format(Locale.US, "%d%%", i*100/size));
			}
		}
		
		Log.info("simplifyGeometries(): Simplified "+numSimplifiedNodes+
				" node(s)");
	}
}
