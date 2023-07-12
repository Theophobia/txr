package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.chat.RecentChat;
import me.theophobia.shtipsbackend.chat.Message;
import me.theophobia.shtipsbackend.repo.MessageRepo;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class MessageService {

	private final MessageRepo messageRepo;

	@Autowired
	public MessageService(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	public Page<Message> getMessagesBetweenUsers(User user1, User user2, Pageable pageable) {
		Page<Message> a = messageRepo.findAllBySenderAndReceiverOrSenderAndReceiverOrderByTimestampDesc(user1, user2, user2, user1, pageable);
//		System.out.println("a.toList() = " + a.toList());
//		System.out.println("user1 = " + user1);
//		System.out.println("user2 = " + user2);
		return a;
	}

	public List<RecentChat> getRecentChats(Long userId) {
		return messageRepo.findLatestMessagesWithDistinctUsers(userId).stream()
			.map(m -> m.toRecentChat(userId)).collect(Collectors.toList());
	}

	public void saveMessage(Message message) {
		messageRepo.save(message);
	}
}
