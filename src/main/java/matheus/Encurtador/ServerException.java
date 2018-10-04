package matheus.Encurtador;

public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;
	private String mensagem;
	private Exception causadora;
	
	public ServerException(Exception e, String mensagem) {
		super(e);
		this.causadora = e;
		this.mensagem = mensagem;
	}
	
	public ServerException(String mensagem) {
		super();
		this.causadora = null;
		this.mensagem = mensagem;
	}
	
	public ServerException(Exception e) {
		super(e);
		this.causadora = e;
		this.mensagem = null;
	}
	
	public Exception getCausa() {return this.causadora;}
	public String getMessage() {return this.mensagem;}
}
