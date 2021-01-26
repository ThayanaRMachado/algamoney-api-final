package br.com.algamoneyapifinal.repository.lancamento;

import java.util.List;
import br.com.algamoneyapifinal.model.Lancamento;
import br.com.algamoneyapifinal.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
