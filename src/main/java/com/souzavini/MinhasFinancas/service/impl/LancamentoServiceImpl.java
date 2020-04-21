package com.souzavini.MinhasFinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.souzavini.MinhasFinancas.exceptions.RegraNegocioException;
import com.souzavini.MinhasFinancas.model.entity.Lancamentos;
import com.souzavini.MinhasFinancas.model.enums.StatusLancamento;
import com.souzavini.MinhasFinancas.model.repository.LancamentoRepository;
import com.souzavini.MinhasFinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;
	
	 public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamentos salvar(Lancamentos lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}
	

	@Override
	@Transactional
	public Lancamentos atualizar(Lancamentos lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamentos lancamento) {
		
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamentos> bsucar(Lancamentos lancamentoFiltro) {
		
		Example example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamentos lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamentos lancamento) {
		
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição válida");
			
		}
		
		if(lancamento.getMes() == null || lancamento.getMes()<1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês Válido");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() !=4) {
			throw new RegraNegocioException("Informe um ano válido");
		}
		
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usuario");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioException("Informe um valor válido");
			
		}
		
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("informe um tipo de Lançamento");
		}
		
	}

}
