package handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Routes {
	private List<Route> routes = new ArrayList<Route>();
	private List<Route> ignoredRoutes = new ArrayList<Route>();
	private Handler handler;
	
	public Routes(Handler handler) {
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
	
	public static class Route {
		private String url;
		private Map<String, Object> data;
		private Predicate<String> predicate;
		
		public Route(String url) {
			this.url = url;
			predicate = Pattern.compile("^" + url + "$").asPredicate();
			this.data = new HashMap<>();
		}
		
		public boolean resolve(String url) {
			return predicate.test(url);
		}
		
		public Route addData(String key, Object object) {
			this.data.put(key, object);
			return this;
		}
		
		public Object getData(String key) {
			return this.data.get(key);
		}
		
		public String getUrl() {
			return this.url;
		}
	}
}

