package handler;
import com.sun.net.httpserver.HttpExchange;

public interface Handler {
	boolean handle(HttpExchange exchange, Routes.Route route) throws Exception;
	public Routes configureRoutes(Routes routes);
}
