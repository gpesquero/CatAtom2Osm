package osm.catatom2osm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CadastreZipFile {
	
	protected final static String BASE_FILE_NAME="A.ES.SDGC.";
	
	ZipFile mZipFile=null;
	
	String mBaseFileName;
	
	public CadastreZipFile(File dir, String dataGroup, int zipCode) {
		
		mBaseFileName=BASE_FILE_NAME+dataGroup+"."+zipCode;
		
		String zipFileName=mBaseFileName+".zip";
		
		File file=new File(dir, zipFileName);
	
		if (!file.exists()) {
	
			Log.error("ZIP file <"+zipFileName+"> does not exist");
			return;
		}
	
		if (!file.isFile()) {
		
			Log.error("ZIP file <"+zipFileName+"> is not a file");
			return;
		}
	
		try {
			mZipFile=new ZipFile(file);
		
		} catch (IOException e) {
		
			Log.error("ZIP File <"+zipFileName+"> open error: "+
					e.getMessage());
			
			mZipFile=null;
			return;
		}
		
		Log.info("ZIP file <"+zipFileName+"> opened Ok!");
		
		return;
	}
	
	boolean isOk() {
		
		return (mZipFile!=null);
	}
	
	public InputStream getGmlFileStream(String gmlString) {
		
		String gmlFileName;
		
		if (gmlString==null) {
			
			gmlFileName=mBaseFileName+".gml";
		}
		else {
			gmlFileName=mBaseFileName+"."+gmlString+".gml";
		}
		
		ZipEntry zipEntry=mZipFile.getEntry(gmlFileName);
		
		if (zipEntry==null) {
			
			Log.error("Entry <"+gmlFileName+"> not found in ZIP file!");
			return null;
		}
		
		InputStream inputStream=null;
		
		try {
			inputStream=mZipFile.getInputStream(zipEntry);
			
		} catch (IOException e) {
			
			Log.error("Entry <"+gmlFileName+"> getInputStream error: "+
					e.getMessage());
			
			return null;
		}
		
		Log.info("ZIP File Entry <"+gmlFileName+"> opened Ok!");
		
		return inputStream;
	}
}
