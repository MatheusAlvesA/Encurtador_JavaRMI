package matheus.Encurtador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * A classe cliente tem a fun��o de iteragir com o usu�rio final
 * Esta classe foi implementada com m�todos est�ticos pois n�o h� nescessidade de instancia-la
 * */
public class Cliente {

	/*
	 * Encurtar tem a finalidade de receber do usu�rio via console qual a url a ser encurtada
	 * E exibir, tamb�m no console a vers�o curta dessa URL 
	 * */
	public static Boolean encurtar(ServerFachada servidor, Scanner leitor) {
		
		System.out.println("Insira a URL a ser encurtada: ");
		try {
			
			String encurtada = servidor.encurtar(leitor.next());
			System.out.println("Sua URL encurtada �: "+encurtada);
			
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida n�o � v�lida");
			return false;
		}
		catch (Exception e) {
			System.out.println("N�o foi poss�vel encurtar ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
		}
		
		return true;
	}

	/*
	 * Esta fun��o insere automaticamente 10 urls para serem encurtadas
	 * �til para debug
	 * */
	public static Boolean popularBanco(ServerFachada servidor) {
		
		try {
			
			System.out.println("facebook.com.br -> "+servidor.encurtar("facebook.com.br"));
			System.out.println("twitter.com.br -> "+servidor.encurtar("twitter.com.br"));
			System.out.println("sigaa.ufrn.br -> "+servidor.encurtar("sigaa.ufrn.br"));
			System.out.println("ru.ufrn.br -> "+servidor.encurtar("ru.ufrn.br"));
			System.out.println("steampowered.com -> "+servidor.encurtar("steampowered.com"));
			System.out.println("stackoverflow.com -> "+servidor.encurtar("stackoverflow.com"));
			System.out.println("https://www.instagram.com -> "+servidor.encurtar("https://www.instagram.com"));
			System.out.println("https://www.twitch.tv/ -> "+servidor.encurtar("https://www.twitch.tv/"));
			System.out.println("www.twitch.tv -> "+servidor.encurtar("www.twitch.tv"));
			System.out.println("orkut.com.br -> "+servidor.encurtar("orkut.com.br"));
			Cliente.total(servidor);
			
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida n�o � v�lida");
			return true;
		}
		catch (Exception e) {
			System.out.println("N�o foi poss�vel encurtar ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
			return false;
		}
		return true;
	}
	
	/*
	 * Esta fun��o executa o processo inverso da fun��o de encurtar URL
	 * 
	 * Solicita ao usu�rio a URL encurtada previamente e exibe no console a URL original
	 * */
	public static Boolean desEncurtar(ServerFachada servidor, Scanner leitor) {
		
		System.out.println("Insira a URL encurtada: ");
		try {
			
			String longa = servidor.desEncurtar(leitor.next());
			if(longa != null)
				System.out.println("Sua URL original �: "+longa);
			else
				System.out.println("A URL informada n�o foi encontrada no banco");
			
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida n�o � v�lida");
			return true;
		}
		catch (Exception e) {
			System.out.println("N�o foi poss�vel desencurtar ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
			return false;
		}
		return true;
	}

	/*
	 * Esta � uma fun��o simples que exibe o n�mero de URLs encurtadas at� agora
	 * */
	public static Boolean total(ServerFachada servidor) {
		try {
			System.out.println("No total foram encurtadas "+servidor.totalEncurtadas()+" URLs");
		} catch (Exception e) {
			System.out.println("Falha ao obter a informa��o ("+e.getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
			return false;
		}
		return true;
	}

	/*
	 * Dada uma URL previamente encurtada essa fun��o vai solicitar ao servidor que a apague do banco
	 * */
	public static Boolean remover(ServerFachada servidor, Scanner leitor) {
		
		System.out.println("Insira a URL encurtada: ");
		try {
			
			if(servidor.remover(leitor.next()))
				System.out.println("Sua URL encurtada foi apagada: ");
			else
				System.out.println("A URL informada n�o foi encontrada no banco");

		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida n�o � v�lida");
			return true;
		}
		catch (Exception e) {
			System.out.println("N�o foi poss�vel remover ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
			return false;
		}
		return true;
	}
	
	/*
	 * A fun��o main n�o instancia a classe pois a mesma foi construida para ser est�tica
	 * */
	public static void main(String[] args) {
			
			System.out.println("Bem vindo(a) ao sistema de encurtamento de URLs");
			
			Scanner leitor = new Scanner(System.in);
			ServerFachada servidor = null;

			try {
				servidor = (ServerFachada) Naming.lookup("rmi://localhost/encurtador");
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				System.out.println("O Servidor recusou a conex�o ("+e.getMessage()+")");
				System.exit(0);
			}

			
			int opcao = 1;
			while(opcao != 0) {
				System.out.println("\n! Escolha o que fazer a seguir !\n");

				System.out.println("1 - Encurtar URL");
				System.out.println("2 - (Des)Encurtar URL");
				System.out.println("3 - Checar quantas foram encurtadas");
				System.out.println("4 - Remover uma URL encurtada");
				System.out.println("5 - Inserir algumas URLs automaticamente");
				System.out.println("9 - Desligar servidor");
				System.out.println("0 - Sair do Programa");
				System.out.print("\n> ");

				try {
					opcao = leitor.nextInt();
				}
				catch (InputMismatchException e) {
					System.err.println("Falha na leitura do n�mero inserido");
					leitor.nextLine();
					opcao = 10; // Op��o inv�lida de prop�sito
				}
				
				boolean serverOnline = false;

				switch (opcao) {
					case 0: break; // Sair do loop
					case 1:
						serverOnline = Cliente.encurtar(servidor, leitor);
						break;
					case 2:
						serverOnline = Cliente.desEncurtar(servidor, leitor);
						break;
					case 3:
						serverOnline = Cliente.total(servidor);
						break;
					case 4:
						serverOnline = Cliente.remover(servidor, leitor);
						break;
					case 5:
						serverOnline = Cliente.popularBanco(servidor);
						break;
						
					case 9:
						try {servidor.desligar();}
						catch (Exception e) { // Vai lan�ar uma exce��o pela queda na conex�o
							System.out.println("Servidor desligado");
							opcao = 0; // Terminando loop
						}
						
						break;
					default:
						System.out.println("Op��o inv�lida");
				}
				while(!serverOnline) {
					try {
						servidor = (ServerFachada) Naming.lookup("rmi://localhost/encurtador");
						serverOnline = true;
					} catch (MalformedURLException | RemoteException | NotBoundException e) {}
				}
			}
			
			leitor.close();
			System.out.println("\nAt� a pr�xima");
	}

}
