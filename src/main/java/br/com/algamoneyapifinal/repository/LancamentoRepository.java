package br.com.algamoneyapifinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.algamoneyapifinal.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
