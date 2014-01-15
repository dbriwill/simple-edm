package fr.simple.ged.common.dto;

import java.io.Serializable;

public class GedMessageDto implements Serializable {

    protected String id;

    protected String message;

    public GedMessageDto(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
