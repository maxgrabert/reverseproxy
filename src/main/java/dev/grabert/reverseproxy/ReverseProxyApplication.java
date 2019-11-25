
package dev.grabert.reverseproxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import dev.grabert.reverseproxy.config.ProxyProperties;
import dev.grabert.reverseproxy.forwarding.LoadBalancer;


// TODO: Auto-generated Javadoc
/**
 * The Class ReverseProxyApplication.
 *
 * @author Markus Grabert <markus@grabert.dev>
 * This is the main() class of the Spring Boot Application
 * It implements a simple reverse proxy
 */
@SpringBootApplication
public class ReverseProxyApplication implements CommandLineRunner {

    /** The reverse proxy properties. */
    @Autowired
    private ProxyProperties reverseProxyProperties;
    
    /** The load balancer. */
    @Autowired
	LoadBalancer loadBalancer;
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReverseProxyApplication.class);
	    app.setBannerMode(Banner.Mode.OFF);
	    app.run(args);
	}

    /**
     * Run.
     *
     * @param args the args
     */
    @Override
    public void run(String... args)  {
    	System.out.println("using properties: " + reverseProxyProperties);
    	
    	if (!CollectionUtils.isEmpty(reverseProxyProperties.getServices())) {
    		reverseProxyProperties.getServices().forEach(loadBalancer::addService);
    	}
    }
}
