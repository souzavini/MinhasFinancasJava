package com.souzavini.MinhasFinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.souzavini.MinhasFinancas.model.entity.Lancamentos;
import com.souzavini.MinhasFinancas.model.enums.StatusLancamento;
import com.souzavini.MinhasFinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManger;
	
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamentos lancamento = criarLancamentos();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}
	
	public void deletarLancamento() {
		Lancamentos lancamento = criarEPersistirUmLancamento();
		
		 lancamento = entityManger.find(Lancamentos.class, lancamento.getId());
		
		 repository.delete(lancamento);
		 
		 Lancamentos lancamentoInexistente = entityManger.find(Lancamentos.class, lancamento.getId());
		 
		 Assertions.assertThat(lancamentoInexistente).isNull();
	}
	
	public void deveBuscarUmLancamentoPorId() {
		Lancamentos lancamento = criarEPersistirUmLancamento();
		
		Optional<Lancamentos> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamentos lancamento = criarEPersistirUmLancamento();
		
		lancamento.setAno(2018);
		lancamento.setDescricao("teste atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamentos lancamentoAtualizado = entityManger.find(Lancamentos.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("teste atualizar");
		Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
		
	}
	
	
	
	
	
	

	private Lancamentos criarEPersistirUmLancamento() {
		Lancamentos lancamento = criarLancamentos();
		 entityManger.persist(lancamento);
		return lancamento;
	}
	
	


	public static Lancamentos criarLancamentos() {
		return Lancamentos.builder()
				.ano(2019)
				.mes(1)
				.descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();
	}
}
