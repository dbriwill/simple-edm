package com.simple.ged.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.simple.ged.dao.GedMessageRepository;
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

	private GedMessageRepository gedMessageRepository = SpringFactory.getAppContext().getBean(GedMessageRepository.class);
	
	/**
	 * Get messages, sorted by date desc
	 */
	public List<GedMessage> getMessages() {
		return gedMessageRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
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
