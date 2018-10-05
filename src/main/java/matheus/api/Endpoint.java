package matheus.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import matheus.Encurtador.ServerFachada;
import matheus.Encurtador.Server;
import matheus.Encurtador.ServerException;
/*
 * Esta classe representa a API
 * */
@Path("/")
public class Endpoint {

	private ServerFachada servidor;
	Map<String, String> mensagensErro;
	
	public Endpoint() {
		try {
			this.servidor = (ServerFachada) new Server("encurtador.matheusalves.com.br");
		} catch (ServerException e) {
			this.servidor = null;
		}
		finally {
			this.mensagensErro = new HashMap<>();
			this.mensagensErro.put("MANUTENCAO", "{\"status\": \"erro\", \"mensagem\": \"O servidor está passando por uma manutenção\"}");
			this.mensagensErro.put("INVALID_REQUEST", "{\"status\": \"erro\", \"mensagem\": \"O corpo da requisição é inválido\"}");
			this.mensagensErro.put("NOT_SUPORTED", "{\"status\": \"erro\", \"mensagem\": \"Essa operação não é suportada\"}");
		}
	}
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response encurtar(String corpo) {
    	if(this.servidor == null) 
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(this.mensagensErro.get("MANUTENCAO")).build();
        
    	JSONObject corpoJSON = null;
    	try {
    		corpoJSON = new JSONObject(corpo);
    		if(corpoJSON.getString("URL") == null)
    			return Response.status(Response.Status.BAD_REQUEST).entity(this.mensagensErro.get("INVALID_REQUEST")).build();
    	}
    	catch(JSONException e) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(this.mensagensErro.get("INVALID_REQUEST")).build();
    	}
    	
    	String curta = null;
    	
    	try {
			curta = this.servidor.encurtar(corpoJSON.getString("URL"));
		}
    	catch (ServerException e) {
    	  return Response.status(Response.Status.NOT_FOUND).entity("{\"status\": \"erro\", \"mensagem\": \""+e.getMessage()+"\"}").build();
    	}
    	
    	return Response.status(Response.Status.OK).entity("{\"status\": \"sucesso\", \"curta\": \""+curta+"\"}").build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response desencurtar(@QueryParam("URL") String curta) {
    	if(this.servidor == null) 
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(this.mensagensErro.get("MANUTENCAO")).build();
        
    	String longa = null;
    	try {
			longa = this.servidor.desEncurtar(curta);
		}
    	catch (ServerException e) {
    		return Response.status(Response.Status.NOT_FOUND).entity("{\"status\": \"erro\", \"mensagem\": \""+e.getMessage()+"\"}").build();
    	}
   
    	return Response.status(Response.Status.OK).entity("{\"status\": \"sucesso\", \"curta\": \""+longa+"\"}").build();
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response PUT() {return this.notSuported();}
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response DELETE() {return this.notSuported();}
    
    private Response notSuported() {
    	return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(this.mensagensErro.get("NOT_SUPORTED")).build();
    }
}
