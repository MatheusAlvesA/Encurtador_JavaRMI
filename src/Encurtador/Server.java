package Encurtador;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerFachada {

	private final boolean avisarErrosConsole = true; // Se deve ou não exibir os erros no console
	public final String ServerURL = "short.com.br"; // A URL onde o servidor está hospedado
	private Encurtador encurtador;
	
	protected Server() throws RemoteException {
		super();
		
		try {
			
			this.encurtador = new Encurtador(this.ServerURL);
			
		} catch (BancoException e) {
			this.logarErro(e);
			throw new RemoteException("Falha ao instanciar o encurtador");
		}
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String encurtar(String URLlonga) throws RemoteException {
		try {
			return this.encurtador.encurtar(URLlonga);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new RemoteException("Falha ao encurtar a URL");
		} catch (MalformedURLException e) {
			this.logarErro(e);
			throw new RemoteException("URL mal formada");
		}
	}

	@Override
	public String desEncurtar(String URLcurta) throws RemoteException {
		try {
			return this.encurtador.desEncurtar(URLcurta);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new RemoteException("Falha ao encurtar a URL");
		}
	}

	@Override
	public boolean remover(String URLcurta) throws RemoteException {
		try {
			return this.encurtador.remover(URLcurta);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new RemoteException("Falha ao encurtar a URL");
		}
	}

	@Override
	public int totalEncurtadas() throws RemoteException {
		return this.encurtador.totalEncurtado();
	}

	
	/*
	 * Esta função escreve no arquivo de logs os erros ocorridos
	 * */
	private void logarErro(Exception ex) {
		String nomeArquivoLog = "log.txt";
		if(this.avisarErrosConsole) System.err.println("Um erro ocorreu!");
		try(FileWriter f = new FileWriter(new File (nomeArquivoLog))) {
			StackTraceElement[] stack = ex.getStackTrace();
			String gravar = ex.getMessage();
			for (StackTraceElement element : stack) {
				gravar += "\r\n#######\r\n";
				gravar += "Classe: "+element.getClassName()+";\r\n";
				gravar += "Arquivo: "+element.getFileName()+";\r\n";
				gravar += "Metodo: "+element.getMethodName()+";\r\n";
				gravar += "Linha: "+element.getLineNumber()+";\r\n";
			}
			f.write(gravar);
			if(this.avisarErrosConsole) System.err.println("Arquivo de log criado: "+nomeArquivoLog);
		} catch(Exception e) {
			String causa = e.getCause().getMessage();
			if(this.avisarErrosConsole) System.err.println("Não foi posível criar o arquivo de log: "+nomeArquivoLog);
			if(this.avisarErrosConsole) System.err.println(causa);
		}
	}
	
	public static void main(String[] args) {
		try {
			
			Server servidor = new Server();
			
			System.out.println("Iniciando servidor encurtador de URLs");
			LocateRegistry.createRegistry(1099);
			Naming.rebind("rmi://localhost/encurtador", servidor);
			System.out.println("Servidor Online!");
			
			//Debug testes
			System.out.println(servidor.encurtar( "http://www.facebook.com.br" ));
			System.out.println(servidor.totalEncurtadas());
		}
		catch (RemoteException e) {e.printStackTrace();}
		catch (MalformedURLException e) {e.printStackTrace();}
	}

}
