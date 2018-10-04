package matheus.Encurtador;

import java.net.MalformedURLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Esta classe tem a fun��o de encurtar as URLs do lado do Servidor
 * */
public class Encurtador {
	private Banco banco;
	private String prefixo; // Representa a URL do servidor onde o encurtador est�
	private final int codSize = 5; //Tamanho do c�digo que representa a URL encurtada
	private final String regexURL = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

	public Encurtador(String prefixo) throws BancoException {
		this.banco = new Banco();
		this.prefixo = prefixo;
	}
	
	/*
	 * Dado uma URL retorna a sua vers�o encurtada
	 * 
	 * Lan�a exce��o caso o banco tenha algum problema
	 * */
	public String encurtar(String URLoriginal) throws BancoException, MalformedURLException {
		//Testando se a URL est� bem formada
		if(URLoriginal == null || !this.testarURL(URLoriginal))
			throw new MalformedURLException("A URL n�o � v�lida("+URLoriginal+")");
		
		String existente = banco.buscarValor(URLoriginal);
		// Se j� existe retorna a URL encurtada j� existente
		if(existente != null) return this.prefixo+'/'+existente;
		
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
		String codigo = this.extrairCodigo(URLEncurtada);
		// A chave n�o est� no banco
		if(!banco.temChave(codigo)) return null;
				
		return banco.get(codigo);
	}

	/*
	 * Esta fun��o remove uma URL encurtada do sistema
	 * O unico par�metro de entrada � a URL encurtada
	 * 
	 * Retorna se a remo��o foi um sucesso ou false caso contr�rio
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
	 * Esta fun��o retorna o n�mero de URLs encurtadas
	 */
	public int totalEncurtado() {
		return this.banco.size();
	}
	
	/*
	 * Esta fun��o retorna uma string de 'codSize' caracteres alphanumericos aleat�rios
	 * */
	private String getCodigo() {
		String cod = ""; // Inicializando
		for(int x = 0; x < this.codSize; x++) // Um loop de codSize vezes
			cod += this.getAlphanumerico(); // Acrescentando um charactere a cada itera��o

		return cod;
	}
	
	/*
	 * Esta � uma fun��o interna que retorna um caractere alphanumerico aleat�rio
	 * */
	private char getAlphanumerico() {
		Random gerador = new Random();
		int gerado = gerador.nextInt(62);
		
		if(gerado < 10) gerado += 48; 	   // � um n�mero
		else if(gerado < 36) gerado += 55; // � um caractere maiusculo
		else gerado += 61;				   // � um caractere menusculo
		
		return (char) gerado;
	}
	
	/*
	 * Esta fun��o aplica uma expre��o regular para verificar se esta � uma URL v�lida
	 * */
	private boolean testarURL(String URL) {
        Pattern pattern = Pattern.compile(this.regexURL);
        Matcher matcher = pattern.matcher(URL);
        
        return matcher.matches();
	}
	
	/*
	 * Dada uma URL encurtada pelo sistema essa fun��o retorna seu c�digo
	 * Ex: https://teste.com/hGt4s -> hGt4s
	 * */
	private String extrairCodigo(String URLEncurtada) {
		String[] cods = URLEncurtada.split("/");
		String codigo = cods[cods.length-1];
		
		return codigo;
	}
}
