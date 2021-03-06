package br.com.algamoneyapifinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration //Classe de configuração.
@EnableResourceServer //Habilita o Resource Server.
@EnableGlobalMethodSecurity(prePostEnabled = true) //Habilita a segurança dos métodos da controladora.
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{ //ResourceServerConfig vai exercer a função do Resource Server.Com o ResourceServerConfigurerAdapter, ganha-se alguns métodos p/ customizar a configuração.
	
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
	
	@Bean //O Bean vai ter um handle para fazer a segurança dos métodos com oauth2.
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}
