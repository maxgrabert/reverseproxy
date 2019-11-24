package dev.grabert.reverseproxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import dev.grabert.reverseproxy.config.ProxyProperties;
import dev.grabert.reverseproxy.forwarding.LoadBalancer;

@SpringBootTest
class ReverseProxyApplicationTests {

    @Autowired
    private ProxyProperties reverseProxyProperties;
    
    @Autowired
	LoadBalancer loadBalancer;
    
	@Test
	void testYamlParsing() {
		Assert.notNull(reverseProxyProperties, "The configuration application.yml file should have been parsed");

		Assert.notEmpty(reverseProxyProperties.getServices(), "Could not identify/create services from info in application.yml");

		Assert.notNull(reverseProxyProperties.getListen(), "Could not identify the interface to bind to from info in application.yml");
	}

}
