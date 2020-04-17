package com.souzavini.MinhasFinancas.exceptions;

public class ErroAutenticacao extends RuntimeException{
	
	public ErroAutenticacao(String mensagem) {
		super(mensagem);
	}
}
