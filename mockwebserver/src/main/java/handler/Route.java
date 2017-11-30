package handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Route {
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
