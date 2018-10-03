package Encurtador;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;

public class Server implements ServerFachada {
	
	private final boolean avisarErrosConsole = true; // Se deve ou n�o exibir os erros no console
	private String ServerURL; // A URL onde o servidor est� hospedado
	private Encurtador encurtador;
	
	protected Server(String endereco) throws Exception {
		super();
		
		try {
			this.ServerURL = endereco;
			this.encurtador = new Encurtador(this.ServerURL);
			
		} catch (BancoException e) {
			this.logarErro(e);
			throw new Exception("Falha ao instanciar o encurtador");
		}
	}

	@Override
	public String encurtar(String URLlonga) throws Exception {
		try {
			return this.encurtador.encurtar(URLlonga);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new Exception("Falha ao encurtar a URL");
		} catch (MalformedURLException e) {
			this.logarErro(e);
			throw new Exception("URL mal formada");
		}
	}

	@Override
	public String desEncurtar(String URLcurta) throws Exception {
		try {
			return this.encurtador.desEncurtar(URLcurta);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new Exception("Falha ao desencurtar a URL");
		}
	}

	@Override
	public boolean remover(String URLcurta) throws Exception {
		try {
			return this.encurtador.remover(URLcurta);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new Exception("Falha ao remover a URL");
		}
	}

	@Override
	public int totalEncurtadas() throws Exception {
		return this.encurtador.totalEncurtado();
	}

	
	/*
	 * Esta fun��o escreve no arquivo de logs os erros ocorridos
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
			if(this.avisarErrosConsole) System.err.println("N�o foi pos�vel criar o arquivo de log: "+nomeArquivoLog);
			if(this.avisarErrosConsole) System.err.println(causa);
		}		
	}
	
	// Esta fun��o desliga o servidor via comando vindo do cliente
	public void desligar() {
		System.out.println("> Servidor encerrado via cliente <");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		try {
			
			Server servidor = new Server("short.com.br");
			
			System.out.println("Iniciando servidor encurtador de URLs");
			/*
			 * TODO
			 * */
			System.out.println("Servidor Online!");

		}
		catch (Exception e) {e.printStackTrace();System.exit(0);}
	}

}
