package com.manual.model;

public class ImageData {
	private String imageId;
	private String imagePath;
	
	public ImageData() {
		super();
	}

	public ImageData(String imageId, String imagePath) {
		super();
		this.imageId = imageId;
		this.imagePath = imagePath;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
