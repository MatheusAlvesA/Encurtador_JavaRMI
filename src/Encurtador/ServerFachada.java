package Encurtador;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * Esta interface abstrai as fun��es b�sicas que o servidor deve ter
 * */
public interface ServerFachada extends Remote {
	
	// Esta fun��o retorna a URL passada por par�metro encurtada
	public String encurtar(String URLlonga) throws RemoteException;
	
	// Esta fun��o retorna a URL encurtada passada por par�metro (DES)encurtada
	public String desEncurtar(String URLcurta) throws RemoteException;
	
	// Dada uma URL previamente encurtada, desencurta a URL
	public boolean remover(String URLcurta) throws RemoteException;
	
	// Retorna quantas URL encurtadas est�o armazenadas no banco
	public int totalEncurtadas() throws RemoteException;
}
