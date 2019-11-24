package dev.grabert.reverseproxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import dev.grabert.reverseproxy.config.ProxyProperties;
import dev.grabert.reverseproxy.forwarding.LoadBalancer;

@SpringBootApplication
public class ReverseProxyApplication implements CommandLineRunner {

    @Autowired
    private ProxyProperties reverseProxyProperties;
    
    @Autowired
	LoadBalancer loadBalancer;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReverseProxyApplication.class);
	    app.setBannerMode(Banner.Mode.OFF);
	    app.run(args);
	}

    @Override
    public void run(String... args)  {
    	System.out.println("using properties: " + reverseProxyProperties);
    	
    	if (!CollectionUtils.isEmpty(reverseProxyProperties.getServices())) {
    		reverseProxyProperties.getServices().forEach(loadBalancer::addService);
    	}
    }
}
