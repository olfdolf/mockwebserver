package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class LogRepository {

	private Connection dbConnection;

	public LogRepository(Connection connection) {
		this.dbConnection = connection;
	}

	public Collection<LogEntry> getAll() throws SQLException {
		ResultSet resultSet = dbConnection.prepareStatement("SELECT * FROM logs ORDER BY id DESC;").executeQuery();
		Collection<LogEntry> allLogs = new ArrayList<>();
		
		while (resultSet.next()) {
			
			LogEntry logEntryFromDb = new LogEntry(
					resultSet.getInt("id"), 
					resultSet.getTimestamp("request_date").getTime(), 
					resultSet.getInt("status_code"), 
					resultSet.getString("request_method"), 
					resultSet.getString("protocol"), 
					resultSet.getString("request_path"), 
					resultSet.getString("remote_address"), 
					resultSet.getString("request_headers"), 
					resultSet.getString("response_headers"), 
					resultSet.getBytes("request_body"), 
					resultSet.getBytes("response_body"), 
					resultSet.getString("mapping"));
			
			allLogs.add(logEntryFromDb);
		}

		return allLogs;
	}
	
	public boolean addLogEntry(LogEntry entry) {
		try {
			PreparedStatement insertStatement = dbConnection.prepareStatement(
					"INSERT into logs (status_code, request_headers, request_body, response_headers, "
							+ "response_body, request_date, request_method, remote_address, request_path, protocol, mapping) "
							+ "values (?,?,?,?,?,?,?,?,?,?,?)");
			insertStatement.setInt(1, entry.getStatusCode());
			insertStatement.setString(2, entry.getRequestHeaders());
			insertStatement.setBytes(3, entry.getRequestBody());
			insertStatement.setString(4, entry.getResponseHeaders());
			insertStatement.setBytes(5, entry.getResponseBody());
			insertStatement.setTimestamp(6, new Timestamp(entry.getTimeStamp()));
			insertStatement.setString(7, entry.getRequestMethod());
			insertStatement.setString(8, entry.getRemoteAddress());
			insertStatement.setString(9, entry.getRequestPath());
			insertStatement.setString(10, entry.getProtocol());
			insertStatement.setString(11, entry.getMapping());

			return (insertStatement.executeUpdate() > 0);
		} catch (SQLException e) {
			return false;
		}
	}
}
