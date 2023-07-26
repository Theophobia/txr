package me.theophobia.shtipsbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void ensureExtensionsExist() {
		if (!isExtensionInstalled("pg_trgm")) {
			executeSql("CREATE EXTENSION IF NOT EXISTS pg_trgm;");
		}

		if (!isExtensionInstalled("fuzzystrmatch")) {
			executeSql("CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;");
		}
	}

	private boolean isExtensionInstalled(String extensionName) {
		String query = "SELECT count(*) FROM pg_extension WHERE extname = ?";
		int count = jdbcTemplate.queryForObject(query, Integer.class, extensionName);
		return count > 0;
	}

	private void executeSql(String sql) {
		jdbcTemplate.execute(sql);
	}
}
