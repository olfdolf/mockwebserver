package handler;
import com.sun.net.httpserver.HttpExchange;

/**
 * Interface used by the pipeline.
 */
public interface Handler {
	/**
	 * This is called whenever a request URI matches the route configured in {@link #configureRoutes(Routes)}.
	 * The {@link Routes.Route} object that this handler configured will get passed to this method aswell as
	 * data about the request. This method should try to generate a response for the request and return if it
	 * did so or not.
	 *
	 * @param exchange The data about the current request.
	 * @param route The route object that this handle configured.
	 * @return True if the request was handled, false otherwise
	 * @throws Exception
	 */
	boolean handle(HttpExchange exchange, Routes.Route route) throws Exception;

	/**
	 * Whenever a handler is added this method is called. The handler should use the routes object to configure
	 * itself for the URI's it wishes to handle. The pipeline uses the routes object to find the correct handler
	 * for the request.
	 * @param routes The object used to configure what URI the handler wishes to handle.
	 * @return The routes object that this this handler wishes to use, usually its the same object that's passed in.
	 * But it can be a subclass for more advanced scenarios.
	 */
	Routes configureRoutes(Routes routes);
}
