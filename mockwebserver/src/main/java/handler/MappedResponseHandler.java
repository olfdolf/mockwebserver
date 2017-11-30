package handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.regex.Pattern;
import com.sun.net.httpserver.HttpExchange;
import repository.LogEntry;
import repository.LogRepository;
import repository.MappedResponse;
import repository.MappedResponseRepository;

//TODO: javadoc
public class MappedResponseHandler implements Handler {
	private LogRepository logRepository;
	private MappedResponseRepository mappedResponseRepository;

	public MappedResponseHandler(LogRepository logRepository, MappedResponseRepository mappedResponseRepository) {
		this.mappedResponseRepository = mappedResponseRepository;
		this.logRepository = logRepository;
	}

	@Override
	public RouteCollection configureRoutes(RouteCollection routeCollection) {
		routeCollection.mapAll();
		return routeCollection;
	}
	
	@Override
	public boolean handle(HttpExchange exchange, Route route) throws SQLException, IOException {
		MappedResponse mappedResponse = null;
		String requestedUri = exchange.getRequestURI().toString();
		
		for (MappedResponse response : mappedResponseRepository.getAll()) {
			if (Pattern.matches(response.getUrl(), requestedUri)) {
				mappedResponse = response;
				break;
			}
		}

		if (mappedResponse == null)
			return false;
		
		exchange.getResponseHeaders().add("content-type", mappedResponse.getContentType());
		exchange.sendResponseHeaders(mappedResponse.getStatusCode(), mappedResponse.getResponseBody().length);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		InputStream requestBodyStream = exchange.getRequestBody();
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = requestBodyStream.read(buffer)) != -1)
			byteArrayOutputStream.write(buffer, 0, bytesRead);

		OutputStream responseStream = exchange.getResponseBody();
		responseStream.write(mappedResponse.getResponseBody());
		responseStream.flush();
		responseStream.close();

		logRepository.addLogEntry(
				new LogEntry(0, exchange, byteArrayOutputStream.toByteArray(), mappedResponse.getResponseBody(),mappedResponse.getUrl()));

		return true;
	}
	
}