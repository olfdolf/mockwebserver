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
//TODO: javadoc
/**
 * This handler generates responses using the filesystem. The handler will configure itself to get requests
 * for all URI's. If a URI matches a relative filepath then that file is read and generated as response.
 * If no filepath matches the URI this handler does nothing.<br/><br/>
 * <b>Example</b>
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
	 * Pass the folder to use as the "webroot" folder. The URI's will be matched against the relative
	 * filepaths in this folder.
	 * @param staticFilesRootFolder The root folder of all files served
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
	
	private String getContentTypeForFilename(String file) {
		// this just generates the correct "content-type" header based on the file extension.
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
