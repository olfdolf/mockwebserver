package handler;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import repository.LogRepository;
import repository.UserDefinedResponse;
import repository.UserDefinedResponseRepository;

public class RestApiHandler implements Handler {
	UserDefinedResponseRepository userDefinedResponseRepository;
	LogRepository logRepository;
	private Mappings mappings;
	
	public RestApiHandler(UserDefinedResponseRepository userDefinedResponseRepository, LogRepository logRepository) {
		this.userDefinedResponseRepository = userDefinedResponseRepository;
		this.logRepository = logRepository;
	}

	@Override
	public Mappings configureMappings() throws Exception {
		if (mappings == null)
			mappings = new RestApiHandlerMappings();
		
		return mappings;
	}
	
	
	@Override
	public void handle(HttpExchange exchange, String mapping) throws Exception {
		
		if (mapping.equals("/list/userdefinedresponse")) {
			replyWithJson(exchange, userDefinedResponseRepository.getAll());
			return;
		}
		if (mapping.equals("/list/log")) {
			replyWithJson(exchange, logRepository.getAll());
			return;
		}
		if (mapping.equals("/remove/\\d+")) {
			String requestedPath = exchange.getRequestURI().toString();
			int idToRemove = Integer.parseInt(requestedPath.substring(requestedPath.lastIndexOf('/')+1));			
			replyWithJson(exchange, userDefinedResponseRepository.remove(idToRemove));
			return;
		}
		if (mapping.equals("/test")) {
			ObjectMapper objectMapper = new ObjectMapper();
			UserDefinedResponse ob = objectMapper.readValue(exchange.getRequestBody(),UserDefinedResponse.class);
			replyWithJson(exchange, userDefinedResponseRepository.add(ob));
			return;
		}
	}

	public void replyWithJson(HttpExchange exchange, Object object) throws IOException {
		exchange.getResponseHeaders().add("content-type", "application/json;charset=utf-8");
		
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] output = objectMapper.writeValueAsBytes(object);
		exchange.sendResponseHeaders(200, output.length);
		
		exchange.getResponseBody().write(output);
		exchange.getResponseBody().flush();
		exchange.getResponseBody().close();
	}
	
	
	
	private class RestApiHandlerMappings extends Mappings{
		public RestApiHandlerMappings() {
			add("/list/userdefinedresponse");
			add("/list/log");
			add("/remove/\\d+");
			add("/test");
			
			
		}
	}
}
