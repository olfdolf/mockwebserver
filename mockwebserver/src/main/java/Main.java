import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import handler.RestApiHandler;
import handler.StaticFilesHandler;
import handler.MappedResponseHandler;
import repository.LogRepository;
import repository.MappedResponseRepository;

public class Main {
	final static String DATABASE_URL = "jdbc:sqlite:mockwebserver.db";
	final static String WEBSERVER_HOST = "";
	final static int WEBSERVER_PORT = 8080;
	final static String WEBSERVER_STATICFILES_ROOT = Paths.get("").resolve("webroot").toAbsolutePath().toString();

	public static void main(String[] args) throws Exception {
		Connection dbConnection = DriverManager.getConnection(DATABASE_URL);
		dbConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS mapped_responses ( id integer primary key not null, status_code integer, response_body blob, url text UNIQUE, content_type text );");
		dbConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS logs ( id integer primary key not null, status_code integer, request_headers text, request_body blob, response_headers text, response_body blob, request_date integer, request_method text, remote_address text, request_path text, protocol text, mapping text );");
		MappedResponseRepository mappedResponseRepository = new MappedResponseRepository(dbConnection);
		LogRepository logRepository = new LogRepository(dbConnection);
		
		AppServer server = new AppServer(WEBSERVER_HOST, WEBSERVER_PORT);

		server.addHandler(new StaticFilesHandler(WEBSERVER_STATICFILES_ROOT));
		server.addHandler(new RestApiHandler(mappedResponseRepository, logRepository));
		server.addHandler(new MappedResponseHandler(logRepository, mappedResponseRepository));
		server.start();

		System.out.println("Any key to quit");
		System.in.read();

	}
}
