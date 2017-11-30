package handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * A Route is a object that is used to check if a URI matches a specific pattern using the {@link #matches(String)} method.
 * The pattern is a ordinary regex pattern that is set when the Route is constructed.
 * Note however that the pattern will get wrapped inside another to make the specified one match the whole URI:
 * <pre>
 *     "hello" -> "^hello$"
 *     "hey" -> "^hey$"
 *     etc...
 * </pre>
 * Route also contains a collecion (map) of objects that can be accessed via keys using {@link #getData(String)} and
 * added using {@link #addData(String, Object)}
 */
public class Route {
	private Map<String, Object> data;
	private Predicate<String> predicate;

	/**
	 * Creates a Route that the method {@link #matches(String)} will match its parameter against.
	 * The uriPattern will get wrapped inside of ^ and $. So for example:
	 * <pre>
	 *     "olf" -> "^olf$"
	 *     "dolf" -> "^dolf$"
	 * </pre>
	 * @param uriPattern A regex pattern, it will be wrapped with ^ and $ so:
	 */
	public Route(String uriPattern) {
		predicate = Pattern.compile("^" + uriPattern + "$").asPredicate();
		this.data = new HashMap<>();
	}

	/**
	 * Matches the specified URI against the pattern used when creating the Route using {@link #Route(String)}.
	 * @param uri The URI to test against the pattern of the Route
	 * @return The result of the matching
	 */
	public boolean matches(String uri) {
		return predicate.test(uri);
	}

	/**
	 * Adds a object to the Route, this can be extracted using {@link #getData(String)}
	 * @param key The key to use as ID for the object
	 * @param object The object to add
	 * @return Self, for fluent method chaining
	 */
	public Route addData(String key, Object object) {
		this.data.put(key, object);
		return this;
	}

	/**
	 * Gets a object using a key that has been previously added using {@link #addData(String, Object)}.
	 * @param key The key the object was added under.
	 * @return The object
	 */
	public Object getData(String key) {
		return this.data.get(key);
	}
}
