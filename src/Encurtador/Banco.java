package Encurtador;

import Encurtador.BancoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONException;

/*
 * Esta classe abstrai a persistencia dos dados no disco
 */
public class Banco {
	
	private JSONObject dados;						// Interpretador do formato JSON
	private final String fileName = "banco.json";	// Local do arquivo de dados
	
	/*
	 *	O construtor é responsável por instanciar o interpretador de JSON
	 *	e realizar a leitura inicial do banco
	 *
	 *	Dois tipos de exceções podem ocorrer:
	 *		O arquivo não com tem um banco de dados váldo, po estar corrompido ou
	 *		O arquivo não pôde ser lido devido a um problema para abri-lo
	 *	Em ambos os casos BancoException é lançado contendo uma mensagem descritiva e a exceção originária
	 */
	public Banco() throws BancoException {
		try {
			
			this.dados = this.lerBanco();
			
		}
		catch (JSONException e) {throw new BancoException(e, "Falha ao ler o banco, o arquivo deve estar corrompido");}
		catch (IOException e) {throw new BancoException(e, "Falha ao ler o arquivo, verifique as permissões de acesso");}
	}
	
	/*
	 * Esta função insere dados no banco
	 * 
	 * Recebe como parâmetro a chave (String)
	 * Retorna o valor (string) associado
	 * 
	 * Complexidade O(1)
	 */
	public String get(String chave) throws BancoException {
		if(chave == null) // Não faz sentido a chave ser nula
			throw new BancoException("A chave não pode ser nula");
		
		String valor = "";
		try {
			
			valor = (String) this.dados.get(chave); // obtendo valor e transformando em string
			
		} catch (JSONException e) {
			throw new BancoException(e, "A chave não foi encontrada no banco");
		} catch (ClassCastException e) {
			throw new BancoException(e, "O valor armazenado na chave '"+chave+"' não é uma String válida");
		}
		
		return valor;
	}
	
	/*
	 * Esta função insere dados no banco 
	 * 
	 * Chave e valor a serem inseridos devem ser sempre do tipo string
	 * 
	 * Complexidade O(1)
	 */
	public void put(String chave, String valor) throws BancoException {
		if(chave == null || valor == null)
			throw new BancoException("A chave e/ou valor não podem ser nulos");

		try {
			this.dados.put(chave, valor);
			this.gravarBanco();
		}
		catch (IOException e) {
			throw new BancoException(e, "Falha ao gravar o banco em disco");
		}
	}

	/*
	 * Esta função busca por um valor no banco e retorna sua chave caso encontre
	 * 
	 * Recebe como parâmetro o valor (String)
	 * Retorna a chave (string) associada ou null caso não encontre
	 * 
	 * Complexidade O(n). Com n = número de elementos no banco
	 */
	public String buscarValor(String valor) throws BancoException {
		// Recebendo um terador com todas as chaves que existem no banco
		Iterator<String> iteradorChaves = this.dados.keys();
		
		// Inicializando uma lista de strings para guardar as chaves
		ArrayList<String> listaChaves = new ArrayList<String>();
		
		//Iterando e convertendo o iterador em uma lista
		while(iteradorChaves.hasNext()) { listaChaves.add(iteradorChaves.next());}
		
		// Percorrendo a lista de chaves em busca do valor
		for(int loop = 0; loop < listaChaves.size(); loop++)
			if(this.dados.get(listaChaves.get(loop)) == valor)
				return listaChaves.get(loop);
		
		return null;
	}
	
	/*
	 * Esta função Verifica a existencia de uma chave no banco
	 * 
	 * Recebe como parâmetro a chave (String)
	 * Retorna se a chave está presente
	 * 
	 * Complexidade O(1)
	 */
	public boolean temChave(String chave) {
		if(chave == null) // Não faz sentido a chave ser nula
			return false;

		try {
			this.get(chave);
		} catch (BancoException e) {return false;}
		
		return true;
	}
	
	/*
	 * Esta função remove o valor associado a chave passada no banco
	 * 
	 * Recebe como parâmetro a chave (String)
	 * 
	 * Complexidade O(1)
	 */
	public void remove(String chave) throws BancoException {
		if(chave == null) // Não faz sentido a chave ser nula
			throw new BancoException("A chave não pode ser nula");

		this.dados.remove(chave); // Removendo do banco
		try {
			this.gravarBanco();
		} catch (IOException e) {throw new BancoException(e, "Falha ao gravar o banco em disco");}
	}
	
	/*
	 * Essa função é responsável por ler os dados do arquivo "fileName" e guardalos em "dados"
	 * */
	private JSONObject lerBanco() throws JSONException,  IOException {
		// Criando uma referencia para o arquivo
		File fileBanco = new File(this.fileName);

		// Alocando espaço para os dados
		byte[] data = new byte[(int) fileBanco.length()];
		
		// Os dados brutos em string que ainda serão lidos
		String bruto = "{}";
		
		// Um try with resources vai evitar problemas para fechar recursos 
		try (FileInputStream fis = new FileInputStream(fileBanco)) {
			
			fis.read(data);
			
		} catch (FileNotFoundException e1) {
			// Se o banco ainda não está no disco ele será criado

			FileWriter arquivo = new FileWriter(fileBanco);
			arquivo.write(bruto);
			arquivo.close();

			return new JSONObject(bruto); // O banco acabou de ser criado, nada está armazenado
		}
		
		bruto = new String(data, "UTF-8"); // UTF-8 é o padrão para JSON
 
		return new JSONObject(bruto);
	}

	/*
	 * Essa função realiza a gravação dos dados em disco
	 * */
	private void gravarBanco() throws IOException {
		// Checando se dados está inicializado corretamente
		if(this.dados == null) return;
		
		// Usando try with resources para evitar problemas para fechar recursos 
		try (FileWriter arquivo = new FileWriter(new File(this.fileName))) {
			arquivo.write(this.dados.toString());
		}
	}
}
