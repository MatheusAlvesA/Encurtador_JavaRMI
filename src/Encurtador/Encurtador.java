package Encurtador;

import java.util.Random;

/*
 * Esta classe tem a função de encurtar as URLs do lado do Servidor
 * */
public class Encurtador {
	private Banco banco;
	private String prefixo; // Representa a URL do servidor onde o encurtador está

	public Encurtador(String prefixo) throws BancoException {
		this.banco = new Banco();
		this.prefixo = prefixo;
	}
	
	/*
	 * Dado uma URL retorna a sua versão encurtada
	 * 
	 * Lança exceção caso o banco tenha algum problema
	 * */
	public String encurtar(String URLoriginal) throws BancoException {
		String existente = banco.buscarValor(URLoriginal);
		// Se já existe retorna a URL encurtada já existente
		if(existente != null) return existente;
		
		String chaveUnica = this.getCodigo();
		// Continue procurando uma chave até que ela não exista no banco
		while(banco.temChave(chaveUnica)) chaveUnica = this.getCodigo();
		
		banco.put(chaveUnica, URLoriginal);// Inserindo no banco
		
		return this.prefixo+'/'+chaveUnica;
	}
	
	/*
	 * Dado uma URL previamente encurtada essa função "desencurta" ela
	 * 
	 * Lança exceção caso o banco tenha algum problema
	 * */
	public String desEncurtar(String URLEncurtada) throws BancoException {
		// Extraindo o codigo
		String[] cods = URLEncurtada.split("/");
		String codigo = cods[cods.length-1];
		// A chave não está no banco
		if(!banco.temChave(codigo)) return null;
				
		return banco.get(codigo);
	}

	/*
	 * Esta função retorna uma string de 5 caracteres alphanumericos
	 * */
	public String getCodigo() {
		String cod = "";
		for(int x = 0; x < 5; x++) {
			cod += this.getAlphanumerico();
		}
		return cod;
	}
	
	/*
	 * Esta é uma função interna que retorna um caractere alphanumerico aleatório
	 * */
	private char getAlphanumerico() {
		Random gerador = new Random();
		int gerado = gerador.nextInt(62);
		
		if(gerado < 10) gerado += 48; 		// É um número
		else if(gerado < 36) gerado += 55; // É um caractere maiusculo
		else gerado += 61;					// É um caractere menusculo
		
		return (char) gerado;
	}
}
