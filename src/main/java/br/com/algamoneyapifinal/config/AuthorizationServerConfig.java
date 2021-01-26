package br.com.algamoneyapifinal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration //Classe de configuração.
@EnableAuthorizationServer //Habilita a segurança. Essa anotação já vem com o @Configuration, portanto não é obrigatório colocá-la no código Só se quiser.
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{ //com o WebSecurityConfigurerAdapter, ganha-se alguns métodos p/ customizar a configuração.
	
	@Autowired
	private AuthenticationManager authenticationManager; //Vai gerenciar a autenticação pegando o usuário e a senha qque estão cadastrados na ResourceServerConfig (.withUser("admin").password("admin")).

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception { //Configura a aplicação, que é o Client, que é quem o usuário está usando. Eu vou autorizar esse Client usar o Authorization Server. 
		clients.inMemory() //O Client pode ficar em memória ou em jdbc, no banco de dados.
		.withClient("angular") //qual é o nome do Client
		.secret("@ngul@r0") //qual é a senha do Client
		.scopes("read", "write") //uma String define o escopo do cliente. Com ele, pode-se evitar o acesso do cliente angular.Podem ser definidos escopos != p/ clientes !=. O tratamento dessas Strings é feito nos métodos.
		.authorizedGrantTypes("password") //Tipos de concessão autorizados-O usuário digita usuário e senha na tela do Angular e o Angular no JavaScript recebe esse usuário e senha e envia p/ pegar o Access Token.O Angular tem acesso ao usuário e à senha. Isso só é feito qdo. se tem extrema confiança na aplicação. Como a aplicação Angular será feita no curso, essa é a configuração ideal p/ utilizar.
		.accessTokenValiditySeconds(1800); //Qtos. segundos o token ficará ativo. Nesse caso, será 30 minutos.
		super.configure(clients);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception { //Recebe o AuthorizationServerEndpointsConfigurer
		endpoints
			.tokenStore(tokenStore()) //O Token será armazendo em um Token Store. Porque a aplicação Angular vai buscar o Token e vai mandá-lo de volta p/ conseguir acessar a API(/lancamentos).O Token tem que estar armazenado em algum lugar, p/ saber se ele é válido ou inválido.
			.accessTokenConverter(accessTokenConverter())
			.authenticationManager(authenticationManager); //Para validar o usuário e a senha e ver se está tudo certo.
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() { //Onde o Token será armazenado.
		return new JwtTokenStore(accessTokenConverter());
	}


}
