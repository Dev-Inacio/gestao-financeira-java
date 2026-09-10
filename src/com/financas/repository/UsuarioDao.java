package com.financas.repository;

import com.financas.model.Usuario;

public interface UsuarioDao {

    Usuario salvar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    Usuario buscarPorId(int usuarioId);
}
