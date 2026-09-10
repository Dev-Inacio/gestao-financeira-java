package com.financas.service;

import com.financas.exception.DadoInvalidoException;
import com.financas.exception.EmailJaCadastradoException;
import com.financas.exception.UsuarioNaoEncontradoException;
import com.financas.model.Usuario;
import com.financas.repository.UsuarioDao;

public class UsuarioService {

    private UsuarioDao usuarioDao;
    private ContaService contaService;

    public UsuarioService(UsuarioDao usuarioDao, ContaService contaService) {
        this.usuarioDao = usuarioDao;
        this.contaService = contaService;
    }

    public Usuario cadastrarUsuario(String nome, String email, String senha) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new DadoInvalidoException("O nome não pode ser vazio.");
        }
        if (!nome.matches("^[\\p{L} ]+$")) {
            throw new DadoInvalidoException("O nome deve conter apenas letras e espaços.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new DadoInvalidoException("O email não pode ser vazio.");
        }

        if (senha == null || senha.trim().isEmpty() || senha.length() < 6) {
            throw new DadoInvalidoException("A senha deve ter no minimo 6 caracteres.");
        }

        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new DadoInvalidoException("O email informado não e válido.");
        }

        if (usuarioDao.buscarPorEmail(email) != null) {
            throw new EmailJaCadastradoException("Já existe um usuário cadastrado com este email.");
        }

        Usuario usuario = new Usuario(nome, email, senha);
        Usuario usuarioSalvo = usuarioDao.salvar(usuario);
        contaService.criarConta(usuarioSalvo,0.0);
        return usuarioSalvo;
    }

    public Usuario autenticar(String email, String senha){

        Usuario usuario = usuarioDao.buscarPorEmail(email);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuario ou senha invalidos.");
        }else if(!usuario.getSenha().equals(senha)){
            throw new UsuarioNaoEncontradoException("Usuario ou senha invalidos.");
        }
        return usuario;
    }
}
