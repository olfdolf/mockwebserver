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

/**
 * This Handler serves static files from a specified root path.
 * The handler tries to match incomming URI against a filename in the rootpath. If a file exists that matches the
 * URI then that file is used to generate a response to the client.
 * If no file is found the Handler does nothing<br />
 * <b>Example usage:</b>
 * <ul>
 *     <li>1. Request with URI <i>Index.html</i> comes in</li>
 *     <li>2. Request is passed to this handler</li>
 *     <li>3. Handler checks in the directory specified by {@link #StaticFilesHandler(String)} for a file named <i>Index.html</i></li>
 *     <li>4. If file exist: Write the file to client with correct mimetype and we are done.</li>
 *     <li>5. If file does not exist: Handler does nothing, next handler in chain gets chance to process the request</li>
 * </ul>
 */
public class StaticFilesHandler implements Handler {
	
	private Path staticFilesRootFolder;

	/**
	 * Creates a StaticFilesHandler with a specific path as the root, the Handler will match URI against the
	 * files in this path.
	 * @param staticFilesRootFolder The path to use as root
	 */
	public StaticFilesHandler(String staticFilesRootFolder) throws InvalidPathException {
		this.staticFilesRootFolder = Paths.get(staticFilesRootFolder);
	}

	@Override
	public RouteCollection configureRoutes(RouteCollection routeCollection) {
		routeCollection.mapAll();
		return routeCollection;
	}

	@Override
	public boolean handle(HttpExchange exchange, Route route) throws IOException {

		Path fileToSend = Paths.get(staticFilesRootFolder.toString(), exchange.getRequestURI().toString());
		
		if (!Files.exists(fileToSend))
			return false;
		
		exchange.getResponseHeaders().add("content-type", getContentTypeForFilename(fileToSend.toString()));
		exchange.sendResponseHeaders(200, Files.size(fileToSend));

		BufferedInputStream bufferedFileInputStream = new BufferedInputStream(Files.newInputStream(fileToSend));
		OutputStream responseStream = exchange.getResponseBody();
		
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = bufferedFileInputStream.read(buffer))!= -1) {
			responseStream.write(buffer, 0, bytesRead);
		}
		
		bufferedFileInputStream.close();
		responseStream.flush();
		responseStream.close();
		
		return true;
	}

	/**
	 * Private helper method to get a content-type header based on the fileextension.
	 * @param file The filename to get the extension for
	 * @return The content-type header
	 */
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
