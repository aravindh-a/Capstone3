package gts.spring.musicManagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Slf4j
@SpringBootApplication
public class MusicManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicManagerApplication.class, args);
		log.info("Music Manager Application Started");
	}
}
