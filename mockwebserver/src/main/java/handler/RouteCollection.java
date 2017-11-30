package handler;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of {@link Route} objects for a specific {@link Handler}.
 */
public class RouteCollection {
	private List<Route> routeList = new ArrayList<>();
	private Handler handler;

	/**
	 * Creates a collection for a specific Handler.
	 * @param handler The Handler for this collection
	 */
	public RouteCollection(Handler handler) {
		this.handler = handler;
	}

	/**
	 * The Handler for the RouteCollection
	 * @return The Handler for the RouteCollection
	 */
	public Handler getHandler() {
		return this.handler;
	}

	/**
	 * Shortcut for creating a method that matches any URI and add it to this RouteCollection.
	 * @return
	 */
	public Route mapAll() {
		return addNewRouteToList(".*");
	}

	/**
	 * Creates a Route with a specific URI pattern and adds it to the collection.
	 * @param uriPattern The pattern to use for matching
	 * @return The Route that was created and added to the collection
	 */
	public Route mapRoute(String uriPattern) {
		return addNewRouteToList(uriPattern);
	}

	/**
	 * Checks if the collection has any Route that matches against a URI.
	 * @param uri The URI to match against
	 * @return The Route matched, or null if none matched
	 */
	public Route hasMatch(String uri) {

		for (Route route : routeList) {
			if (route.matches(uri)) {
				return route;
			}
		}
		
		return null;
	}

	/**
	 * Private method for creating and adding a Route to the collectionn
	 * @param uriPattern The pattern to use when creating the Route
	 * @return The Route created
	 */
	private Route addNewRouteToList(String uriPattern) {
		Route route = new Route(uriPattern);
		routeList.add(route);
		return route;
	}

}

