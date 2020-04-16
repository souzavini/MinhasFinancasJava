package com.souzavini.MinhasFinancas.service;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.souzavini.MinhasFinancas.model.repository.UsuarioRepository;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario
		repository.deleteAll();
		
		//acao
		service.validarEmail("email@email.com");
		
		
	}
	
}
