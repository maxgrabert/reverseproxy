package dev.grabert.reverseproxy.config.model;

import java.util.ArrayList;
import java.util.List;

public class Service {
	private String name;
	
	private String domain;
	
	List<Host> hosts = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<Host> getHosts() {
		return hosts;
	}

	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", domain=" + domain + ", hosts=" + hosts + "]";
	}
	
}
