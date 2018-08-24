package Encurtador;

public class BancoException extends Exception {

	private static final long serialVersionUID = 1L;

	private String mensagem;
	private Exception causadora;
	
	public BancoException(Exception e, String mensagem) {
		super(e);
		this.causadora = e;
		this.mensagem = mensagem;
	}
	
	public BancoException(String mensagem) {
		super();
		this.causadora = null;
		this.mensagem = mensagem;
	}
	
	public BancoException(Exception e) {
		super(e);
		this.causadora = e;
		this.mensagem = null;
	}
	
	public Exception getCausa() {return this.causadora;}
	public String getMensagem() {return this.mensagem;}
}
