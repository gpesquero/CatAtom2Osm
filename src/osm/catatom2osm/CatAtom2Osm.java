package osm.catatom2osm;

import java.io.File;
import java.io.InputStream;

public class CatAtom2Osm {
	
	private int mZipCode;
	
	private File mDir=null;
	
	CatAtom2Osm(String path) {
		
		// Check if the destination directory exists and has a correct name
		if (!checkDir(path)) {
			
			// checkDir() failed!!. Exit program
			return;
		}
		
		// Open Address ZIP File
		CadastreZipFile adZipFile=new CadastreZipFile(mDir, "AD", mZipCode);
		
		if (!adZipFile.isOk()) {
			return;
		}
		
		InputStream addressGmlInputStream=adZipFile.getGmlFileStream(null);
				
		if (addressGmlInputStream==null) {
			return;
		}
		
		// Address parsing
		AddressParser addressParser=new AddressParser(addressGmlInputStream);
		addressParser.launchParser();
		AddressList addressList=addressParser.getAddressList();
		Log.info("Number of Addresses: "+addressList.size());
		
		addressGmlInputStream=adZipFile.getGmlFileStream(null);
				
		if (addressGmlInputStream==null) {
			return;
		}
		
		// Street parsing
		StreetParser streetParser=new StreetParser(addressGmlInputStream);
		streetParser.launchParser();
		StreetList streetList=streetParser.getStreets();
		Log.info("Number of Streets: "+streetList.size());
		
		addressGmlInputStream=adZipFile.getGmlFileStream(null);
		
		if (addressGmlInputStream==null) {
			return;
		}
		
		// PostalCodes parsing
		PostalCodeParser postalCodeParser=new PostalCodeParser(addressGmlInputStream);
		postalCodeParser.launchParser();
		PostalCodeList postalCodeList=postalCodeParser.getPostalCodes();
		Log.info("Number of PostalCodes: "+postalCodeList.size());
		
				addressGmlInputStream=adZipFile.getGmlFileStream(null);
				
		if (addressGmlInputStream==null) {
			return;
		}
		
		// AdminUnit parsing
		AdminUnitParser adminUnitParser=new AdminUnitParser(addressGmlInputStream);
		adminUnitParser.launchParser();
		AdminUnitList adminUnitList=adminUnitParser.getAdminUnits();
		Log.info("Number of AdminUnits: "+adminUnitList.size());
		
		// Open Building ZIP File
		CadastreZipFile buZipFile=new CadastreZipFile(mDir, "BU", mZipCode);
		
		if (!buZipFile.isOk()) {
			return;
		}
		
		// Building parsing
		
		InputStream buildingGmlInputStream=buZipFile.getGmlFileStream(
			"building");
			
		if (buildingGmlInputStream==null) {
			return;
		}
		
		BuildingParser buildingParser=new BuildingParser(
			buildingGmlInputStream);
		
		buildingParser.launchParser();
		
		BuildingList buildingList=buildingParser.getBuildings();
		
		Log.info("Number of Buildings: "+buildingList.size());
		
		/*
		// Combine buildings with addresses
		buildingList.combine(addressList);
		
		int unassignedAddresses=addressList.getUnassignedCount();
		
		if (unassignedAddresses>0) {
			
			Log.info("Unassigned addresses count: "+unassignedAddresses);
		}
		
		Log.info("Building count: "+buildingList.size());
		
		int unassignedBuildings=buildingList.getUnassignedCount();
		
		if (unassignedBuildings>0) {
			
			Log.info("Unassigned buildings count: "+unassignedBuildings);
		}
		*/
		
		// Open the Building Part GML file
		InputStream buildingPartGmlInputStream=buZipFile.getGmlFileStream(
			"buildingpart");
		
		if (buildingPartGmlInputStream==null) {
			return;
		}
		
		// BuildingPart parsing
		
		BuildingPartParser buildingPartParser=new BuildingPartParser(
			buildingPartGmlInputStream);
		buildingPartParser.launchParser();
		
		BuildingPartList buildingPartList=buildingPartParser.getBuildingParts();
		
		Log.info("Number of BuildingParts: "+buildingPartList.size());
		
		CadastreZipFile cpZipFile=new CadastreZipFile(mDir, "CP", mZipCode);
		
		if (!cpZipFile.isOk()) {
			return;
		}
		
		
		InputStream parcelGmlInputStream=cpZipFile.getGmlFileStream("cadastralparcel");
		
		if (parcelGmlInputStream==null) {
			return;
		}
		
		ParcelParser parcelParser=new ParcelParser(parcelGmlInputStream);
		parcelParser.launchParser();
		
		ParcelList parcelList=parcelParser.getParcels();
		Log.info("Number of Parcels: "+parcelList.size());
		
		
		InputStream zoneGmlInputStream=cpZipFile.getGmlFileStream("cadastralzoning");
			
		if (zoneGmlInputStream==null) {
			return;
		}
		
		// Zone parsing
		ZoneParser zoneParser=new ZoneParser(zoneGmlInputStream);
		zoneParser.launchParser();
		
		ZoneList zoneList=zoneParser.getZones();
		ZoneList blockList=zoneParser.getSpecificZones("MANZANA");
		ZoneList polygonList=zoneParser.getSpecificZones("POLIGONO");
		
		Log.info("Number of Zones: "+zoneList.size());
		Log.info("Number of Zones (BLOCKS): "+blockList.size());
		Log.info("Number of Zones (POLYGONS): "+polygonList.size());
		
		
		// Creation of the resulting OSM files
		
		OsmFile fullOsmFile=new OsmFile(mDir, "result_full.osm");
		fullOsmFile.addAddresses(addressList);
		fullOsmFile.addBuildings(buildingList);
		fullOsmFile.addBuildingParts(buildingPartList);
		fullOsmFile.addZones(zoneList);
		fullOsmFile.writeDataToFile();
		fullOsmFile.close();
		
		OsmFile addressOsmFile=new OsmFile(mDir, "address.osm");
		addressOsmFile.addAddresses(addressList);
		addressOsmFile.writeDataToFile();
		addressOsmFile.close();
		
		OsmFile buildingOsmFile=new OsmFile(mDir, "building.osm");
		buildingOsmFile.addBuildings(buildingList);
		buildingOsmFile.writeDataToFile();
		buildingOsmFile.close();
		
		OsmFile buildingPartOsmFile=new OsmFile(mDir, "building_parts.osm");
		buildingPartOsmFile.addBuildingParts(buildingPartList);
		buildingPartOsmFile.writeDataToFile();
		buildingPartOsmFile.close();
		
		OsmFile zoneOsmFile=new OsmFile(mDir, "zones.osm");
		zoneOsmFile.addZones(zoneList);
		zoneOsmFile.writeDataToFile();
		zoneOsmFile.close();
		
		OsmFile manzanasOsmFile=new OsmFile(mDir, "zones_blocks.osm");
		manzanasOsmFile.addZones(blockList);
		manzanasOsmFile.writeDataToFile();
		manzanasOsmFile.close();
		
		OsmFile poligonosOsmFile=new OsmFile(mDir, "zones_polygons.osm");
		poligonosOsmFile.addZones(polygonList);
		poligonosOsmFile.writeDataToFile();
		poligonosOsmFile.close();
		
		
		// Creation of the GeoJSON file to store the zones for the Task Manager
		
		GeoJsonFile geoJsonFile=new GeoJsonFile(mDir, "zones.geojson");
		geoJsonFile.writeZones(zoneList);
		geoJsonFile.close();
		
		GeoJsonFile manzanasGeoJsonFile=new GeoJsonFile(mDir, "zones_blocks.geojson");
		manzanasGeoJsonFile.writeZones(blockList);
		manzanasGeoJsonFile.close();
		
		GeoJsonFile poliginosGeoJsonFile=new GeoJsonFile(mDir, "zones_polygons.geojson");
		poliginosGeoJsonFile.writeZones(polygonList);
		poliginosGeoJsonFile.close();
		
		Log.info("Statistics summary:");
		Log.info("Number of Addresses: "+addressList.size());
		Log.info("Number of Streets: "+streetList.size());
		Log.info("Number of PostalCodes: "+postalCodeList.size());
		Log.info("Number of AdminUnits: "+adminUnitList.size());
		Log.info("Number of Buildings: "+buildingList.size());
		Log.info("Number of BuildingParts: "+buildingPartList.size());
		Log.info("Number of Parcels: "+parcelList.size());
		Log.info("Number of Zones: "+zoneList.size());
		Log.info("Number of Zones (BLOCKS): "+blockList.size());
		Log.info("Number of Zones (POLYGONS): "+polygonList.size());
	}
	
	boolean checkDir(String path) {
		
		mDir=new File(path);
		
		if (!mDir.exists()) {			
			
			Log.error("Dir <"+path+"> does not exist!!");
			return false;
		}
		
		if (!mDir.isDirectory()) {
			
			Log.error("Dir <"+path+"> is not a directory!!");
			return false;
		}
		
		String dirName=mDir.getName();
		
		if (dirName.length()<5) {
			
			Log.error("Dir name <"+dirName+"> is too short!! It must begin with a 5 digits ZIP Code");
			return false;
		}
		
		try {
			mZipCode=Integer.parseInt(dirName.substring(0, 5));
		}
		catch(NumberFormatException e) {
			
			Log.error("Failed to parse ZIP Code! Dir name must begin with a 5 digits ZIP Code!!");
			return false;
		}
		
		Log.info("Dir <"+path+"> OK!");
		Log.info("ZIP Code: "+mZipCode);	
				
		return true;		
	}
}
