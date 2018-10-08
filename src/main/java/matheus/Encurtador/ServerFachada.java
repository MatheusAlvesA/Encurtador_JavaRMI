package matheus.Encurtador;

/*
 * Esta interface abstrai as funçõe  básicas que o servidor deve ter
 * */
public interface ServerFachada {
	
	// Esta função retorna a URL passada por parâmetro encurtada
	public String encurtar(String URLlonga) throws ServerException;
	
	// Esta função (DES)encurta a URL encurtada passada por parâmetro
	public String desEncurtar(String URLcurta) throws ServerException;
	
	// Dada uma URL previamente encurtada, remove a URL
	public boolean remover(String URLcurta) throws ServerException;
	
	// Retorna quantas URL encurtadas estão armazenadas no banco
	public int totalEncurtadas() throws ServerException;
	
	// Encerra o servidor
	public void desligar() throws ServerException;

}
