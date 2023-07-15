package me.theophobia.shtipsbackend.repo;

import me.theophobia.shtipsbackend.Update;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateRepo extends JpaRepository<Update, Long> {
	List<Update> findAllByUser(User user);
}
