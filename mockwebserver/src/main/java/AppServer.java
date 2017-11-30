import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import handler.Handler;
import handler.Route;
import handler.RouteCollection;
//TODO: javadoc
/**
 * A HTTP server implementation that handles requests using implementations of the {@link handler.Handler} interface.<br />
 * <b>To use this class you should:</b>
 * <ul>
 * <li>Create a instance using {@link #AppServer(String, int)}</li>
 * <li>Register the handlers you wish to use with {@link #addHandler(Handler)}</li>
 * <li>Call {@link #start()} to make the server start processing requests.</li>
 * <li>Call {@link #stop(int)} to stop the server</li>
 * </ul>
 *<br />
 * When the HTTP server gets a request, that request will get passed to the first handler added using {@link #addHandler(Handler)}.
 * If that handler can handle the request and generate a response then we are done.
 * However, if the handler can't generate a response, the request is passed on till the next handler in the chain. This goes
 * on until either a handler generates a response or the end of the handler list is found.
 * If no handler is found a message display this will be generated as response to the client.
 */
public class AppServer {
	
	private HttpServer httpServer;
	private String host;
	private int port;
	private List<RouteCollection> routeCollectionList;

	/**
	 * Creates a HTTP server using the host/port specified. Note that the server is not started until
	 * {@link #start()} is called.
	 * @param host Hostname
	 * @param port Port
	 */
	public AppServer(String host, int port) {
		this.host = host;
		this.port = port;
		this.routeCollectionList = new ArrayList<>();
	}

	/**
	 * Creates a HTTP server that listens on the host/port specified using
	 * {@link #AppServer(String, int)} constructor.
	 */
	public void start() throws IOException {
		InetSocketAddress address = new InetSocketAddress(host, port);
		this.httpServer = HttpServer.create(address, 0);
		httpServer.createContext("/", new AppServerHttpHandler());
		httpServer.start();
	}

	/**
	 * Adds a handler to the servers request processing pipeline. Se the class description for more info regarding
	 * the handler pipeline.s
	 *
	 * @param handler A implementation of the handler interface used to process a specific request.
	 */
	public void addHandler(Handler handler) throws Exception {
		routeCollectionList.add(handler.configureRoutes(new RouteCollection(handler)));
	}

	/**
	 * Stops the server
	 * @param seconds Time in seconds to finish processing active requests.
	 */
	public void stop(int seconds) {
		httpServer.stop(seconds);
	}

	/**
	 * This is a internal class that is required by the underlying {@link com.sun.net.httpserver.HttpServer}.
	 * Basically this is what handles the pipeline.
	 */
	private class AppServerHttpHandler implements HttpHandler {

		/**
		 * Every handler registered a route when added using the {@link #addHandler(Handler)}.
		 * This is where the incomming request URI is matched against those routes to find a handler that wants
		 * to process the request.
		 * @param exchange The data of the http exchange
		 */
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			boolean requestHandled = false;
			OutputStream responseStream = exchange.getResponseBody();

			// check each route in the routelist until request is processed or no handle is found
			for (int i = 0; !requestHandled && i < routeCollectionList.size(); i++){
				try {
					// check if the URI matches the current route in the routeList
					Route route = routeCollectionList.get(i).hasMatch(exchange.getRequestURI().toString());

					// if so, pass the exchange data and the route info to the handler and see if it can handle it
					if (route != null && routeCollectionList.get(i).getHandler().handle(exchange, route)) {
						requestHandled = true;
					}
				} catch (Exception e) {
					// this code just generates a response incase a exception happend
					// if this happens the request i classed as handled.
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					PrintStream printStream = new PrintStream(byteArrayOutputStream);

					printStream.print("<h1>Exception occured!</h1><pre>");
					e.printStackTrace(printStream);
					printStream.print("</pre>");

					byte[] byteArray = byteArrayOutputStream.toByteArray();
					printStream.close();
					byteArrayOutputStream.close();

					exchange.getResponseHeaders().add("content-type", "text/html;charset=utf-8");
					exchange.sendResponseHeaders(200, byteArray.length);
					responseStream.write(byteArray);

					requestHandled = true;
				}
			}

			// if no handler could process the request and no exception occured we display this response to the client
			if (!requestHandled) {
				byte[] byteArray = "<h1>No handler found</h1>There was no handler to process the request.".getBytes("UTF-8");
				
				exchange.getResponseHeaders().add("content-type", "text/html;charset=utf-8");
				exchange.sendResponseHeaders(200, byteArray.length);
				responseStream.write(byteArray);
			}

			responseStream.flush();
			responseStream.close();
		}
		
		
	}
}
