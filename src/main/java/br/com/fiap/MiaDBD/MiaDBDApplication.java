package br.com.fiap.MiaDBD;

import br.com.fiap.MiaDBD.models.Application;
import br.com.fiap.MiaDBD.repositories.ApplicationRepository;
import br.com.fiap.MiaDBD.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class MiaDBDApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiaDBDApplication.class, args);
	}

}
