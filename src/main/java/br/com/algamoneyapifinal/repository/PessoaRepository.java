package br.com.algamoneyapifinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.algamoneyapifinal.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
