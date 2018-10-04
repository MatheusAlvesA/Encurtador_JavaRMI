package matheus.Encurtador;

import java.rmi.Remote;

/*
 * Esta interface abstrai as fun��es b�sicas que o servidor deve ter
 * */
public interface ServerFachada extends Remote {
	
	// Esta função retorna a URL passada por par�metro encurtada
	public String encurtar(String URLlonga) throws Exception;
	
	// Esta função (DES)encurta a URL encurtada passada por par�metro
	public String desEncurtar(String URLcurta) throws Exception;
	
	// Dada uma URL previamente encurtada, remove a URL
	public boolean remover(String URLcurta) throws Exception;
	
	// Retorna quantas URL encurtadas estão armazenadas no banco
	public int totalEncurtadas() throws Exception;
	
	// Encerra o servidor
	public void desligar() throws Exception;;
}
