package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.message.Message;
import me.theophobia.shtipsbackend.repo.MessageRepo;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public final class MessageService {

	private final MessageRepo messageRepo;

	@Autowired
	public MessageService(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	public Page<Message> getMessagesBetweenUsers(User user1, User user2, Pageable pageable) {
		return messageRepo.findAllBySenderAndReceiverOrSenderAndReceiverOrderByTimestampAsc(user1, user2, user2, user1, pageable);
	}
}
