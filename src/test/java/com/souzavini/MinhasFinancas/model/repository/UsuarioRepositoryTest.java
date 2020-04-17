package com.souzavini.MinhasFinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import com.souzavini.MinhasFinancas.model.entity.Usuario;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificaraExistenciaDeUmEmail() {
		
		//cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		entityManager.persist(usuario);
		
		//acao/execuçao
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		
		//verificação
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//cenario
		
		
		//açao
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		//Verificação
		Assertions.assertThat(result).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// cenário
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		// cenário
		Usuario usuario =criarUsuario();
		entityManager.persist(usuario);
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
				
	}
	
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
				
	}
	
	public static Usuario criarUsuario() {
		return   Usuario.builder()
				.nome("Usuario")
				.email("usuario@gmail.com")
				.senha("senha")
				.build();
	}
}









