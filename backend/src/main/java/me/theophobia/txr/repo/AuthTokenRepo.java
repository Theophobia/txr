package me.theophobia.txr.repo;

import me.theophobia.txr.auth.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepo extends JpaRepository<AuthToken, Long> {
	Optional<AuthToken> findByUserId(long userId);
	Optional<AuthToken> findByToken(String token);
}
