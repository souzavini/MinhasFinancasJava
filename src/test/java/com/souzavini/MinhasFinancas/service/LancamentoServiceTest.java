package com.souzavini.MinhasFinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.souzavini.MinhasFinancas.exceptions.RegraNegocioException;
import com.souzavini.MinhasFinancas.model.entity.Lancamentos;
import com.souzavini.MinhasFinancas.model.entity.Usuario;
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
	
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenario
		Lancamentos lancamento = LancamentoRepositoryTest.criarLancamentos();
		lancamento.setId(1l);
		
		//execução e verificação
		service.deletar(lancamento);
		
		//verificação
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenario
		Lancamentos lancamento = LancamentoRepositoryTest.criarLancamentos();
		
		
		//execução e verificação
		Assertions.catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class);
		
		//verificação
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		//cenario
		Lancamentos lancamento = LancamentoRepositoryTest.criarLancamentos();
		lancamento.setId(1l);
		
		List<Lancamentos> lista = java.util.Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//execução
		List<Lancamentos> resultado = service.buscar(lancamento);
		
		//verificações
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		//cenario
				Lancamentos lancamento = LancamentoRepositoryTest.criarLancamentos();
				lancamento.setId(1l);
				lancamento.setStatus(StatusLancamento.PENDENTE);
				
			StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
			Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
			
			//execução
			service.atualizarStatus(lancamento,novoStatus);
			
			//verificações
			
			Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
			Mockito.verify(service).atualizar(lancamento);
				
	}
	
	@Test
	public void deveObterUmLancamentoPorId(){
		//cenario 
		Long id = 1l;
		
		Lancamentos lancamento = LancamentoRepositoryTest.criarLancamentos();
		lancamento.setId(1l);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//execução
		Optional<Lancamentos> resultado = service.obterPorId(id);
		
		//verificação
		Assertions.assertThat(resultado.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioQuandoUmLancamentoNaoExiste(){
		//cenario 
		Long id = 1l;
		
		Lancamentos lancamento = LancamentoRepositoryTest.criarLancamentos();
		lancamento.setId(1l);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//execução
		Optional<Lancamentos> resultado = service.obterPorId(id);
		
		//verificação
		Assertions.assertThat(resultado.isPresent()).isFalse();
		
	}
	
	@Test
	public void deveLancarErrosAoValidarLancamento() {
		Lancamentos lancamento = new Lancamentos();
		
		Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida");
		
		lancamento.setDescricao("");
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida");
		
		lancamento.setDescricao("Salario");
		
		 erro = Assertions.catchThrowable(() -> service.validar(lancamento));
			Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês Válido");
			
			lancamento.setAno(0);
			
			erro = Assertions.catchThrowable(() -> service.validar(lancamento));
			Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês Válido");
			
			lancamento.setAno(13);
			
			erro = Assertions.catchThrowable(() -> service.validar(lancamento));
			Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês Válido");
			
		lancamento.setMes(1);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido");
		
		
		
		lancamento.setAno(2020);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuario");
		
		lancamento.setAno(202);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido");
		
		
		
		
	
		
		
		
		
		
	}
	
	
}
