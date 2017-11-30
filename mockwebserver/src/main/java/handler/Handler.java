package handler;
import com.sun.net.httpserver.HttpExchange;

/**
 * Interface used by the pipeline.
 */
public interface Handler {

	/**
	 * Whenever a URI is matched against a Route that got configured in {@link #configureRoutes(RouteCollection)}, this
	 * method is called. The parameters will hold the Route that matched the URI and the data for the request(exchange).
	 * A handler should return true if it handled the request and false otherwise. Handling a request means
	 * generating a response to the responseBody stream in the exchange object.
	 *
	 * @param exchange The data of the http exchange
	 * @param route The route that matched the URI
	 * @return If handled or not
	 */
	boolean handle(HttpExchange exchange, Route route) throws Exception;

	/**
	 * A handler should configure itself to handle specific URI's. It can use the routeCollection object that has
	 * been passed into this method to add URI patterns it wishes to handle.
	 * @param routeCollection The configuration object for Route's
	 * @return The configuration object for Route's (Usually the same but doesn't have to be)
	 */
	RouteCollection configureRoutes(RouteCollection routeCollection);
}
