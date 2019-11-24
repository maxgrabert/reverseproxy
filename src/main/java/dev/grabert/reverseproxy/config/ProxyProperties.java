package dev.grabert.reverseproxy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import dev.grabert.reverseproxy.config.model.*;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("proxy")
public class ProxyProperties {
	
	private Listener listen;
	
	List<Service> services = new ArrayList<>();

	public Listener getListen() {
		return listen;
	}

	public void setListen(Listener listen) {
		this.listen = listen;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return "ProxyProperties [listen=" + listen + ", services=" + services + "]";
	}
	
	
}
