package handler;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Mappings {
	Map<String, Predicate<String>> predicateMap = new HashMap<String, Predicate<String>>();
	
	public void add(String pattern) {
		predicateMap.put(pattern, Pattern.compile("^" + pattern + "$").asPredicate());
	}
	
	public void clear() {
		predicateMap.clear();
	}
	
	public void remove(String pattern) {
		predicateMap.remove(pattern);
	}
	
	public String getMappingFor(String path) throws Exception {
		for (Map.Entry<String, Predicate<String>> predicate : predicateMap.entrySet() ) {
			if (predicate.getValue().test(path))
				return predicate.getKey();
		}
		
		return null;
	}
	
}
