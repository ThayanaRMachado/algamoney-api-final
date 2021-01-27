package br.com.algamoneyapifinal.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import br.com.algamoneyapifinal.event.RecursoCriadoEvent;
import br.com.algamoneyapifinal.model.Categoria;
import br.com.algamoneyapifinal.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')") //Adição das permissões. Se tem autorização e a regra do usuário. O usuário pode pesquisar categorias. O escopo é de leitura
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')") //Adição das permissões - O usuário pode cadastrar categorias. Escopo de escrita.
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	//hasAuthority('ROLE_PESQUISAR_CATEGORIA') é a permissão do usuário logado. |#oauth2.hasScope('read') é o escopo do cliente. O usuário admin, usando a aplicação angular, pode ler e escrever. O usuário admin usando mobile só pode ler. Além da permissão do usuário, tem a permissão da aplicação também.
	@GetMapping("{/codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")//Adição das permissões - O usuário pode pesquisar categorias. Se eu quiser que qualquer um acesse as categorias, eu não posso colocar essa regra. Escopo de leitura.
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		return categoria.isPresent()?
				ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();
	}
}
