package br.com.algamoneyapifinal.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.algamoneyapifinal.model.Usuario;
import br.com.algamoneyapifinal.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService{ //Ensina como buscar o usuário

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //carrega o usuário pelo username p/ o Spring poder validar usuário e senha. 
		
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email); //Busca o usuário. Na hora de logar no sistema tem q/ buscar o email.
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos")); //Se não der certo, retorna a mensagem
		
		return new UsuarioSistema(usuario, getPermissoes(usuario));//Não retorna mais User e sim a classe UsuarioSistema que extende de User. Então continua sendo User. Retorna  usuário logado.
	}
	
	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>(); //Cria uma lista de permissões
		usuario.getPermissoes().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase()))); //Carrega as pemissões do usuário. Para cada uma dessas permissões eu vou adicionando dentro do authorities, p/ validar.
		return authorities; //valida se o usuário e a senha estão corretos.
	}
}
