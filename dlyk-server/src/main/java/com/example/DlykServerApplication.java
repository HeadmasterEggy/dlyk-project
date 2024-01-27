package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.example.mapper"})
@SpringBootApplication
public class DlykServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlykServerApplication.class, args);
	}

}
