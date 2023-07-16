package me.theophobia.shtipsbackend.update;

import com.google.gson.Gson;
import jakarta.persistence.*;
import me.theophobia.shtipsbackend.user.User;

@Entity
public class NewMessageUpdate implements IUpdate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User sender;

	@ManyToOne
	private User receiver;

	private UpdateType type = UpdateType.NEW_MESSAGE;

	public NewMessageUpdate() {
	}

	public NewMessageUpdate(Long id, User sender, User receiver, UpdateType type) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User user) {
		this.receiver = user;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public UpdateType getType() {
		return type;
	}

	public Plain toPlain() {
		return new Plain(type, sender.getUsername(), receiver.getUsername());
	}

	public record Plain(UpdateType type, String sender, String receiver) implements IPlain {
		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}

}
