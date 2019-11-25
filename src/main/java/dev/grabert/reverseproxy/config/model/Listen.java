package dev.grabert.reverseproxy.config.model;

import java.math.BigInteger;

// TODO: Auto-generated Javadoc
/**
 * This is a simple bean used as Spring Boot Configuration class and is automatically instantiated
 * during application start via {@link ProxyProperties}.
 *
 * @author Markus Grabert <markus@grabert.dev>
 * @see ProxyProperties
 */
public class Listen {
	
	/** The address. */
	private String address;
	
	/** The port. */
	private BigInteger port;

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public BigInteger getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(BigInteger port) {
		this.port = port;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Listen [address=" + address + ", port=" + port + "]";
	}

}
