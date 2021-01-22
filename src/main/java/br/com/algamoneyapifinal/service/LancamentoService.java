package br.com.algamoneyapifinal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.algamoneyapifinal.model.Lancamento;
import br.com.algamoneyapifinal.model.Pessoa;
import br.com.algamoneyapifinal.repository.LancamentoRepository;
import br.com.algamoneyapifinal.repository.PessoaRepository;
import br.com.algamoneyapifinal.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	public Lancamento salvar(Lancamento lancamento) throws PessoaInexistenteOuInativaException {
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		
		if (!pessoa.isPresent() || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}
}
