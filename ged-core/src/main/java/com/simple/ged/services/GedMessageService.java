package com.simple.ged.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simple.ged.dao.MessageRepository;
import com.simple.ged.models.GedMessage;
import com.simple.ged.tools.SpringFactory;

/**
 * 
 * This service is used to manage messages
 * 
 * @author Xavier
 *
 */
@Service
public final class GedMessageService {

	private MessageRepository gedMessageRepository = SpringFactory.getAppContext().getBean(MessageRepository.class);
	
	/**
	 * Get messages, sorted by date desc
	 */
	public List<GedMessage> getMessages() {
		return gedMessageRepository.findAllOrderByDateDesc();
	}
	
	/**
	 * Is there unread messages ?
	 */
	public boolean thereIsUnreadMessages() {
		return gedMessageRepository.findByReadFalse().size() > 0;
	}
	
	/**
	 * Mark all messages as read
	 */
	public void markAllMessagesAsRead() {
		gedMessageRepository.markAllMessagesAsRead();
	}
	
	/**
	 * Add some message
	 */
	public void save(GedMessage message) {
		gedMessageRepository.saveAndFlush(message);
	}
}
