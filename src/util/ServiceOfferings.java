package util;

import java.util.HashSet;

public class ServiceOfferings {
	HashSet<Integer> serviceOfferingActiveFlags = new HashSet<Integer>();

	public ServiceOfferings() {

	}

	public void SetActiveFlag(Integer serviceOfferingID, boolean flag) {
		// serviceOfferingID > 0, activeFlag = 1
		// serviceOfferingID < 0, activeFlag = 0

		int serviceOfferingActiveFlag = flag ? serviceOfferingID : -serviceOfferingID;

		// precautions from conflicting : remove old value if it is present
		getServiceOfferingActiveFlags().remove(-serviceOfferingID);

		// keep the last update
		add(serviceOfferingActiveFlag);
	}

	private void add(int serviceOfferingActiveFlag) {
		getServiceOfferingActiveFlags().add(serviceOfferingActiveFlag);
	}

	public boolean isActiveFlag(Integer serviceOfferingID) {
		if(serviceOfferingActiveFlags.contains(serviceOfferingID)) return true;
		else if(serviceOfferingActiveFlags.contains(-serviceOfferingID)) return false;
		else return false;
	}

	public HashSet<Integer> getServiceOfferingActiveFlags() {
		return serviceOfferingActiveFlags;
	}

	public void setServiceOfferingActiveFlags(HashSet<Integer> serviceOfferingActiveFlags) {
		this.serviceOfferingActiveFlags = serviceOfferingActiveFlags;
	}
}
