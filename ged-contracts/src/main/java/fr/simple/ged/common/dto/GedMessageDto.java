package fr.simple.ged.common.dto;

import java.io.Serializable;

public class GedMessageDto implements Serializable {

	public String message;

	public GedMessageDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
