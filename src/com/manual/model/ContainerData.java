package com.manual.model;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="containerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainerData {
	
	@XmlElementWrapper(name = "containers")
	@XmlElement(name = "container")
	private List<Container> containers;
	//List<Container> containers = new ArrayList<Container>
	
	@XmlTransient
	private String manual_file_timestamp;
	
	private String scene_path;
	
	public ContainerData(List<Container> containers) {
		super();
		this.containers = containers;
	}
	
	public ContainerData() {
		super();
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public String getManual_file_timestamp() {
		return manual_file_timestamp;
	}

	public void setManual_file_timestamp(String manual_file_timestamp) {
		this.manual_file_timestamp = manual_file_timestamp;
	}

	public String getScene_path() {
		return scene_path;
	}

	public void setScene_path(String scene_path) {
		this.scene_path = scene_path;
	}
	
}
