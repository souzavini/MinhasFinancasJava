package com.souzavini.MinhasFinancas.service;

import java.util.List;

import com.souzavini.MinhasFinancas.model.entity.Lancamentos;
import com.souzavini.MinhasFinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamentos salvar(Lancamentos lancamento);
	
	Lancamentos atualizar(Lancamentos lancamento);
	
	void deletar(Lancamentos lancamento);
	
	List<Lancamentos> bsucar( Lancamentos lancamentoFiltro);
	
	void atualizarStatus(Lancamentos lancamento, StatusLancamento status);
	
	void validar(Lancamentos lancamento);
	
}
