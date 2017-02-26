package osm.catatom2osm;

import java.util.ArrayList;
import java.util.Iterator;

public class BuildingList extends ArrayList<Building> {

	private static final long serialVersionUID = 1L;
	
	int getUnassignedCount() {
		
		int count=0;
		
		Iterator<Building> iterator=this.iterator();
		
		while(iterator.hasNext()) {
			
			Building building=iterator.next();
			
			if (building.mAssigned==false) {
				
				count++;;
			}
		}
		
		return count;
	}
	
	boolean combine(AddressList addresses) {
		
		Iterator<Building> iterator=this.iterator();
		
		while(iterator.hasNext()) {
			
			/*
			Building building=iterator.next();
			
			if (building.mId==null) {
				
				Log.warning("Building with null id");
				continue;
			}
			
			Address address=addresses.findAddressWithId(building.mId);
			
			if (address==null) {
				
				//Log.warning("Not found address matching building id <"+building.mId+">");
			}
			else {
				
				if (address.mAssigned) {
					
					Log.warning("Address is already assigned");
				}
				else {
					
					address.mAssigned=true;
				}
				
				if (building.mAssigned) {
					
					Log.warning("Building is already assigned");
				}
				else {
					building.mAssigned=true;
				}
			}
			*/
		}
		
		return true;
	}
}
