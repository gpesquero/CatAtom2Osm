package osm.catatom2osm;

import java.util.ArrayList;
import java.util.Iterator;

public class AddressList extends ArrayList<Address> {

	private static final long serialVersionUID = 1L;
	
	int getUnassignedCount() {
		
		int count=0;
		
		Iterator<Address> iterator=this.iterator();
		
		while(iterator.hasNext()) {
			
			Address address=iterator.next();
			
			if (address.mAssigned==false) {
				
				count++;;
			}
		}
		
		return count;
	}
	
	Address findAddressWithId(String id) {
		
		/*
		Iterator<Address> iterator=this.iterator();
		
		while(iterator.hasNext()) {
			
			Address address=iterator.next();
			
			if ((address.mId!=null) && (address.mId.equalsIgnoreCase(id))) {
				
				return address;
			}
		}
		*/
		
		return null;
	}
}
