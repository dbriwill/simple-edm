package fr.simple.edm.common.dto;

import java.io.Serializable;

public class EdmMessageDto implements Serializable {

	protected String id;
	
    protected String date;

    protected String message;

    public EdmMessageDto() {
        this.id      = "undefined";
        this.date    = "undefined";
        this.message = "undefined";
    }

    public EdmMessageDto(String id, String message) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
