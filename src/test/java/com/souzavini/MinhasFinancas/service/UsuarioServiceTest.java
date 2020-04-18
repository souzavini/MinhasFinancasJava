package com.souzavini.MinhasFinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.souzavini.MinhasFinancas.exceptions.ErroAutenticacao;
import com.souzavini.MinhasFinancas.exceptions.RegraNegocioException;
import com.souzavini.MinhasFinancas.model.entity.Usuario;
import com.souzavini.MinhasFinancas.model.repository.UsuarioRepository;
import com.souzavini.MinhasFinancas.service.impl.UsuarioServiceImpl;



@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp() {
		service = Mockito.spy(UsuarioServiceImpl.class);
		
		service = new UsuarioServiceImpl(repository);
	}
	
	public void deveSalvarUmUsuario() {
		//cenario
		
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email) ).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		
		//verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha") );
		
		//verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado para o email informado.");
	}
	
	@Test
	public void DeveLancarErroQuandoSenhaNaoBater() {
		//cenario 
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//açao
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123") );
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida");
		
		
	}
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("email@email.com");
			
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancaErroAoValidarEmailQuandoExistirEmailCadastrado(){
		
		
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		//acao
		 service.validarEmail("email@email.com");
		
	}
	

	
}
