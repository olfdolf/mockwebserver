import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

import handler.RestApiHandler;
import handler.StaticFilesHandler;
import handler.UserDefinedResponseHandler;
import repository.LogRepository;
import repository.UserDefinedResponse;
import repository.UserDefinedResponseRepository;

public class Main {
	final static String DATABASE_USER = "postgres";
	final static String DATABASE_PASSWORD = "password";
	final static String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";

	final static String WEBSERVER_HOST = "";
	final static int WEBSERVER_PORT = 8080;
	final static String WEBSERVER_STATICFILES_ROOT = Paths.get("").resolve("webroot").toAbsolutePath().toString();

	public static void main(String[] args) throws Exception {
		Connection dbConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

		UserDefinedResponseRepository userDefinedRepository = new UserDefinedResponseRepository(dbConnection);
		userDefinedRepository.add(new UserDefinedResponse(0, 200, "hello world 2".getBytes("UTF-8"), "/helloworld2",
				"text/html;charset=utf-8"));
		LogRepository logRepository = new LogRepository(dbConnection);
		
		AppServer server = new AppServer(WEBSERVER_HOST, WEBSERVER_PORT);

		server.addHandler(new StaticFilesHandler(WEBSERVER_STATICFILES_ROOT));
		server.addHandler(new RestApiHandler(userDefinedRepository, logRepository));
		server.addHandler(new UserDefinedResponseHandler(logRepository, userDefinedRepository));
		server.start();

		System.out.println("Any key to quit");
		System.in.read();

	}
}
