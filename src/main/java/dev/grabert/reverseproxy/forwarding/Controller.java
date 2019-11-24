package dev.grabert.reverseproxy.forwarding;

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

@RestController
public class Controller {

	@Autowired
	LoadBalancer loadBalancer;

	@GetMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downstreamGetResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers) {

		return doRestCall(HttpMethod.GET, params, headers, null);
	}

	@PostMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downstreamPostResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.POST, params, headers, body);
	}

	@PutMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downstreamPutResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.PUT, params, headers, body);
	}
	
	@DeleteMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downstreamDeleteResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.DELETE, params, headers, body);
	}

	@PatchMapping(value = "/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downstreamPatchResponse(@PathVariable("path") String path,
			@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers,
			@RequestBody String body) {

		return doRestCall(HttpMethod.PATCH, params, headers, body);
	}
	
	private ResponseEntity<String> doRestCall(HttpMethod httpMethod, MultiValueMap<String, String> params,
			MultiValueMap<String, String> headers, String body) {
		final String requestedService = headers.getFirst("host");
		final Host downstreamHost = loadBalancer.getNextHostForDomain(requestedService);

		System.out.println("ServiceName: " + requestedService);
		loadBalancer.getDomains().forEach(key -> System.out.println("Domain: " + key + ": " + loadBalancer.getServiceForDomain(key)));
		
		if (downstreamHost != null) {
			final String urlString = "http://" + downstreamHost.getAddress() + ":" + downstreamHost.getPort() + "/";

			RestTemplate restTemplate = new RestTemplate();

			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.remove("host");

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
			uriBuilder.queryParams(params);

			System.out.println("    Request: " + uriBuilder.toUriString());

			HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), httpMethod, requestEntity, String.class);

			return response;
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

}
