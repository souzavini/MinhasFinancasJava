package com.souzavini.MinhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.souzavini.MinhasFinancas.model.entity.Lancamentos;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long>{
	
}
