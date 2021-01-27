package br.com.algamoneyapifinal.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha { //Gera uma nova senha encodada.

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("m0b1l30"));
	}
}
