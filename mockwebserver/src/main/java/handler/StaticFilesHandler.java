package handler;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;


public class StaticFilesHandler implements Handler {
	
	private Path staticFilesRootFolder;
	
	public StaticFilesHandler(String staticFilesRootFoler) throws InvalidPathException {
		this.staticFilesRootFolder = Paths.get(staticFilesRootFoler);
	}
	
	//TODO: cleanup kanske
	public Mappings configureMappings() throws IOException  {
		return new StaticFilesHandlerMappings();
	}
	
	public void handle(HttpExchange exchange, String mapping) throws Exception {
		
		// Set correct content-type header depending on file extension
		exchange.getResponseHeaders().add("content-type", getContentTypeForFilename(mapping));
		
		// Send headers 
		Path fileToSend = Paths.get(staticFilesRootFolder.toString(), exchange.getRequestURI().toString());
		exchange.sendResponseHeaders(200, Files.size(fileToSend));
		
		// Write file to responsebody stream
		BufferedInputStream bufferedFileInputStream = new BufferedInputStream(Files.newInputStream(fileToSend));
		OutputStream responseStream = exchange.getResponseBody();
		
		byte[] buffer = new byte[1024];
		int bytesRead = -1;
		while ((bytesRead = bufferedFileInputStream.read(buffer))!= -1) {
			responseStream.write(buffer, 0, bytesRead);
		}
		
		bufferedFileInputStream.close();
		responseStream.flush();
		responseStream.close();
	}
	
	//helper function
	private String getContentTypeForFilename(String file) {
		file = file.toLowerCase().trim();
		Map<String,String> contentTypes = new HashMap<>();
		contentTypes.put("htm", "text/html;charset=utf-8");	
		contentTypes.put("html", "text/html;charset=utf-8");
		contentTypes.put("jpg", "image/jpeg");
		contentTypes.put("jpeg", "image/jpeg");
		
		return contentTypes.get(file.substring(file.lastIndexOf('.')+1));
	}
	
	private class StaticFilesHandlerMappings extends Mappings {

			private StaticFilesHandlerMappings() throws IOException {
				Files.walk(staticFilesRootFolder).filter(filePath -> Files.isRegularFile(filePath)).forEach(filePath -> {
					String mapping = "/" + staticFilesRootFolder.relativize(filePath).toString().replace('\\', '/');
					add(mapping);
				});
			}
	}
}
