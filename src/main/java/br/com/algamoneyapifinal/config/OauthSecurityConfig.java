package br.com.algamoneyapifinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class OauthSecurityConfig extends WebSecurityConfigurerAdapter{ //Sem a classe OauthSecurityConfig, não será possível injetar a propriedade AuthenticationManager que está como dependência em AuthorizationServerConfig. Por isso AuthenticationManager será definida como um Bean aqui na classe OauthSecurityConfig.

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() { //P/ conseguir ler a senha que está encodada.
		return new BCryptPasswordEncoder(); //
	}
}
