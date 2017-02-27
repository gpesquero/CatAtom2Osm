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
	
	void assignAdminUnits(AdminUnitList adminUnitList) {
		
		Iterator<Address> addrIter=this.iterator();
		
		while(addrIter.hasNext()) {
			
			Address address=addrIter.next();
			
			address.mAdminUnit=null;
			
			String id=address.mAdminUnitId;
			
			Iterator<AdminUnit> adminUnitIter=adminUnitList.iterator();
			
			while(adminUnitIter.hasNext()) {
				
				AdminUnit adminUnit=adminUnitIter.next();
				
				if (adminUnit.getId().equals(id)) {
					
					address.mAdminUnit=adminUnit.mName;
					
					break;
				}
			}
			
			if (address.mAdminUnit==null) {
				
				Log.warning("Address with id <"+address.getId()+
						"> has no matching AdminUnit!!");
			}
		}
	}
	
	void assignPostalCodes(PostalCodeList postalCodeList) {
		
		Iterator<Address> addrIter=this.iterator();
		
		while(addrIter.hasNext()) {
			
			Address address=addrIter.next();
			
			address.mPostalCode=null;
			
			String id=address.mPostalCodeId;
			
			Iterator<PostalCode> postalCodeIter=postalCodeList.iterator();
			
			while(postalCodeIter.hasNext()) {
				
				PostalCode postalCode=postalCodeIter.next();
				
				if (postalCode.getId().equals(id)) {
					
					address.mPostalCode=postalCode.mCode;
					
					break;
				}
			}
			
			if (address.mPostalCode==null) {
				
				Log.warning("Address with id <"+address.getId()+
						"> has no matching PostalCode!!");
			}
		}
	}

	void assignStreets(StreetList streetList) {
		
		Iterator<Address> addrIter=this.iterator();
		
		while(addrIter.hasNext()) {
			
			Address address=addrIter.next();
			
			address.mStreet=null;
			
			String id=address.mStreetId;
			
			Iterator<Street> streetIter=streetList.iterator();
			
			while(streetIter.hasNext()) {
				
				Street street=streetIter.next();
				
				if (street.getId().equals(id)) {
					
					address.mStreet=street.mName;
					
					break;
				}
			}
			
			if (address.mStreet==null) {
				
				Log.warning("Address with id <"+address.getId()+
						"> has no matching Street!!");
			}
		}
	}
}
