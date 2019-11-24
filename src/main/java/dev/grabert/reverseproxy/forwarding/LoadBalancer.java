package dev.grabert.reverseproxy.forwarding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import dev.grabert.reverseproxy.config.model.Host;
import dev.grabert.reverseproxy.config.model.Service;
import io.micrometer.core.instrument.util.StringUtils;

@Component
public class LoadBalancer {
	public  enum SchedulingType {
		RANDOM, ROUND_ROBIN
	}
	
	private SchedulingType type = SchedulingType.ROUND_ROBIN;
	
	private HashMap<String, Service> domainServiceMap = new HashMap<>();
	private HashMap<String, Integer> domainCurrentHostMap = new HashMap<>();
	
	private HashMap<String, Iterator<Host>>  domainHostIterator = new HashMap<>();
	
	
	public Service getServiceForDomain(final String domainName) {
		return this.domainServiceMap.get(domainName);
	}
	
	public Set<String> getDomains() {
		return domainServiceMap.keySet();
	}
	
	public Host getCurrentHostForDomain(final String domainName) {
		if (this.domainCurrentHostMap.containsKey(domainName)) {
			int currentHostIndex = this.domainCurrentHostMap.get(domainName).intValue();
			return this.domainServiceMap.get(domainName).getHosts().get(currentHostIndex);
		}
		return null;
	}
	
	public void getdomainCurrentHostMap(HashMap<String, Integer> domainCurrentHostMap) {
		this.domainCurrentHostMap = domainCurrentHostMap;
	}

	public SchedulingType getType() {
		return type;
	}

	public void setType(SchedulingType type) {
		this.type = type;
	}

	public void addService(Service service) {
		if (service != null && StringUtils.isNotBlank(service.getName()) && !CollectionUtils.isEmpty(service.getHosts())
				&& StringUtils.isNotBlank(service.getDomain())) {
			this.domainServiceMap.put(service.getDomain(), service);
			this.domainCurrentHostMap.put(service.getDomain(), 0);
		}
	}
	
	public void removeService(Service service) {
		if (service != null && StringUtils.isNotBlank(service.getDomain())) {
			this.domainServiceMap.remove(service.getDomain());
			this.domainCurrentHostMap.remove(service.getDomain());
		}
	}

	public Host getNextHostForDomain(String domainName) {
		Host host = null;
		
		switch (this.type) {
		case ROUND_ROBIN:
			if (this.domainServiceMap.containsKey(domainName)) {
				Iterator<Host> hostIterator = domainHostIterator.get(domainName);
				if (hostIterator == null || !hostIterator.hasNext()) {
					hostIterator = this.domainServiceMap.get(domainName).getHosts().iterator();
					this.domainHostIterator.put(domainName, hostIterator);
				}
				host = hostIterator.next();
			}
			break;
			
		case RANDOM:
			if (this.domainServiceMap.containsKey(domainName)) {
				Iterator<Host> hostIterator = domainHostIterator.get(domainName);
				if (hostIterator == null || !hostIterator.hasNext()) {
					Collections.shuffle(this.domainServiceMap.get(domainName).getHosts());
					hostIterator = this.domainServiceMap.get(domainName).getHosts().iterator();
					this.domainHostIterator.put(domainName, hostIterator);
				}
				host = hostIterator.next();
			}
			break;			
			
		default:
			break;
		}
		return host;
	}
}
