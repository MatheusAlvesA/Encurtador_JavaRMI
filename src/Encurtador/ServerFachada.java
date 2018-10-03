package Encurtador;

import java.rmi.Remote;

/*
 * Esta interface abstrai as funções básicas que o servidor deve ter
 * */
public interface ServerFachada extends Remote {
	
	// Esta função retorna a URL passada por parâmetro encurtada
	public String encurtar(String URLlonga) throws Exception;
	
	// Esta função (DES)encurta a URL encurtada passada por parâmetro
	public String desEncurtar(String URLcurta) throws Exception;
	
	// Dada uma URL previamente encurtada, remove a URL
	public boolean remover(String URLcurta) throws Exception;
	
	// Retorna quantas URL encurtadas estão armazenadas no banco
	public int totalEncurtadas() throws Exception;
	
	// Encerra o servidor
	public void desligar() throws Exception;;
}
