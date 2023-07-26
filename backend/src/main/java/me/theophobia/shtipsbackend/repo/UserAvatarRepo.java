package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.user.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAvatarRepo extends JpaRepository<UserAvatar, Long> {
	Optional<UserAvatar> findByUser_Id(Long userId);
	Optional<UserAvatar> findByUser(User user);
}
