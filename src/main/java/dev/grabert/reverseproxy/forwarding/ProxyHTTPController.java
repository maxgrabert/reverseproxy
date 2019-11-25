package dev.grabert.reverseproxy.forwarding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import dev.grabert.reverseproxy.config.model.Host;
import io.micrometer.core.annotation.Timed;


// TODO: Auto-generated Javadoc
/**
 * This is the Rest HTTP Controller that accepts all requests and forwards them
 * to the downstream services. It is making use of {@link LoadBalancer} to figure out
 * where to forward the incoming requests.
 *
 * @author Markus Grabert <markus@grabert.dev> 
 */
@RestController
@Timed
public class ProxyHTTPController {

    /** The logger. */
    Logger logger = LoggerFactory.getLogger(ProxyHTTPController.class);
    
	/** The load balancer. */
	@Autowired
	LoadBalancer loadBalancer;
	
	
	/**
	 * Downstream get response.
	 *
	 * @param path the path
	 * @param params the params
	 * @param headers the headers
	 * @return the response entity
	 */
	@GetMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> downstreamGetResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers) {

		return doRestCall(HttpMethod.GET, params, headers, null);
	}

	/**
	 * Downstream post response.
	 *
	 * @param path the path
	 * @param params the params
	 * @param headers the headers
	 * @param body the body
	 * @return the response entity
	 */
	@PostMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> downstreamPostResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.POST, params, headers, body);
	}

	/**
	 * Downstream put response.
	 *
	 * @param path the path
	 * @param params the params
	 * @param headers the headers
	 * @param body the body
	 * @return the response entity
	 */
	@PutMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> downstreamPutResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.PUT, params, headers, body);
	}
	
	/**
	 * Downstream delete response.
	 *
	 * @param path the path
	 * @param params the params
	 * @param headers the headers
	 * @param body the body
	 * @return the response entity
	 */
	@DeleteMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> downstreamDeleteResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.DELETE, params, headers, body);
	}

	/**
	 * Downstream patch response.
	 *
	 * @param path the path
	 * @param params the params
	 * @param headers the headers
	 * @param body the body
	 * @return the response entity
	 */
	@PatchMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> downstreamPatchResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.PATCH, params, headers, body);
	}
	
	/**
	 * utility method to perform a rest/HTTP call, used for making the request to the downstream server 
	 *
	 * @param httpMethod the http method
	 * @param params the params
	 * @param headers the headers
	 * @param body the body
	 * @return the response entity
	 */
	private ResponseEntity<String> doRestCall(HttpMethod httpMethod, MultiValueMap<String, String> params,
			MultiValueMap<String, String> headers, String body) {
		final String requestedService = headers.getFirst("host");
		final Host downstreamHost = loadBalancer.getNextHostForDomain(requestedService);

		System.out.println("ServiceName: " + requestedService);
		loadBalancer.getDomains().forEach(key -> logger.trace("Domain: " + key + ": " + loadBalancer.getServiceForDomain(key)));
		
		if (downstreamHost != null) {
			// ask the Load balancer which Host to use for the domain specified in the Host-Header.
			final String urlString = "http://" + downstreamHost.getAddress() + ":" + downstreamHost.getPort() + "/";

			RestTemplate restTemplate = new RestTemplate();

			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.remove("host");

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
			uriBuilder.queryParams(params);

			logger.trace("Request to downstream: " + uriBuilder.toUriString());

			HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), httpMethod, requestEntity, String.class);

			return response;
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

}
