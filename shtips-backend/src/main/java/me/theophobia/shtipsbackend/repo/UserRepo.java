package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
}
