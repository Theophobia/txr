package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.auth.AuthToken;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepo extends JpaRepository<AuthToken, Long> {
	Optional<AuthToken> findByUserId(long userId);
	Optional<AuthToken> findByToken(String token);
}
