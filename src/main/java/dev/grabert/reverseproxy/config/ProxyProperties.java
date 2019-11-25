package dev.grabert.reverseproxy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import dev.grabert.reverseproxy.config.model.*;

// TODO: Auto-generated Javadoc
/**
 * This class is a Spring Boot Configuration class and is automatically instantiated
 * during application start. It (and all classes referenced with in it) 
 * will be initialized with the values defined in src/main/resources/application.yml.
 * 
 * @author Markus Grabert <markus@grabert.dev> <markus@grabert.dev>
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties("proxy")
public class ProxyProperties {
	
	/** The listen. */
	private Listen listen;
	
	/** The services. */
	List<Service> services = new ArrayList<>();

	/**
	 * Gets the listen.
	 *
	 * @return the listen
	 */
	public Listen getListen() {
		return listen;
	}

	/**
	 * Sets the listen.
	 *
	 * @param listen the new listen
	 */
	public void setListen(Listen listen) {
		this.listen = listen;
	}

	/**
	 * Gets the services.
	 *
	 * @return the services
	 */
	public List<Service> getServices() {
		return services;
	}

	/**
	 * Sets the services.
	 *
	 * @param services the new services
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ProxyProperties [listen=" + listen + ", services=" + services + "]";
	}
	
}
