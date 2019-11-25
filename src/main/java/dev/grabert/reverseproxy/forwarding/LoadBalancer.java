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

// TODO: Auto-generated Javadoc
/**
 * This class implements some basic load balancing strategies used by {@link ProxyHTTPController}.
 *
 * @author Markus Grabert <markus@grabert.dev>
 */
@Component
public class LoadBalancer {
	
	/**
	 * The Enum SchedulingType.
	 */
	public  enum SchedulingType {
		RANDOM, 
		ROUND_ROBIN
	}
	
	private SchedulingType type = SchedulingType.ROUND_ROBIN;
	
	/** A HashMap&lt;domainName, service> */
	private HashMap<String, Service> domainServiceMap = new HashMap<>();
	
	/** A HashMap&lt;domainName, currentHost> */
	private HashMap<String, Integer> domainCurrentHostMap = new HashMap<>();
	
	/** A HashMap&lt;domainName, HostIterator> */
	private HashMap<String, Iterator<Host>>  domainHostIterator = new HashMap<>();
	
	
	/**
	 * Gets the service for domain.
	 *
	 * @param domainName the domain name
	 * @return the service for domain
	 */
	public Service getServiceForDomain(final String domainName) {
		return this.domainServiceMap.get(domainName);
	}
	
	/**
	 * Gets the domains.
	 *
	 * @return the domains
	 */
	public Set<String> getDomains() {
		return domainServiceMap.keySet();
	}
	
	/**
	 * Gets the current host for domain.
	 *
	 * @param domainName the domain name
	 * @return the current host for domain
	 */
	public Host getCurrentHostForDomain(final String domainName) {
		if (this.domainCurrentHostMap.containsKey(domainName)) {
			int currentHostIndex = this.domainCurrentHostMap.get(domainName).intValue();
			return this.domainServiceMap.get(domainName).getHosts().get(currentHostIndex);
		}
		return null;
	}
	
	/**
	 * Gets the domain current host map.
	 *
	 * @param domainCurrentHostMap the domain current host map
	 * @return the domain current host map
	 */
	public void getdomainCurrentHostMap(HashMap<String, Integer> domainCurrentHostMap) {
		this.domainCurrentHostMap = domainCurrentHostMap;
	}

	/**
	 * Gets the scheduling type.
	 *
	 * @return the {@link SchedulingType}
	 */
	public SchedulingType getType() {
		return type;
	}

	/**
	 * Sets the {@link SchedulingType} to be used.
	 *
	 * @param type the new type
	 */
	public void setType(SchedulingType type) {
		this.type = type;
	}

	/**
	 * Adds the service to the load balancer configuration
	 *
	 * @param service the service
	 */
	public void addService(Service service) {
		if (service != null && StringUtils.isNotBlank(service.getName()) && !CollectionUtils.isEmpty(service.getHosts())
				&& StringUtils.isNotBlank(service.getDomain())) {
			this.domainServiceMap.put(service.getDomain(), service);
			this.domainCurrentHostMap.put(service.getDomain(), 0);
		}
	}
	
	/**
	 * Removes the service.
	 *
	 * @param service the service
	 */
	public void removeService(Service service) {
		if (service != null && StringUtils.isNotBlank(service.getDomain())) {
			this.domainServiceMap.remove(service.getDomain());
			this.domainCurrentHostMap.remove(service.getDomain());
		}
	}

	/**
	 * Gets the next {@link Host} in line for the domain name specified in the Host header of the request.
	 *
	 * @param domainName the domain name
	 * @return the next host for domain
	 */
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
