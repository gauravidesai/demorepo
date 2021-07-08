package com.app;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.app.Controller.CodingTestService;


@EnableWebSecurity
@SpringBootApplication

public class XmlValidatorApplication implements CommandLineRunner {
	
//Return a logger named corresponding to the class passed as parameter,
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(CodingTestService.class);
	
//used to autowire bean	
	@Autowired
	CodingTestService codingTestService;

	public static void main(String[] args) {
		SpringApplication.run(XmlValidatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		File input = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"sampleInput.xml");
		logger.info(String.valueOf(codingTestService.validateInput(input)));
		
		if(true)
		{
			codingTestService.readXml(input);
			
		}
	
			
		
	}

}
