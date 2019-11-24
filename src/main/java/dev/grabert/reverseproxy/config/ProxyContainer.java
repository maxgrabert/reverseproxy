package dev.grabert.reverseproxy.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Autowired
    private ProxyProperties reverseProxyConfig;
    
    public void customize(ConfigurableServletWebServerFactory factory) {
    	InetAddress address = null;
    	if (reverseProxyConfig != null  &&  reverseProxyConfig.getListen() != null  &&  reverseProxyConfig.getListen().getAddress() != null) {
    		try {
        		address = InetAddress.getByName(reverseProxyConfig.getListen().getAddress());
    		} catch (UnknownHostException e) {
				e.printStackTrace();
				address = null; 
				System.err.println("could not parse interface address!");
			}
    	}
    	if (address != null) {
        	factory.setAddress(address);
    	}

     	
    	int port = 8080;
    	if (reverseProxyConfig != null  &&  reverseProxyConfig.getListen() != null  &&  reverseProxyConfig.getListen().getPort() != null) {
    		port = reverseProxyConfig.getListen().getPort().intValue();
    	} else {
			System.err.println("could not parse interface port, using default port 8080");
    	}
    	factory.setPort(port);
    	
    	factory.setContextPath("");
	}
}
