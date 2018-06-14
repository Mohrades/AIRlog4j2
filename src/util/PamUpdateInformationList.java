package util;

import java.util.HashSet;

public class PamUpdateInformationList {

	private HashSet<PamUpdateInformation> list;

	public PamUpdateInformationList() {
		list = new HashSet<PamUpdateInformation>();
	}
	
	public PamUpdateInformationList(HashSet<PamUpdateInformation> list) {
		this.list = list;
	}
	
	public void add(PamUpdateInformation pamUpdateInformation) {
		if(list != null) list.add(pamUpdateInformation);
	}

	public HashSet<PamUpdateInformation> getList() {
		return list;
	}

	public void setList(HashSet<PamUpdateInformation> list) {
		this.list = list;
	}
}
