package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.message.Message;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
//	Page<Message> findAllByOrderByTimestampDesc(Pageable pageable);
	Page<Message> findAllBySenderAndReceiverOrSenderAndReceiverOrderByTimestampAsc(User sender1, User receiver1, User sender2, User receiver2, Pageable pageable);

}
