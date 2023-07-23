package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.chat.RecentChat;
import me.theophobia.shtipsbackend.chat.Message;
import me.theophobia.shtipsbackend.repo.MessageRepo;
import me.theophobia.shtipsbackend.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public final class MessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

	private final MessageRepo messageRepo;
	private final ActivityService activityService;

	private static MessageService instance = null;
	public static MessageService getInstance() {
		return instance;
	}

	@Autowired
	public MessageService(
		MessageRepo messageRepo,
		ActivityService activityService
	) {
		this.messageRepo = messageRepo;
		this.activityService = activityService;

		// TODO: probably a based hack, we ball
		if (instance == null) {
			instance = this;
		}
	}

	public List<Message> getMessagesBetweenUsers(User user1, User user2, LocalDateTime before, int n) {
		List<Message> a = messageRepo.findNMessagesBeforeTimestamp(user1, user2, user2, user1, before, n);
		Collections.reverse(a);
		return a;
	}

	public List<RecentChat> getRecentChats(Long userId) {
		return messageRepo.findLatestMessagesWithDistinctUsers(userId).stream()
			.map(m -> m.toRecentChat(userId)).collect(Collectors.toList());
	}

	public Message save(Message message) {
		if (Objects.equals(message.getSender().getId(), message.getReceiver().getId())) {
			LOGGER.error("Sent message is between the same user {}", message.getSender());
			return null;
		}
		activityService.submitActivity(message.getSender(), message.getTimestamp());
		return messageRepo.save(message);
	}
}
