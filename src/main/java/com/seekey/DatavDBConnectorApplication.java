package com.seekey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:spring/context-db.xml"})
public class DatavDBConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatavDBConnectorApplication.class, args);
	}

}
