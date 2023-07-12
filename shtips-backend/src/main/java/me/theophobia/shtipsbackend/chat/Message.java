package me.theophobia.shtipsbackend.chat;

import jakarta.persistence.*;
import me.theophobia.shtipsbackend.user.User;

import java.time.LocalDateTime;

@Entity
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

	public Message() {
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public MessageDataType getType() {
		return type;
	}

	public void setType(MessageDataType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getBonusData() {
		return bonusData;
	}

	public void setBonusData(String bonusData) {
		this.bonusData = bonusData;
	}

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

	@Override
	public String toString() {
		return "Message{" +
			"messageId=" + messageId +
			", sender=" + sender +
			", receiver=" + receiver +
			", timestamp=" + timestamp +
			", type=" + type +
			", data='" + data + '\'' +
			", bonusData='" + bonusData + '\'' +
			'}';
	}
}
