package handler;

import java.io.IOException;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import repository.LogRepository;
import repository.MappedResponse;
import repository.MappedResponseRepository;

/**
 * Recieves and generates JSON responses based on URI. The Handler modifies/reads the repositories
 * assign to it.
 * For example the URI "/list/mappings" will return all the mappings made to the client in JSON format
 */
public class RestApiHandler implements Handler {
	private MappedResponseRepository mappedResponseRepository;
	private LogRepository logRepository;
	private ObjectMapper jsonObjectMapper = new ObjectMapper();

	/**
	 * Creates the RestApiHandler with specific repositories for logs and mappedresponses.
	 * @param mappedResponseRepository The repository that has all the user mapped responses
	 * @param logRepository The repository that has all the logs
	 */
	public RestApiHandler(MappedResponseRepository mappedResponseRepository, LogRepository logRepository) {
		this.mappedResponseRepository = mappedResponseRepository;
		this.logRepository = logRepository;
	}

	@Override
	public RouteCollection configureRoutes(RouteCollection routeCollection) {
		routeCollection.mapRoute("/list/mappings").addData("action","listmappings");
		routeCollection.mapRoute("/list/logs").addData("action","listlogs");
		routeCollection.mapRoute("/remove/mapping/\\d+").addData("action","removemapping");
		routeCollection.mapRoute("/add/mapping").addData("action","addmapping");
		routeCollection.mapRoute("/get/mapping/\\d+").addData("action","getmapping");
		routeCollection.mapRoute("/edit/mapping").addData("action","editmapping");
		return routeCollection;
	}
	
	@Override
	public boolean handle(HttpExchange exchange, Route route) throws IOException, SQLException  {
		String action = (String)route.getData("action");
		MappedResponse mappedResponse;
		
		switch (action) {
			case "listmappings":
				replyWithJson(exchange, mappedResponseRepository.getAll());
			break;
			case "listlogs":
				replyWithJson(exchange, logRepository.getAll());
			break;
			case "getmapping":
				int idToGet = Integer.parseInt(getLastPartOfURI(exchange.getRequestURI()));	
				replyWithJson(exchange, mappedResponseRepository.get(idToGet));	
			break;
			case "removemapping":
				int idToRemove = Integer.parseInt(getLastPartOfURI(exchange.getRequestURI()));			
				replyWithJson(exchange, mappedResponseRepository.remove(idToRemove));	
			break;
			case "editmapping":
				mappedResponse = jsonObjectMapper.readValue(exchange.getRequestBody(),MappedResponse.class);		
				replyWithJson(exchange, mappedResponseRepository.update(mappedResponse));
			break;
			case "addmapping":
				mappedResponse = jsonObjectMapper.readValue(exchange.getRequestBody(),MappedResponse.class);
				replyWithJson(exchange, mappedResponseRepository.add(mappedResponse));
			break;
			default:
				return false;
		}
		
		return true;
	}

	/**
	 * Converts a object into its json representation and writes it to the responsestream.
	 * @param exchange The http exchange data
	 * @param object The object to convert to json
	 */
	private void replyWithJson(HttpExchange exchange, Object object) throws IOException {
		exchange.getResponseHeaders().add("content-type", "application/json;charset=utf-8");
		byte[] output = jsonObjectMapper.writeValueAsBytes(object);
		exchange.sendResponseHeaders(200, output.length);
		exchange.getResponseBody().write(output);
		exchange.getResponseBody().flush();
		exchange.getResponseBody().close();
	}

	/**
	 * Gets the last part of a URI, for example the URI /olf/dolf/34 will result in 34 being returned.
	 * @param uri The URI to extract the last part from
	 * @return The last part of the URI
	 */
	private String getLastPartOfURI(java.net.URI uri) {
		String uriString = uri.toString();
		return uriString.substring(uriString.lastIndexOf('/')+1);
	}
}
