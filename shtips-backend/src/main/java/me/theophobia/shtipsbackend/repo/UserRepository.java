package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
}
