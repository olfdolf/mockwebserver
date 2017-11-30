package handler;

import java.io.IOException;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import repository.LogRepository;
import repository.MappedResponse;
import repository.MappedResponseRepository;

public class RestApiHandler implements Handler {
	private MappedResponseRepository mappedResponseRepository;
	private LogRepository logRepository;
	private ObjectMapper jsonObjectMapper = new ObjectMapper();
	
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

	private void replyWithJson(HttpExchange exchange, Object object) throws IOException {
		exchange.getResponseHeaders().add("content-type", "application/json;charset=utf-8");
		byte[] output = jsonObjectMapper.writeValueAsBytes(object);
		exchange.sendResponseHeaders(200, output.length);
		exchange.getResponseBody().write(output);
		exchange.getResponseBody().flush();
		exchange.getResponseBody().close();
	}
	
	private String getLastPartOfURI(java.net.URI uri) {
		String uriString = uri.toString();
		return uriString.substring(uriString.lastIndexOf('/')+1);
	}
}
