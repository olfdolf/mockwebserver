package repository;

public class UserDefinedResponse {
	private int id;
	private int statusCode;
	private byte[] responseBody;
	private String mapping;
	private String contentType;

	public UserDefinedResponse(int id, int statusCode, byte[] responseBody, String mapping, String contentType) {
		this.id = id;
		this.statusCode = statusCode;
		this.responseBody = responseBody;
		this.mapping = mapping;
		this.contentType = contentType;
	}

	public int getId() {
		return id;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	public String getMapping() {
		return mapping;
	}

	public String getContentType() {
		return contentType;
	}

	// Anv�nds f�r JSON mappning
	@SuppressWarnings("unused")
	private UserDefinedResponse() {
		
	}
	
	// Anv�nds f�r JSON mappning
	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}
	
	// Anv�nds f�r JSON mappning
	@SuppressWarnings("unused")
	private void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	// Anv�nds f�r JSON mappning
	@SuppressWarnings("unused")	
	private void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}

	// Anv�nds f�r JSON mappning
	@SuppressWarnings("unused")
	private void setMapping(String mapping) {
		this.mapping = mapping;
	}

	// Anv�nds f�r JSON mappning
	@SuppressWarnings("unused")
	private void setContentType(String contentType) {
		this.contentType = contentType;
	}
}