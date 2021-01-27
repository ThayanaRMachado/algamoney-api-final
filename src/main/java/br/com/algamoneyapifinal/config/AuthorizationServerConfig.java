package br.com.algamoneyapifinal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception { //Configura a aplicação, que é o Client, que é quem o usuário está usando. Eu vou autorizar esse Client usar o Authorization Server. 
		clients.inMemory() //O Client pode ficar em memória ou em jdbc, no banco de dados.
		.withClient("angular") //qual é o nome do Client
		.secret("$2a$10$G1j5Rf8aEEiGc/AET9BA..xRR.qCpOUzBZoJd8ygbGy6tb3jsMT9G") //Senha do Client encriptada
		.scopes("read", "write") //uma String define o escopo do cliente. Com ele, pode-se evitar o acesso do cliente angular.Podem ser definidos escopos != p/ clientes !=. O tratamento dessas Strings é feito nos métodos.
		.authorizedGrantTypes("password", "refresh_token") //Tipos de concessão autorizados-O usuário digita usuário e senha na tela do Angular e o Angular no JavaScript recebe esse usuário e senha e envia p/ pegar o Access Token.O Angular tem acesso ao usuário e à senha. Isso só é feito qdo. se tem extrema confiança na aplicação. Como a aplicação Angular será feita no curso, essa é a configuração ideal p/ utilizar.**Adicionar o novo granType chamado refresh_token. será possível usar o Refresh Token para fornecer o Access Token.
		.accessTokenValiditySeconds(1800) //Tempo de vida do Access Token.
		.refreshTokenValiditySeconds(3600 * 24) //Tempo de vida do Refresh Token, que será de 1 dia.
		.and() //Adição de um novo Client
		.withClient("mobile") //qual é o nome do Client
		.secret("$2a$10$lHy.UrHOegN7cCaaWx0m0uboVHiyucPJm/z4WZttsjE5x70Uu67JK") //Senha do Client encriptada
		.scopes("read") //Escopo read
		.authorizedGrantTypes("password", "refresh_token") //Tipos de concessão autorizados-O usuário digita usuário e senha na tela do Angular e o Angular no JavaScript recebe esse usuário e senha e envia p/ pegar o Access Token.O Angular tem acesso ao usuário e à senha. Isso só é feito qdo. se tem extrema confiança na aplicação. Como a aplicação Angular será feita no curso, essa é a configuração ideal p/ utilizar.**Adicionar o novo granType chamado refresh_token. será possível usar o Refresh Token para fornecer o Access Token.
		.accessTokenValiditySeconds(1800) //Tempo de vida do Access Token.
		.refreshTokenValiditySeconds(3600 * 24); //Tempo de vida do Refresh Token, que será de 1 dia.
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception { //Recebe o AuthorizationServerEndpointsConfigurer
		endpoints
			.tokenStore(tokenStore()) //O Token será armazendo em um Token Store. Porque a aplicação Angular vai buscar o Token e vai mandá-lo de volta p/ conseguir acessar a API(/lancamentos).O Token tem que estar armazenado em algum lugar, p/ saber se ele é válido ou inválido.
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false) //Sempre q/ pedir um novo Access Token com Refresh Token, um novo Reresh Token será enviado. A ideia é que o usuário não se deslogue da aplicação enquanto estiver usando. Se ele usa a aplicação todos os dias, o Refresh Token não expira e é possível buscar novos Access Tokens p/ usar na API. Se não foi settado como false, o refresh Token vai durar apenas 24 horas.  
			.userDetailsService(this.userDetailsService)
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
