package handler;
import java.util.ArrayList;
import java.util.List;

public class RouteCollection {
	private List<Route> routes = new ArrayList<>();
	private List<Route> ignoredRoutes = new ArrayList<>();
	private Handler handler;
	
	public RouteCollection(Handler handler) {
		this.handler = handler;
	}
	
	public Handler getHandler() {
		return this.handler;
	}
	
	public Route mapAll() {
		return addNewRouteToList(".*", routes);
	}
	
	public Route mapRoute(String url) {
		return addNewRouteToList(url, routes);
	}
	
	public Route ignoreRoute(String url) {
		return addNewRouteToList(url, ignoredRoutes);
	}
	
	public Route getRouteFor(String url) {
		for (Route route : ignoredRoutes) {
			if (route.resolve(url)) {
				return null;
			}
		}
		
		for (Route route : routes) {
			if (route.resolve(url)) {
				return route;
			}
		}
		
		return null;
	}
	
	private Route addNewRouteToList(String url, List<Route> list) {
		Route route = new Route(url);
		list.add(route);
		return route;
	}

}

