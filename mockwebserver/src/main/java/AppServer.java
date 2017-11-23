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
import handler.Routes;


public class AppServer {
	
	private HttpServer httpServer;
	private String host;
	private int port;
	private List<Routes> routesList;
	
	public AppServer(String host, int port) {
		this.host = host;
		this.port = port;
		this.routesList = new ArrayList<>();
	}
	
	public void start() throws IOException {
		InetSocketAddress address = new InetSocketAddress(host, port);
		this.httpServer = HttpServer.create(address, 0);
		httpServer.createContext("/", new AppServerHttpHandler());
		httpServer.start();
	}
	
	public void addHandler(Handler handler) throws Exception {
		routesList.add(handler.configureRoutes(new Routes(handler)));
	}
	
	public void stop() {
		httpServer.stop(10);
	}

	private class AppServerHttpHandler implements HttpHandler {
		
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			boolean requestHandled = false;
			OutputStream responseStream = exchange.getResponseBody();
			
			for (Routes routes : routesList) {
				try {
					Routes.Route route = routes.getRouteFor(exchange.getRequestURI().toString());
					if (route != null && routes.getHandler().handle(exchange, route)) {
						requestHandled = true;
						System.out.println("Route mapped: " + exchange.getRequestURI() + " -> " + route.getUrl() + " (" + routes.getHandler().getClass() +")");
					}
				} catch (Exception e) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(bos);
					ps.print("<h1>Exception occured!</h1><pre>");
					e.printStackTrace(ps);
					ps.print("</pre>");
					
					byte[] output = bos.toByteArray();
					ps.close();
					bos.close();
					
					exchange.getResponseHeaders().add("content-type", "text/html;charset=utf-8");
					exchange.sendResponseHeaders(200, output.length);
					responseStream.write(output);
					requestHandled = true;
				}
				if (requestHandled)
					break;
			}

			if (requestHandled == false) {
				byte[] response = "<h1>No handler found</h1>There was no handler to process the request.".getBytes("UTF-8");
				
				exchange.getResponseHeaders().add("content-type", "text/html;charset=utf-8");
				exchange.sendResponseHeaders(200, response.length);
				responseStream.write(response);
			}

			responseStream.flush();
			responseStream.close();
		}
		
		
	}
}
