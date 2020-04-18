package com.souzavini.MinhasFinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.souzavini.MinhasFinancas.exceptions.ErroAutenticacao;
import com.souzavini.MinhasFinancas.exceptions.RegraNegocioException;
import com.souzavini.MinhasFinancas.model.entity.Usuario;
import com.souzavini.MinhasFinancas.model.repository.UsuarioRepository;
//import com.souzavini.MinhasFinancas.model.repository.UsuarioRepositoryTest;
import com.souzavini.MinhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email");
		}
		
		
	}

}
