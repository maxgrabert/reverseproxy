package dev.grabert.reverseproxy.config.model;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * This is a simple bean used as Spring Boot Configuration class and is automatically instantiated
 * during application start via {@link ProxyProperties}.
 *
 * @author Markus Grabert <markus@grabert.dev>
 * @see ProxyProperties
 */
public class Service {
	
	/** The name. */
	private String name;
	
	/** The domain. */
	private String domain;
	
	/** The hosts. */
	List<Host> hosts = new ArrayList<>();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the hosts.
	 *
	 * @return the hosts
	 */
	public List<Host> getHosts() {
		return hosts;
	}

	/**
	 * Sets the hosts.
	 *
	 * @param hosts the new hosts
	 */
	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Service [name=" + name + ", domain=" + domain + ", hosts=" + hosts + "]";
	}
	
}
