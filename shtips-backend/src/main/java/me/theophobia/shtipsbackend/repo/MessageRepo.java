package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.chat.Message;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
	Page<Message> findAllBySenderAndReceiverOrSenderAndReceiverOrderByTimestampDesc(User sender1, User receiver1, User sender2, User receiver2, Pageable pageable);

	@Query("SELECT m FROM Message m " +
		"WHERE (m.sender.id = :userId OR m.receiver.id = :userId) " +
		"AND (m.sender.id <> m.receiver.id) " +
		"AND m.timestamp = (SELECT MAX(m2.timestamp) " +
		"FROM Message m2 " +
		"WHERE (m2.sender.id = m.sender.id OR m2.sender.id = m.receiver.id) " +
		"AND (m2.receiver.id = m.sender.id OR m2.receiver.id = m.receiver.id))")
	List<Message> findLatestMessagesWithDistinctUsers(@Param("userId") Long userId);

	@Query("SELECT m FROM Message m " +
		"WHERE ((m.sender = :sender1 AND m.receiver = :receiver1) OR (m.sender = :sender2 AND m.receiver = :receiver2)) " +
		"AND m.timestamp < :timestamp " +
		"ORDER BY m.timestamp DESC LIMIT :n")
	List<Message> findNMessagesBeforeTimestamp(@Param("sender1") User sender1, @Param("receiver1") User receiver1,
											   @Param("sender2") User sender2, @Param("receiver2") User receiver2,
											   @Param("timestamp") LocalDateTime timestamp, @Param("n") int n);
}
