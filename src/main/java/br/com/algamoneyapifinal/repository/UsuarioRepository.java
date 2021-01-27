package br.com.algamoneyapifinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.algamoneyapifinal.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{ //Long é o tipo de chave primária.

	public Optional<Usuario> findByEmail(String email); //Optional - se não encontrar, não precisa verificar se é diferente de null. É uma abordagem mais orientada ao objeto.
} //Está sendo utilizado o findByEmail, p/ não precisar implementar de uma outra forma, é um dos motivos por ter deixado permissoes como EAGER na classe Usuario.
