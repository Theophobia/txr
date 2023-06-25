package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.auth.AuthToken;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepo extends JpaRepository<AuthToken, Long> {
	AuthToken findByUserId(long userId);
	AuthToken findByToken(String token);
}
