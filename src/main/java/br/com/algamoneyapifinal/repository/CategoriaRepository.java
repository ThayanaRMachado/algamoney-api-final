package br.com.algamoneyapifinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.algamoneyapifinal.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
