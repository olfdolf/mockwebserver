package repository;

public class MappedResponse {
	private int id;
	private int statusCode;
	private byte[] responseBody;
	private String url;
	private String contentType;

	public MappedResponse(int id, int statusCode, byte[] responseBody, String url, String contentType) {
		this.id = id;
		this.statusCode = statusCode;
		this.responseBody = responseBody;
		this.url = url;
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

	public String getUrl() {
		return url;
	}

	public String getContentType() {
		return contentType;
	}

	@SuppressWarnings("unused")
	private MappedResponse() {
	}
	
	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}
	
	@SuppressWarnings("unused")
	private void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	@SuppressWarnings("unused")	
	private void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}

	@SuppressWarnings("unused")
	private void setUrl(String url) {
		this.url = url;
	}

	@SuppressWarnings("unused")
	private void setContentType(String contentType) {
		this.contentType = contentType;
	}
}