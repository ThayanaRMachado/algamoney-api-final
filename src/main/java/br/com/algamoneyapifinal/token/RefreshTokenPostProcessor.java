package br.com.algamoneyapifinal.token; //Pacote Token p/ saber q/ estou trabalhando com um Token.

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice //Todas as controladoras tem acesso à classe.
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{//A classe RefreshTokenPostProcessor é um processador depois do Refresh token ter sido criado.** A interface ResponseBodyAdvice intercepta um processador q/pega a requisição antes de ser escrita de volta p/ quem chamou. O OAuth2AccessToken é o tipo de dado q/ eu quero que seja interceptado qdo. estiver voltando.

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken"); //Ele retorna true qdo. o nome do método é postAccessToken.
	}

	@Override //Esse método só será executado qdo. o método supports retornar true. Aqui vai ficar a regra que eu quero.
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body; //Faz um casting do body p/ um objeto DefaultOAuth2AccessToken. Porque nesse objeto eu tenho o método setRefreshToken, q/ vai tirar o refresh Token do body.
		
		String refreshToken = body.getRefreshToken().getValue(); //Nesse momento o Refresh Token está na String refreshToken.
		adicionarRefreshTokenNoCookie(refreshToken, req, resp); //Manda os 3 atributos p/ serrem adicionados ao Cookie.
		removerRefreshTokenDoBody(token); //Remove o Refresh Token do body passando o token.
		
		return body; //Retorna o body.
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null); //Retira o Refresh Token do body.
	}
	
	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {		
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken); //Cria o Cookie chamado refreshToken.
		refreshTokenCookie.setHttpOnly(true); //O Cookie só é acessível via Http.
		refreshTokenCookie.setSecure(false); //O Cookie não deve funcionar apenas en Https por enquanto (em desenvolvimento). Qdo. estiver em produção, vai mudar p/ true.
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token"); //P/ qual caminho esse Cookie deve ser enviado pelo browser.Se tiver algum contextPath da aplicação, ele concatena com o /oauth/token.
		refreshTokenCookie.setMaxAge(2592000); //Em qto. tempo o Cookie expira em dias. 
		resp.addCookie(refreshTokenCookie); //Adiciona o Cookie na resposta.
	}

}
