package me.theophobia.txr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TxrApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxrApplication.class, args);
	}

}
