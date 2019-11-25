/*
 * 
 */
package dev.grabert.reverseproxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import dev.grabert.reverseproxy.config.ProxyProperties;
import dev.grabert.reverseproxy.forwarding.LoadBalancer;

// TODO: Auto-generated Javadoc
/**
 * The Class ReverseProxyApplicationTests.
 */
@SpringBootTest
class ReverseProxyApplicationTests {

    /** The reverse proxy properties. */
    @Autowired
    private ProxyProperties reverseProxyProperties;
    
    /** The load balancer. */
    @Autowired
	LoadBalancer loadBalancer;
    
	/**
	 * Test yaml parsing.
	 */
	@Test
	void testYamlParsing() {
		Assert.notNull(reverseProxyProperties, "The configuration application.yml file should have been parsed");
	}
	
	/**
	 * Test yaml services parsing.
	 */
	@Test
	void testYamlServicesParsing() {
		Assert.notEmpty(reverseProxyProperties.getServices(), "Could not identify/create services from info in application.yml");
	}
	
	/**
	 * Test yaml listen parsing.
	 */
	@Test
	void testYamlListenParsing() {
		Assert.notNull(reverseProxyProperties.getListen(), "Could not identify the interface to bind to from info in application.yml");
	}

}
