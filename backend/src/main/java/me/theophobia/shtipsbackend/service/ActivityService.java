package me.theophobia.shtipsbackend.service;

import lombok.Synchronized;
import me.theophobia.shtipsbackend.activity.ActivityStatus;
import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.ws.WebSocketStore;
import me.theophobia.shtipsbackend.ws.event.Event31;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {

	private final Map<User, LocalDateTime> lastActivityTime = new HashMap<>();
	private final Map<User, ActivityStatus> activityStatus = new HashMap<>();

	private static ActivityService instance = null;
	public static ActivityService getInstance() {
		return instance;
	}

	@Autowired
	public ActivityService() {
		// TODO: probably a based hack, we ball
		if (instance == null) {
			instance = this;
		}
	}

	@Synchronized
	@Scheduled(fixedRate = 5_000)
	public void updateActivities() {
		final long nowSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

		lastActivityTime.forEach((user, time) -> {
			long timeSeconds = time.toEpochSecond(ZoneOffset.UTC);
			long diff = nowSeconds - timeSeconds;

			ActivityStatus oldActivity = activityStatus.get(user);
			ActivityStatus newActivity;

			if (diff > 20) {
				newActivity = ActivityStatus.OFFLINE;
			}
			else if (diff > 10) {
				newActivity = ActivityStatus.AWAY;
			}
			else {
				newActivity = ActivityStatus.ONLINE;
			}

			activityStatus.put(user, newActivity);
			if (!newActivity.equals(oldActivity)) {
				Event31.Data data = new Event31.Data(user.getUsername(), newActivity);
				Event31 event31 = new Event31(List.of(data));

				WebSocketStore.getInstance().sendMessage(user, "0031", event31.json());
			}
		});

		System.out.println("activityStatus = " + activityStatus);
	}

	@Synchronized
	public Map<User, ActivityStatus> getActivityStatus() {
		return activityStatus;
	}

	@Synchronized
	public ActivityStatus getActivityStatusOf(User user) {
		return activityStatus.getOrDefault(user, ActivityStatus.OFFLINE);
	}

	@Synchronized
	public void submitActivity(User user, LocalDateTime time) {
		if (time == null) {
			return;
		}

		if (time.isAfter(LocalDateTime.now())) {
			return;
		}

		LocalDateTime lastUpdate = lastActivityTime.get(user);
		if (lastUpdate != null && time.isBefore(lastUpdate)) {
			return;
		}

		lastActivityTime.put(user, time);

		updateActivities();
	}
}
