package fr.simple.edm.common.dto;

import java.io.Serializable;
import java.util.Date;

import fr.simple.edm.common.EdmNodeType;

public class EdmDocumentDto extends EdmNodeDto implements Serializable {

    private String description;

    private String fileExtension;

    private String fileContentType;
    
    private String serverDocumentFilePath;
    
    // only used to make a bridge with uploaded file
    private String temporaryFileToken;
    
    private Date date;
    
    public EdmDocumentDto() {
        super(EdmNodeType.DOCUMENT);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTemporaryFileToken() {
        return temporaryFileToken;
    }

    public void setTemporaryFileToken(String temporaryFileToken) {
        this.temporaryFileToken = temporaryFileToken;
    }

    public String getServerDocumentFilePath() {
        return serverDocumentFilePath;
    }

    public void setServerDocumentFilePath(String serverDocumentFilePath) {
        this.serverDocumentFilePath = serverDocumentFilePath;
    }
}
