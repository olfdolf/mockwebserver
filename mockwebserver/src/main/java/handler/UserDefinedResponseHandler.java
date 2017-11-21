package handler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import com.sun.net.httpserver.HttpExchange;

import repository.LogEntry;
import repository.LogRepository;
import repository.UserDefinedResponse;
import repository.UserDefinedResponseRepository;

public class UserDefinedResponseHandler implements Handler {
	private LogRepository logRepository;
	private UserDefinedResponseRepository userDefinedResponseRepository;

	public UserDefinedResponseHandler(LogRepository logRepository,
			UserDefinedResponseRepository userDefinedResponseRepository) {
		this.userDefinedResponseRepository = userDefinedResponseRepository;
		this.logRepository = logRepository;
	}

	public Mappings configureMappings() throws SQLException {
		return new UserDefinedResponseMappings();
	}
	
	@Override
	public void handle(HttpExchange exchange, String mapping) throws Exception {
		UserDefinedResponse userDefinedResponse = userDefinedResponseRepository.getForMapping(mapping);

		// Send headers for this mapping from userDefinedResponse
		exchange.getResponseHeaders().add("content-type", userDefinedResponse.getContentType());
		exchange.sendResponseHeaders(userDefinedResponse.getStatusCode(), userDefinedResponse.getResponseBody().length);

		// Read request stream into the ByteArrayOutputStream
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		InputStream requestBodyStream = exchange.getRequestBody();
		byte[] buffer = new byte[1024];
		int bytesRead = -1;
		while ((bytesRead = requestBodyStream.read(buffer)) != -1)
			byteArrayOutputStream.write(buffer, 0, bytesRead);

		// Write response to client from userDefinedResponse
		OutputStream responseStream = exchange.getResponseBody();
		responseStream.write(userDefinedResponse.getResponseBody());
		responseStream.flush();
		responseStream.close();

		// Log this request
		logRepository.addLogEntry(
				new LogEntry(0, exchange, byteArrayOutputStream.toByteArray(), userDefinedResponse.getResponseBody(),mapping));

	}
	
	private class UserDefinedResponseMappings extends Mappings {
		private UserDefinedResponseMappings() throws SQLException {
			for (UserDefinedResponse userDefinedResponse : userDefinedResponseRepository.getAll()) {
				add(userDefinedResponse.getMapping());
			}
		}
		
	}
}