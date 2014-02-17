package fr.simple.edm.common.dto;

import java.io.Serializable;

/**
 * This is the response which is sent from server when you upload some file. 
 * It give you the file server reference. It's an help for async uploads
 * 
 * @author xavier
 *
 */
public class EdmDocumentUploadResponse implements Serializable {

    private String temporaryFileToken;

    public String getTemporyFileToken() {
        return temporaryFileToken;
    }

    public void setTemporyFileToken(String temporaryFileToken) {
        this.temporaryFileToken = temporaryFileToken;
    }
}
