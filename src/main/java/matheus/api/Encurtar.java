package matheus.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import matheus.Encurtador.ServerFachada;
import matheus.Encurtador.Server;
import matheus.Encurtador.ServerException;
/*
 * Esta classe representa a API
 * */
@Path("encurtar")
public class Encurtar {

	public Encurtar() {
		try {
			ServerFachada servidor = (ServerFachada) new Server("encurtador.matheusalves.com.br");
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        return "{}";
    }
}
