package Encurtador;

import java.net.MalformedURLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Esta classe tem a função de encurtar as URLs do lado do Servidor
 * */
public class Encurtador {
	private Banco banco;
	private String prefixo; // Representa a URL do servidor onde o encurtador está
	private final int codSize = 5; //Tamanho do código que representa a URL encurtada
	private final String regexURL = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

	public Encurtador(String prefixo) throws BancoException {
		this.banco = new Banco();
		this.prefixo = prefixo;
	}
	
	/*
	 * Dado uma URL retorna a sua versão encurtada
	 * 
	 * Lança exceção caso o banco tenha algum problema
	 * */
	public String encurtar(String URLoriginal) throws BancoException, MalformedURLException {
		//Testando se a URL está bem formada
		if(URLoriginal == null || !this.testarURL(URLoriginal))
			throw new MalformedURLException("A URL não é válida("+URLoriginal+")");
		
		String existente = banco.buscarValor(URLoriginal);
		// Se já existe retorna a URL encurtada já existente
		if(existente != null) return this.prefixo+'/'+existente;
		
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
		String codigo = this.extrairCodigo(URLEncurtada);
		// A chave não está no banco
		if(!banco.temChave(codigo)) return null;
				
		return banco.get(codigo);
	}

	/*
	 * Esta função remove uma URL encurtada do sistema
	 * O unico parâmetro de entrada é a URL encurtada
	 * 
	 * Retorna se a remoção foi um sucesso ou false caso contrário
	 */
	public boolean remover(String URLEncurtada) throws BancoException {
		// Extraindo o codigo
		String chave = this.extrairCodigo(URLEncurtada);
		
		// Testando se a chave existe
		if(chave == null || !this.banco.temChave(chave)) return false;
		
		// Removendo a url encurtada
		this.banco.remove(chave);
		
		return true;
	}
	
	/*
	 * Esta função retorna o número de URLs encurtadas
	 */
	public int totalEncurtado() {
		return this.banco.size();
	}
	
	/*
	 * Esta função retorna uma string de 'codSize' caracteres alphanumericos aleatórios
	 * */
	public String getCodigo() {
		String cod = ""; // Inicializando
		for(int x = 0; x < this.codSize; x++) // Um loop de codSize vezes
			cod += this.getAlphanumerico(); // Acrescentando um charactere a cada iteração

		return cod;
	}
	
	private boolean testarURL(String URL) {
        Pattern pattern = Pattern.compile(this.regexURL);
        Matcher matcher = pattern.matcher(URL);
        
        return matcher.matches();
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
	
	/*
	 * Dada uma URL encurtada pelo sistema essa função retorna seu código
	 * Ex: https://teste.com/hGt4s -> hGt4s
	 * */
	private String extrairCodigo(String URLEncurtada) {
		String[] cods = URLEncurtada.split("/");
		String codigo = cods[cods.length-1];
		
		return codigo;
	}
}
