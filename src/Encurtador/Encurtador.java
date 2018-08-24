package Encurtador;

import java.util.Random;

/*
 * Esta classe tem a fun��o de encurtar as URLs do lado do Servidor
 * */
public class Encurtador {
	private Banco banco;
	private String prefixo; // Representa a URL do servidor onde o encurtador est�

	public Encurtador(String prefixo) throws BancoException {
		this.banco = new Banco();
		this.prefixo = prefixo;
	}
	
	/*
	 * Dado uma URL retorna a sua vers�o encurtada
	 * 
	 * Lan�a exce��o caso o banco tenha algum problema
	 * */
	public String encurtar(String URLoriginal) throws BancoException {
		String existente = banco.buscarValor(URLoriginal);
		// Se j� existe retorna a URL encurtada j� existente
		if(existente != null) return existente;
		
		String chaveUnica = this.getCodigo();
		// Continue procurando uma chave at� que ela n�o exista no banco
		while(banco.temChave(chaveUnica)) chaveUnica = this.getCodigo();
		
		banco.put(chaveUnica, URLoriginal);// Inserindo no banco
		
		return this.prefixo+'/'+chaveUnica;
	}
	
	/*
	 * Dado uma URL previamente encurtada essa fun��o "desencurta" ela
	 * 
	 * Lan�a exce��o caso o banco tenha algum problema
	 * */
	public String desEncurtar(String URLEncurtada) throws BancoException {
		// Extraindo o codigo
		String[] cods = URLEncurtada.split("/");
		String codigo = cods[cods.length-1];
		// A chave n�o est� no banco
		if(!banco.temChave(codigo)) return null;
				
		return banco.get(codigo);
	}

	/*
	 * Esta fun��o retorna uma string de 5 caracteres alphanumericos
	 * */
	public String getCodigo() {
		String cod = "";
		for(int x = 0; x < 5; x++) {
			cod += this.getAlphanumerico();
		}
		return cod;
	}
	
	/*
	 * Esta � uma fun��o interna que retorna um caractere alphanumerico aleat�rio
	 * */
	private char getAlphanumerico() {
		Random gerador = new Random();
		int gerado = gerador.nextInt(62);
		
		if(gerado < 10) gerado += 48; 		// � um n�mero
		else if(gerado < 36) gerado += 55; // � um caractere maiusculo
		else gerado += 61;					// � um caractere menusculo
		
		return (char) gerado;
	}
}
