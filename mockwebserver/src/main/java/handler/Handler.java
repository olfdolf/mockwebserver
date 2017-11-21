package handler;
import com.sun.net.httpserver.HttpExchange;

public interface Handler {
	void handle(HttpExchange exchange, String mapping) throws Exception;
	public Mappings configureMappings() throws Exception;
}
