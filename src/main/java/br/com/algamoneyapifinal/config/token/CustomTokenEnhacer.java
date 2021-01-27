package br.com.algamoneyapifinal.config.token;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import br.com.algamoneyapifinal.security.UsuarioSistema;

public class CustomTokenEnhacer implements TokenEnhancer {//Essa classe vai melhorar o token. Para isso ela precisa implementar a interface TokenEnhancer.

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) { //O método já recebe a autenticação
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal(); //Com a autenticação é possível fazer um getPrincipal para pegar o usuário logado.
		
		Map<String, Object> addInfo = new HashMap<>(); //Cria um apa de Object p/ passar as informações q/ serão adicionadas no token. 
		addInfo.put("nome", usuarioSistema.getUsuario().getNome());//Pega o nome do usuário e adiciona no token. Adiciona o atributo nome e coloca o nome do usuário.
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo); //Chama o método setAdditionalInformatio passando o mapa.
		return accessToken; //E retorna o Access Token.
	}

}
