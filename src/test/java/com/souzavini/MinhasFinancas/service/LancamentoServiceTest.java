package com.souzavini.MinhasFinancas.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.souzavini.MinhasFinancas.exceptions.RegraNegocioException;
import com.souzavini.MinhasFinancas.model.entity.Lancamentos;
import com.souzavini.MinhasFinancas.model.enums.StatusLancamento;
import com.souzavini.MinhasFinancas.model.repository.LancamentoRepository;
import com.souzavini.MinhasFinancas.model.repository.LancamentoRepositoryTest;
import com.souzavini.MinhasFinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamentos lancamentoASalvar = LancamentoRepositoryTest.criarLancamentos();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamentos lancamentoSalvo = LancamentoRepositoryTest.criarLancamentos();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		
		//execucao
		Lancamentos lancamento = service.salvar(lancamentoASalvar);
		
		//verificacao
		
		Assertions.assertThat(lancamento.getId() ).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus() ).isEqualTo(StatusLancamento.PENDENTE);
		
	}
	
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamentos lancamentoASalvar = LancamentoRepositoryTest.criarLancamentos();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
		
		Mockito.verify(repository,Mockito.never()).save(lancamentoASalvar);
	}
	
	
	
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		Lancamentos lancamentoSalvo = LancamentoRepositoryTest.criarLancamentos();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		
		//execucao
		Lancamentos lancamento = service.atualizar(lancamentoSalvo);
		
		//verificacao
		Mockito.verify(repository,Mockito.times(1)).save(lancamentoSalvo);

		
	}
	
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamentos lancamentoASalvar = LancamentoRepositoryTest.criarLancamentos();
		
		Assertions.catchThrowableOfType( () -> service.atualizar(lancamentoASalvar), NullPointerException.class);
		
		Mockito.verify(repository,Mockito.never()).save(lancamentoASalvar);
	}
	
	
	
	
}
