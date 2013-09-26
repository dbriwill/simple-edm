package com.simple.ged.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.simple.ged.models.GedMessage;

/**
 * This class is the message DAO
 * 
 * @author xavier
 *
 */
public interface GedMessageRepository extends JpaRepository<GedMessage, Integer> {

	public List<GedMessage> findByReadFalse(); 
	
	@Query("UPDATE GedMessage SET read=true")
	public void markAllMessagesAsRead();
}
