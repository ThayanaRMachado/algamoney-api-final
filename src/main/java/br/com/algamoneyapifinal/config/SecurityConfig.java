package br.com.algamoneyapifinal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration //Classe de configuração.
@EnableWebSecurity //Habilita a segurança. Essa anotação já vem com o @Configuration, portanto não é obrigatório colocá-la no código Só se quiser.
public class SecurityConfig extends WebSecurityConfigurerAdapter{ //com o WebSecurityConfigurerAdapter, ganha-se alguns métodos p/ customizar a configuração.

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication() //Onde o usuário e a senha serão validados. Pode-se buscar no banco de dados, mas aqui ficará em memória.
		.withUser("admin").password("{noop}admin").roles("ROLE"); //Na autenticação em memória passa-se o usuário, a senha, (para uma autenticação) e uma permissão, (para uma autorização), que esse usuário poderia ter.A permissão não será utilizada agora.
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { //Configura as autorizações das requisições
		http.authorizeRequests() //Autoriza requisições
		.antMatchers("/categorias").permitAll() //Para /categorias, qualquer um pod acessar.
		.anyRequest().authenticated() //Para qualquer requisição é preciso estar autenticado (usuário e senha validados).
		.and()
		.httpBasic().and() //Tipo de autenticação utilizada. httpBasic()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //Desabilita a criação de sessão no servidor. A API REST não terá esado de nada.
		.csrf().disable(); //Desabilita o csrf para que não se possa ser feito um Java Script injection dentro do serviço web.Como na aplicação não tem parte web, ele pode ser desabilitado.
	}
}
