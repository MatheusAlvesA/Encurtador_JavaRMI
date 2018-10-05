package matheus.Encurtador;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;


public class Server implements ServerFachada {
	
	private final boolean avisarErrosConsole = true; // Se deve ou não exibir os erros no console
	private String ServerURL; // A URL onde o servidor está hospedado
	private Encurtador encurtador;
	
	public Server(String endereco) throws ServerException {
		try {
			
			this.ServerURL = endereco;
			this.encurtador = new Encurtador(this.ServerURL);
			
		} catch (BancoException e) {
			this.logarErro(e);
			throw new ServerException("Falha ao instanciar o encurtador");
		}
	}

	@Override
	public String encurtar(String URLlonga) throws ServerException {
		try {
			return this.encurtador.encurtar(URLlonga);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new ServerException("Falha ao encurtar a URL");
		} catch (MalformedURLException e) {
			this.logarErro(e);
			throw new ServerException("URL mal formada");
		}
	}

	@Override
	public String desEncurtar(String URLcurta) throws ServerException {
		if(URLcurta == null || URLcurta.equals(""))
			throw new ServerException("A URL não pode estar vazia");

		try {
			String longa =  this.encurtador.desEncurtar(URLcurta);
			if(longa == null)
				throw new ServerException("A URL encurtada não foi encontrada, verifique a URL informada");
			return longa;
		} catch (BancoException e) {
			this.logarErro(e);
			throw new ServerException("Falha ao desencurtar a URL");
		}
	}

	@Override
	public boolean remover(String URLcurta) throws ServerException {
		try {
			return this.encurtador.remover(URLcurta);
		} catch (BancoException e) {
			this.logarErro(e);
			throw new ServerException("Falha ao remover a URL");
		}
	}

	@Override
	public int totalEncurtadas() throws ServerException {
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
			String causa = "Causa desconhecida";
			if(e.getCause() != null)
				causa = e.getCause().getMessage();
			if(this.avisarErrosConsole) System.err.println("N�o foi pos�vel criar o arquivo de log: "+nomeArquivoLog);
			if(this.avisarErrosConsole) System.err.println(causa);
		}		
	}
	
	// Esta função desliga o servidor via comando vindo do cliente
	public void desligar() {
		System.out.println("> Servidor encerrado via cliente <");
		System.exit(0);
	}

}
