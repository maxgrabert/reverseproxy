package dev.grabert.reverseproxy.config.model;

import java.math.BigInteger;

public class Host {

	private String address;
	
	private BigInteger port;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigInteger getPort() {
		return port;
	}

	public void setPort(BigInteger port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Host [address=" + address + ", port=" + port + "]";
	}
	
}
