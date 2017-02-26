# CatAtom2Osm README.md file

Conversion Tool of the data sets of the INSPIRE Services of the Spanish
Cadastre (http://www.catastro.minhap.gob.es/webinspire/index.html) to
OSM files.

Build environment:

  * Ubuntu 16.04
  * Eclipse Platform 3.8.1
  * GeoTools 16.1
  * JDK 1.8


In order to build the application, the following GeoTools JAR files shall be
included in the "Java Build Path > Add External Jars" window of the "Project
Properties" in Eclipse:

  * gt-api-16.1.jar
  * gt-epsg-hsql-16.1.jar


The application shall be launched as "CatAtom2Osm <path>", where the argument
path states the directory where the source files are located. The directory
name shall start with 5 digits (GGMMM) matching the Cadastral Provincial
Office and Municipality Code.

The directory shall contain three ZIP data files downloaded from the INSPIRE
Services of the Spanish Cadastre:

  * A.ES.SDGC.CP.GGMMM.zip (Cadastral Parcels) 
  * A.ES.SDGC.BU.GGMMM.zip (Buildings)
  * A.ES.SDGC.AD.GGMMM.zip (Addresses)

CatAtom2Osm class description:

  * Main.java
    Contains the main() function. Performs argument parsing and creates the
    main CatAtom2Osm object.

  * EpsgTransformer.java
    Class used to perform the conversion of the geographical coordinates.

  * Log.java
    Class used to perform logging of debug, warning, error messages to stdout.

  * CatAtom2Osm.java
    Main application class that creates and launches all the objects.

  * CadastreZipFile.java
    Class derived from the ZipFile class to handle the source Cadastre Zip
    files.

  * BaseGmlParser.java
    Abstract class that implements the DefaultHandler interface used to parse
    XML files using the standard SAXParser class.

  * AddressParser.java
  * AdminUnitParser.java
  * BuildingParser.java
  * BuildingPartParser.java
  * ParcelParser.java
  * PostalCodeParser.java
  * StreetParser.java
  * ZoneParser.java
    Classes derived from the BaseGmlParser class that handles the XML files
    parsing for each type of element of the data sets.

  * BaseElement.java
    Abstract class base of the different elements (Address, Building, etc.)

  * Address.java
  * AdminUnit.java
  * Building.java
  * BuildingPart.java
  * Parcel.java
  * PostalCode.java
  * Street.java
  * Zone.java
    Classes derived from the BaseElement for each type of element of the data
    sets.

  * AddressList.java
  * AdminUnitList.java
  * BuildingList.java
  * BuildingPartList.java
  * ParcelList.java
  * PostalCodeList.java
  * StreetList.java
  * ZoneList.java
    Classes derived from ArrayList<> for each type of element of the data
    sets to handle operation of the elements parsed.

  * TextCoord.java
    Class used to store a geographical coordinate (lon,lat) in text format.

  * TextRing.java
    Class used to store a geographica ring (array of TextCoords) in text
    format.

  * OsmFile.java
    Class used to create (write to disk) OSM files

  * GeoJsonFile.java
    Class used to create (write to disk) GeoJSON files

  * OsmItem.java
    Abstract class base for all the OSM elements (nodes, ways and relations). 

  * OsmTags.java
    Class used to store and handle the tags for the different OSM elements
    (nodes, ways and relations). 

  * OsmNode.java
  * OsmWay.java
  * OsmRelation.java
    Classes derived fro OsmItem used to store and handle information of OSM
    elements (nodes, ways and relations).



