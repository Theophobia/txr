package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.chat.Message;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
//	Page<Message> findAllByOrderByTimestampDesc(Pageable pageable);
	Page<Message> findAllBySenderAndReceiverOrSenderAndReceiverOrderByTimestampDesc(User sender1, User receiver1, User sender2, User receiver2, Pageable pageable);

//	List<Message> findDistinctBySender_IdOrReceiver_IdOrderBySender_IdAscReceiver_IdAscTimestampDesc(long id1, long id2);

	@Query("SELECT m FROM Message m " +
		"WHERE (m.sender.id = :userId OR m.receiver.id = :userId) " +
		"AND (m.sender.id <> m.receiver.id) " +
		"AND m.timestamp = (SELECT MAX(m2.timestamp) " +
		"FROM Message m2 " +
		"WHERE (m2.sender.id = m.sender.id OR m2.sender.id = m.receiver.id) " +
		"AND (m2.receiver.id = m.sender.id OR m2.receiver.id = m.receiver.id))")
	List<Message> findLatestMessagesWithDistinctUsers(@Param("userId") Long userId);

//	default List<RecentChat> findRecentChats(Long userId) {
//		List<Message> messages = findDistinctBySender_IdOrReceiver_IdOrderBySender_IdAscReceiver_IdAscTimestampDesc(userId, userId);
//
//		interface Lambda {
//			String getUsername(Message m);
//		}
//		Lambda a = (Message message) -> message.getReceiver().getId().equals(userId) ? message.getSender().getUsername() : message.getReceiver().getUsername();
//
//		List<RecentChat> recentChats = messages.stream()
//			.map(message -> new RecentChat(a.getUsername(message), message.getTimestamp(), message.getData(), message.getBonusData(), message.getType()))
//			.collect(Collectors.toList());
//
//		return recentChats;
//	}
}
