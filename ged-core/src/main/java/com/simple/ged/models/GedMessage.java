package com.simple.ged.models;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Some message
 * @author xavier
 *
 */
@Entity
@Table(name="message")
public class GedMessage {

	/**
	 * The ID
	 */
    @Id
    @GeneratedValue
    @Column(name="rowid")
	private Integer id;
	
	/**
	 * The message date
	 */
    @Column(name="message_date")
	private Date date;
	
	/**
	 * The message label
	 */
    @Column(name="label")
	private String label;
	
	/**
	 * Was message already read ?
	 */
    @Column(name="read")
	private boolean read;
	
	/**
	 * The level of the message
	 * Can be :
	 * 		NEUTRAL
	 * 		INFO
	 * 		ERROR
	 */
    @Column(name="level")
	private String messageLevel;

	
	public GedMessage() {
		this.id = null;
		this.read = false;
	}
	
	public GedMessage(String level, String label) {
		this.id = null;
		this.date = new GregorianCalendar().getTime();
		this.read = false;
		this.messageLevel = level;
		this.label = label;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isRead() {
		return read;
	}
	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getMessageLevel() {
		return messageLevel;
	}

	public void setMessageLevel(String messageLevel) {
		this.messageLevel = messageLevel;
	}
}
