package me.theophobia.shtipsbackend.chat;

import jakarta.persistence.*;
import lombok.*;
import me.theophobia.shtipsbackend.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public final class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable = false)
	private User receiver;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	private MessageDataType type;

	@Column(length = 4000)
	private String data; // text message OR file

	@Column(length = 250)
	private String bonusData; // Present if type == FILE, this is file name

	public AnonymousMessage toAnonymousMessage() {
		return new AnonymousMessage(
			sender.getUsername(),
			timestamp,
			type,
			data,
			bonusData
		);
	}

	public RecentChat toRecentChat(Long userId) {
		return new RecentChat(
			sender.getId().equals(userId) ? receiver.getUsername() : sender.getUsername(),
			timestamp,
			data,
			bonusData,
			type
		);
	}
}
