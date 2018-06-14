package util;

import java.util.HashSet;

public class PamInformationList {

	private HashSet<PamInformation> list;

	public PamInformationList() {
		list = new HashSet<PamInformation>();
	}
	
	public PamInformationList(HashSet<PamInformation> list) {
		this.list = list;
	}
	
	public void add(PamInformation pamInformation) {
		if(list != null) list.add(pamInformation);
	}

	public HashSet<PamInformation> getList() {
		return list;
	}

	public void setList(HashSet<PamInformation> list) {
		this.list = list;
	}

}
