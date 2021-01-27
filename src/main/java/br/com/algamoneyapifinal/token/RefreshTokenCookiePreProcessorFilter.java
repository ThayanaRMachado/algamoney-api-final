package br.com.algamoneyapifinal.token;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component //Coomponente do Spring
@Order(Ordered.HIGHEST_PRECEDENCE) //É um filtro com uma prioridade muito alta. Porque a requisição tem q/ ser analisada antes de todos.Se for uma requisição que tem o cookie, ela tem que adicionada na requisição p/ a aplicação funcionar.
public class RefreshTokenCookiePreProcessorFilter implements Filter{ //Implementa um filtro comum.

	@Override //O filtro pega o refresh token e coloca nos parâmetros da requisição. 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request; //Pega o request e faz um casting para HttpServletRequest.
		//Se acontecer o que está no if, é para substituir a requisição.
		if ("/oauth/token".equalsIgnoreCase(req.getRequestURI()) //Verificar se a requisição é para /oauth/token.Se for, já tem uma possibilidade de ter um Refresh Token no Cookie.
				&& "refresh_token".equals(req.getParameter("grant_type")) //Eu tenho um parâmetro grant_type com o parâmetro refresh_token?. O Refresh Token q/ está no cookie, só será utilizado qdo. o grant_type for refresh_token.
				&& req.getCookies() != null) {  //Se tem algum Cookie presente, tem um Refresh Token.
			String refreshToken = Stream.of(req.getCookies()) //Transforma o array de cookies em um Strem, com o comando Stream.of(...)
					.filter(cookie -> "refreshToken".equals(cookie.getName())) //Filtra os dados do Stream para que retorne apenas o que tenha o nome refreshToken
					.findFirst() //Obtem o primeiro objeto do Stream (caso exista)
					.map(cookie -> cookie.getValue()) //Entra no objeto, pega o cookie e transforma em uma String com o seu valor.
					.orElse(null); //Caso não tenha encontrado um cookie com o nome refreshToken, retorna null.
			
			req = new MyServletRequestWrapper(req, refreshToken); //Passa a requisição e o refreshToken
		}//Depois do if, tem que continuar a cadeia do filtro.
		chain.doFilter(req, response); //passa o req e o response na requisição que foi substituída.
	}
	
	static class MyServletRequestWrapper extends HttpServletRequestWrapper { //A classe é o Wrapper do ServletRequest.
		
		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) { //No construtor recebe um request e um refreshtoken para ser salvo na String refreshToken.
			super(request);
			this.refreshToken = refreshToken;
		}
		
		public Map<String, String[]> getParameterMap() { //Qdo. a aplicação precisar do ParameterMap, para pegar os parâmetros, será criado um novo ParameterMap.
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap()); //O novo ParameterMap será iniciado com o getRequest.getParameterMap.
			map.put("refresh_token", new String[] { refreshToken}); //Usa o nome usado pelo Spring Security oauth2 p/ liberar o refresh token, seguido do valor que é refreshToken.
			map.setLocked(true); //trava o mapa na requisição
			return map; //retorna o map.
		}
	}

}
