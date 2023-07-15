package me.theophobia.shtipsbackend;

import jakarta.persistence.*;
import me.theophobia.shtipsbackend.user.User;

@Entity
public class Update {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	private UpdateType type;

	public Update() {
	}

	public Update(Long id, User user, UpdateType type) {
		this.id = id;
		this.user = user;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UpdateType getType() {
		return type;
	}

	public void setType(UpdateType type) {
		this.type = type;
	}

	public Plain toPlain() {
		return new Plain(type);
	}

	public record Plain(UpdateType type) {
		@Override
		public String toString() {
			return type.toString();
		}
	}

}
