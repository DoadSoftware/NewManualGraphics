package com.manual.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Configurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configurations {
	
	@XmlElement(name="ipAddressEverest")
	private String ipAddressEverest;
	
	@XmlElement(name="ipAddressScenes")
	private String ipAddressScenes;
	
	@XmlElement(name="portNumber")
	private int portNumber;
	
	
	public Configurations(String ipAddressEverest, String ipAddressScenes, int portNumber) {
		super();
		this.ipAddressEverest = ipAddressEverest;
		this.ipAddressScenes = ipAddressScenes;
		this.portNumber = portNumber;
	}

	public Configurations() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getIpAddressEverest() {
		return ipAddressEverest;
	}

	public void setIpAddressEverest(String ipAddressEverest) {
		this.ipAddressEverest = ipAddressEverest;
	}

	public String getIpAddressScenes() {
		return ipAddressScenes;
	}

	public void setIpAddressScenes(String ipAddressScenes) {
		this.ipAddressScenes = ipAddressScenes;
	}

	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

}
