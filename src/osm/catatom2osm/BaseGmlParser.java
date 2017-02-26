package osm.catatom2osm;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class BaseGmlParser extends DefaultHandler {
	
	private InputStream mInputStream;
	
	private String mElementTag=null;
	
	private String mCurrentData=null;
	
	protected BaseElement mElement=null;
	
	private int mRingType=TextRing.RING_TYPE_NULL;
	
	protected BaseGmlParser(InputStream inputStream, String elementTag) {
		
		mInputStream=inputStream;
		
		mElementTag=elementTag;
	}
	
	boolean launchParser() {
		
		if (mInputStream==null) {
			Log.error("launchParser mInputStream==null");
			return false;
		}
	
		SAXParserFactory factory=SAXParserFactory.newInstance();
		
		SAXParser saxParser;
		
		try {
			saxParser=factory.newSAXParser();
		
		} catch (ParserConfigurationException | SAXException e) {
		
			Log.error("SAXParserFactory.newSAXParser() error: "+
					e.getMessage());
			
			return false;
		}
		
		try {
			saxParser.parse(mInputStream, this);
		
		} catch (SAXException | IOException e) {
		
			Log.error("SAXParser.parse() error: "+e.getMessage());
			return false;
		}
		
		return true;
	}
	
	@Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException {
		
		if (qName.equalsIgnoreCase(mElementTag)) {
			
			// There's a new element. Call onElementStart() to create
			// a new element...
			
			String id=attributes.getValue("gml:id");
			
			if (id==null) {
				
				Log.warning("gml:id is null in tag <"+qName+">");
				return;
			}
			
			onElementStart(id);
		}
		else if (mElement==null) {
			
			// There is no element, so we do not process any tag start
		}
		else if (qName.equalsIgnoreCase("gml:Envelope")) {
			
			String value=attributes.getValue("srsName");
			
			if (value==null) {
				
				Log.warning("srsName attribute is null in tag <"+qName+">");
				return;
			}
			else {
				
				TextRing envelope=new TextRing();
				
				envelope.setEPSG(formatEpsg(value));
				
				mElement.setEnvelope(envelope);
			}
		}
		else if ((qName.equalsIgnoreCase("gml:Point")) ||
				(qName.equalsIgnoreCase("gml:Surface"))) {
			
			// We've detected a Point or Surface tag. We have to get the EPSG
			// from the srsName attribute
			
			String value=attributes.getValue("srsName");
			
			if (value==null) {
				
				Log.warning("srsName attribute is null in tag <"+qName+">");
				return;
			}
			else {
				
				mElement.setEPSG(formatEpsg(value));
			}
		}
		else if (qName.equalsIgnoreCase("gml:exterior")) {
			
			// Starts an exterior surface
			
			mRingType=TextRing.RING_TYPE_EXTERIOR;
		}
		else if (qName.equalsIgnoreCase("gml:interior")) {
			
			// Starts an interior surface
			
			mRingType=TextRing.RING_TYPE_INTERIOR;
		}
		else {
			
			onTag(qName, attributes);
			
			//mCurrentTag=qName;
			mCurrentData=null;
		}
	}
	
	@Override
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		if (qName.equalsIgnoreCase(mElementTag)) {
			
			// Detected element end. Call onElementEnd() to check it and 
			// store it
			
			onElementEnd();
			
			mElement=null;
		}
		else if (mElement==null) {
			
			// There is no element, so we do not process any tag end
		}
		else if (qName.equalsIgnoreCase("gml:exterior")) {
			
			// Ends an exterior surface
			
			mRingType=TextRing.RING_TYPE_NULL;
		}
		else if (qName.equalsIgnoreCase("gml:interior")) {
			
			// Ends an interior surface
			
			mRingType=TextRing.RING_TYPE_NULL;
		}
		else if ((qName.equalsIgnoreCase("gml:lowerCorner"))  ||
				(qName.equalsIgnoreCase("gml:upperCorner"))) {
			
			TextRing envelope=mElement.getEnvelope();
			
			if (envelope==null) {
				
				Log.warning("mElement.mEnvelope==null with tag <"+qName+">");
			}
			else {
				if (envelope.mSourceData==null) {
					envelope.mSourceData=mCurrentData;
				}
				else {
					envelope.mSourceData+=" "+mCurrentData;
				}
			}
		}
		else if (qName.equalsIgnoreCase("gml:pos")) {
			
			mElement.setPos(new TextCoord(mCurrentData));
		}
		else if ((qName.equalsIgnoreCase("gml:posList"))) {
			
			switch(mRingType) {
			case TextRing.RING_TYPE_EXTERIOR:
			case TextRing.RING_TYPE_INTERIOR:
				
				// Add ring to element
				mElement.addRing(new TextRing(mRingType, mCurrentData));
				break;
				
			default:
				
				Log.warning("Unknown ring type in tag <"+qName+">");
				break;
			}
		}
		else if (mCurrentData!=null) {
			
			// Detected data of an unparsed tag. Call the overloaded function
			
			onData(qName, mCurrentData);
		}
		
		mCurrentData=null;
	}
	
	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		
		if (mElement==null) {
			
			// There is no element, so we do not process any character
		}
		else {
			
			if (mCurrentData==null) {
				
				mCurrentData=new String(ch, start, length);
			}
			else {
				
				mCurrentData+=new String(ch, start, length);
			}
		}
	}
	
	private String formatEpsg(String srcEPSG) {
	
		String epsgString=new String("EPSG::"); 
		
		int epsgPos=srcEPSG.lastIndexOf(epsgString);
		
		if (epsgPos<0) {
		
			Log.warning("EPSG Pos of Address <0");
			return null;
		}
		
		return srcEPSG.substring(epsgPos).replace("::", ":");
	}
	
	abstract protected void onElementStart(String id);
	abstract protected void onElementEnd();
	abstract protected void onTag(String tagName, Attributes attributes);
	abstract protected void onData(String tagName, String data);
}
