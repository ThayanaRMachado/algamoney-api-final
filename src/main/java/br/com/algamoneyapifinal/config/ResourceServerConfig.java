package br.com.algamoneyapifinal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration //Classe de configuração.
@EnableWebSecurity //Habilita a segurança. Essa anotação já vem com o @Configuration, portanto não é obrigatório colocá-la no código Só se quiser.
@EnableResourceServer //Habilita o Resource Server.
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{ //ResourceServerConfig vai exercer a função do Resource Server.Com o ResourceServerConfigurerAdapter, ganha-se alguns métodos p/ customizar a configuração.

	@Autowired //Injeta o AuthenticationManagerBuilder
	public void configure(AuthenticationManagerBuilder auth) throws Exception { //Construtor de gerenciador de autenticação
		auth.inMemoryAuthentication() //Onde o usuário e a senha serão validados. Pode-se buscar no banco de dados, mas aqui ficará em memória.
		.withUser("admin").password("admin").roles("ROLE"); //Na autenticação em memória passa-se o usuário, a senha, (para uma autenticação) e uma permissão, (para uma autorização), que esse usuário poderia ter.A permissão não será utilizada agora.
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception { //Configura as autorizações das requisições
		http.authorizeRequests() //Autoriza requisições
		.antMatchers("/categorias").permitAll() //Para /categorias, qualquer um pod acessar.
		.anyRequest().authenticated() //Para qualquer requisição é preciso estar autenticado (usuário e senha validados).
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //Desabilita a criação de sessão no servidor. A API REST não terá esado de nada.
		.csrf().disable(); //Desabilita o csrf para que não se possa ser feito um Java Script injection dentro do serviço web.Como na aplicação não tem parte web, ele pode ser desabilitado.
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception{
		resources.stateless(true); //Para deixar o servidor sem estado. Não vai armazenar nada no resource.
	}
}
