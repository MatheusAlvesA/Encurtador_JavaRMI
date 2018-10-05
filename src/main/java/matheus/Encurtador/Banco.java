package matheus.Encurtador;

import matheus.Encurtador.BancoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONException;

/*
 * Esta classe abstrai a persistencia dos dados no disco
 */
public class Banco {
	
	private JSONObject dados;						// Interpretador do formato JSON
	private final String fileName = "banco.json";	// Local do arquivo de dados
	
	/*
	 *	O construtor � respons�vel por instanciar o interpretador de JSON
	 *	e realizar a leitura inicial do banco
	 *
	 *	Dois tipos de exce��es podem ocorrer:
	 *		O arquivo n�o comt�m um banco de dados v�ldo, por estar corrompido ou
	 *		O arquivo n�o p�de ser lido devido a um problema para abri-lo
	 *	Em ambos os casos BancoException � lan�ado contendo uma mensagem descritiva e a exce��o origin�ria
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
	 * Recebe como par�metro a chave (String)
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
	 * Esta fun��o insere dados no banco 
	 * 
	 * Chave e valor a serem inseridos devem ser sempre do tipo string
	 * 
	 * Complexidade O(1)
	 */
	public void put(String chave, String valor) throws BancoException {
		if(chave == null || valor == null)
			throw new BancoException("A chave e/ou valor n�o podem ser nulos");

		try {
			this.dados.put(chave, valor);
			this.gravarBanco();
		}
		catch (IOException e) {
			throw new BancoException(e, "Falha ao gravar o banco em disco");
		}
	}

	/*
	 * Esta fun��o busca por um valor no banco e retorna sua chave caso encontre
	 * 
	 * Recebe como par�metro o valor (String)
	 * Retorna a chave (string) associada ou null caso n�o encontre
	 * 
	 * Complexidade O(n). Com n = n�mero de elementos no banco
	 */
	public String buscarValor(String valor) {
		// Recebendo um terador com todas as chaves que existem no banco
		Iterator<String> iteradorChaves = this.dados.keys();
		
		//Iterando e convertendo o iterador em uma lista
		while(iteradorChaves.hasNext()) {
			String chave = iteradorChaves.next();
			if(valor.equals(this.dados.get(chave)))
				return chave;
		}

		return null;
	}
	
	/*
	 * Esta fun��o Verifica a existencia de uma chave no banco
	 * 
	 * Recebe como par�metro a chave (String)
	 * Retorna se a chave est� presente
	 * 
	 * Complexidade O(1)
	 */
	public boolean temChave(String chave) {
		if(chave == null) // N�o faz sentido a chave ser nula
			return false;

		try {
			this.get(chave);
		} catch (BancoException e) {return false;}
		
		return true;
	}
	
	/*
	 * Esta fun��o retorna o n�mero de entradas armazenadas
	 */
	public int size() {
		Set<String> set = dados.keySet();
		return set.size();
	}
	
	/*
	 * Esta função remove entrada associada a chave passada no banco
	 * 
	 * Recebe como parãmetro a chave (String)
	 * 
	 * Complexidade O(1)
	 */
	public void remove(String chave) throws BancoException {
		if(chave == null) // N�o faz sentido a chave ser nula
			throw new BancoException("A chave não pode ser nula");

		this.dados.remove(chave); // Removendo do banco
		try {
			this.gravarBanco();
		} catch (IOException e) {throw new BancoException(e, "Falha ao gravar o banco em disco");}
	}
	
	/*
	 * Essa fun��o � respons�vel por ler os dados do arquivo "fileName" e guarda-los em "dados"
	 * */
	private JSONObject lerBanco() throws JSONException, IOException {
		// Criando uma referencia para o arquivo
		File fileBanco = new File(this.fileName);

		// Alocando espa�o para os dados
		byte[] data = new byte[(int) fileBanco.length()];
		
		// Os dados brutos em string que ainda ser�o lidos
		String bruto = "{}";
		
		// Um try with resources vai evitar problemas para fechar recursos 
		try (FileInputStream fis = new FileInputStream(fileBanco)) {
			
			fis.read(data);
			
		} catch (FileNotFoundException e1) {
			// Se o banco ainda n�o est� no disco ele ser� criado

			FileWriter arquivo = new FileWriter(fileBanco);
			arquivo.write(bruto);
			arquivo.close();

			return new JSONObject(bruto); // O banco acabou de ser criado, nada est� armazenado
		}
		
		bruto = new String(data, "UTF-8"); // UTF-8 � o padr�o para JSON
 
		return new JSONObject(bruto);
	}

	/*
	 * Essa fun��o realiza a grava��o dos dados em disco
	 * */
	private void gravarBanco() throws IOException {
		// Checando se dados est� inicializado corretamente
		if(this.dados == null) return;
		
		// Usando try with resources para evitar problemas para fechar recursos 
		try (FileWriter arquivo = new FileWriter(new File(this.fileName))) {
			arquivo.write(this.dados.toString());
		}
	}
}
