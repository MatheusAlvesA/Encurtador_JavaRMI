package Encurtador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * A classe cliente tem a função de iteragir com o usuário final
 * Esta classe foi implementada com métodos estáticos pois não há nescessidade de instancia-la
 * */
public class Cliente {

	/*
	 * Encurtar tem a finalidade de receber do usuário via console qual a url a ser encurtada
	 * E exibir, também no console a versão curta dessa URL 
	 * */
	public static void encurtar(ServerFachada servidor, Scanner leitor) {
		
		System.out.println("Insira a URL a ser encurtada: ");
		try {
			
			String encurtada = servidor.encurtar(leitor.next());
			System.out.println("Sua URL encurtada é: "+encurtada);
			
		} catch (RemoteException e) {
			System.out.println("Não foi possível encurtar ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida não é válida");
		}
	}

	/*
	 * Esta função insere automaticamente 10 urls para serem encurtadas
	 * Útil para debug
	 * */
	public static void popularBanco(ServerFachada servidor) {
		
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
			
		} catch (RemoteException e) {
			System.out.println("Não foi possível encurtar ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida não é válida");
		}
	}
	
	/*
	 * Esta função executa o processo inverso da função de encurtar URL
	 * 
	 * Solicita ao usuário a URL encurtada previamente e exibe no console a URL original
	 * */
	public static void desEncurtar(ServerFachada servidor, Scanner leitor) {
		
		System.out.println("Insira a URL encurtada: ");
		try {
			
			String longa = servidor.desEncurtar(leitor.next());
			if(longa != null)
				System.out.println("Sua URL original é: "+longa);
			else
				System.out.println("A URL informada não foi encontrada no banco");
			
		} catch (RemoteException e) {
			System.out.println("Não foi possível desencurtar ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida não é válida");
		}
	}

	/*
	 * Esta é uma função simples que exibe o número de URLs encurtadas até agora
	 * */
	public static void total(ServerFachada servidor) {
		try {
			System.out.println("No total foram encurtadas "+servidor.totalEncurtadas()+" URLs");
		} catch (RemoteException e) {
			System.out.println("Falha ao obter a informação ("+e.getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
		}
	}

	/*
	 * Dada uma URL previamente encurtada essa função vai solicitar ao servidor que a apague do banco
	 * */
	public static void remover(ServerFachada servidor, Scanner leitor) {
		
		System.out.println("Insira a URL encurtada: ");
		try {
			
			if(servidor.remover(leitor.next()))
				System.out.println("Sua URL encurtada foi apagada: ");
			else
				System.out.println("A URL informada não foi encontrada no banco");

		} catch (RemoteException e) {
			System.out.println("Não foi possível remover ("+e.getCause().getMessage()+")");
			System.out.println("Verifique o log de erros do servidor");
		}
		catch (InputMismatchException e) {
			System.out.println("A string inserida não é válida");
		}
	}
	
	/*
	 * A função main não instancia a classe pois a mesma foi construida para ser estática
	 * */
	public static void main(String[] args) {
			
			System.out.println("Bem vindo(a) ao sistema de encurtamento de URLs");
			
			Scanner leitor = new Scanner(System.in);
			ServerFachada servidor = null;

			try {
				servidor = (ServerFachada) Naming.lookup("rmi://localhost/encurtador");
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				System.out.println("O Servidor recusou a conexão ("+e.getMessage()+")");
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
					System.err.println("Falha na leitura do número inserido");
					leitor.nextLine();
					opcao = 10; // Opção inválida de propósito
				}

				switch (opcao) {
					case 0: break; // Sair do loop
					case 1:
						Cliente.encurtar(servidor, leitor);
						break;
					case 2:
						Cliente.desEncurtar(servidor, leitor);
						break;
					case 3:
						Cliente.total(servidor);
						break;
					case 4:
						Cliente.remover(servidor, leitor);
						break;
					case 5:
						Cliente.popularBanco(servidor);
						break;
						
					case 9:
						try {servidor.desligar();}
						catch (Exception e) { // Vai lançar uma exceção pela queda na conexão
							System.out.println("Servidor desligado");
							opcao = 0; // Terminando loop
						}
						
						break;
					default:
						System.out.println("Opção inválida");
				}
			}
			
			leitor.close();
			System.out.println("\nAté a próxima");
	}

}
