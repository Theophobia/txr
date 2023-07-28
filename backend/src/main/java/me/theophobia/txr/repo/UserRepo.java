package me.theophobia.txr.repo;

import me.theophobia.txr.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	@Query(value = "SELECT * FROM users WHERE levenshtein(username, :searchTerm) <= :maxDistance ORDER BY levenshtein(username, :searchTerm) ASC LIMIT :limit", nativeQuery = true)
	List<User> findSimilarByName(@Param("searchTerm") String searchTerm, @Param("maxDistance") int maxDistance, @Param("limit") int limit);

}
