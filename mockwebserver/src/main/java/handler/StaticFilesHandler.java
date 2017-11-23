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
	
	public StaticFilesHandler(String staticFilesRootFolder) throws InvalidPathException {
		this.staticFilesRootFolder = Paths.get(staticFilesRootFolder);
	}
	
	@Override
	public Routes configureRoutes(Routes routes) {
		routes.mapAll();
		return routes;
	}
	
	@Override
	public boolean handle(HttpExchange exchange, Routes.Route route) throws IOException {
		Path fileToSend = Paths.get(staticFilesRootFolder.toString(), exchange.getRequestURI().toString());
		
		if (Files.exists(fileToSend) == false) 
			return false;
		
		exchange.getResponseHeaders().add("content-type", getContentTypeForFilename(fileToSend.toString()));
		exchange.sendResponseHeaders(200, Files.size(fileToSend));

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
		
		return true;
	}
	
	private String getContentTypeForFilename(String file) {
		file = file.toLowerCase().trim();
		Map<String,String> contentTypes = new HashMap<>();
		contentTypes.put("htm", "text/html;charset=utf-8");	
		contentTypes.put("html", "text/html;charset=utf-8");
		contentTypes.put("js", "application/javascript;charset=utf-8");
		contentTypes.put("css", "text/css;charset=utf-8");
		contentTypes.put("jpg", "image/jpeg");
		contentTypes.put("jpeg", "image/jpeg");
		return contentTypes.get(file.substring(file.lastIndexOf('.')+1));
	}
	
}
