package repository;

import java.util.StringJoiner;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

//TODO:  CLEANUP
public class LogEntry {
	private int id;
	private String requestMethod;
	private int statusCode;
	private String requestHeaders;
	private byte[] requestBody;
	private String responseHeaders;
	private byte[] responseBody;
	private long timeStamp;
	private String remoteAddress;
	private String requestPath;
	private String protocol;
	private String mapping;

	public LogEntry(int id, HttpExchange exchange, byte[] requestBody, byte[] responseBody, String mapping) {
		this.responseHeaders = formatHeaders(exchange.getResponseHeaders());
		this.requestHeaders = formatHeaders(exchange.getRequestHeaders());
		this.statusCode = exchange.getResponseCode();
		this.id = id;
		this.remoteAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
		this.requestMethod = exchange.getRequestMethod();
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.timeStamp = System.currentTimeMillis();
		this.requestPath = exchange.getRequestURI().toString();
		this.protocol = exchange.getProtocol();
		this.mapping = mapping;
	}
	
	//TODO: ... fixa
	public LogEntry(int id, long timeStamp, int responseCode, String requestMethod, String protocol, String requestPath, String remoteAddress, String requestHeaders, String responseHeaders, byte[] requestBody, byte[] responseBody, String mapping) {
		this.responseHeaders = responseHeaders;
		this.requestHeaders = requestHeaders;
		this.statusCode = responseCode;
		this.id = id;
		this.remoteAddress = remoteAddress;
		this.requestMethod = requestMethod;
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.timeStamp = timeStamp;
		this.requestPath = requestPath;
		this.protocol = protocol;
		this.mapping = mapping;
	}

	private String formatHeaders(Headers headers) {
		// Assemble request headers string
		final StringJoiner headerJoiner = new StringJoiner("\n");
		headers.forEach((name, values) -> {
			StringJoiner valueJoiner = new StringJoiner(";");
			values.forEach(valueJoiner::add);
			headerJoiner.add(name + ": " + valueJoiner.toString());
		});

		return headerJoiner.toString();
	}

	public int getId() {
		return this.id;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public String getRequestHeaders() {
		return requestHeaders;
	}

	public byte[] getRequestBody() {
		return requestBody;
	}

	public String getResponseHeaders() {
		return responseHeaders;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	//TODO: Timestamp, inte TimeStamp
	public long getTimeStamp() {
		return timeStamp;
	}

	public String getMapping() {
		return mapping;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private LogEntry() {

	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setRequestBody(byte[] requestBody) {
		this.requestBody = requestBody;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setResponseHeaders(String responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	// Används för JSON mappning
	@SuppressWarnings("unused")
	private void setMapping(String mapping) {
		this.mapping = mapping;
	}

}
