package br.com.algamoneyapifinal.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.algamoneyapifinal.model.Lancamento;
import br.com.algamoneyapifinal.repository.filter.LancamentoFilter;
import br.com.algamoneyapifinal.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
