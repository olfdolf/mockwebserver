package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserDefinedResponseRepository {
	private Connection dbConnection;

	public UserDefinedResponseRepository(Connection connection) {
		this.dbConnection = connection;
	}

	public UserDefinedResponse getForMapping(String mapping) throws SQLException {
		PreparedStatement selectStatement = dbConnection.prepareStatement("SELECT * FROM user_defined_response WHERE mapping = ? LIMIT 1");
		selectStatement.setString(1, mapping);
		ResultSet resultSet = selectStatement.executeQuery();
		
		if (resultSet.next()) {
			UserDefinedResponse userDefinedResponseFromDb = new UserDefinedResponse(resultSet.getInt("id"),resultSet.getInt("status_code"),
					resultSet.getBytes("response_body"), resultSet.getString("mapping"),
					resultSet.getString("content_type"));
			return userDefinedResponseFromDb;
		}
		
		return null;
	}

	public Collection<UserDefinedResponse> getAll() throws SQLException {
		ResultSet resultSet = dbConnection.prepareStatement("SELECT * FROM user_defined_response ORDEr BY id DESC;").executeQuery();
		Collection<UserDefinedResponse> allUserDefinedResponses = new ArrayList<>();
		
		while (resultSet.next()) {
			UserDefinedResponse userDefinedResponseFromDb = new UserDefinedResponse(resultSet.getInt("id"),resultSet.getInt("status_code"),
					resultSet.getBytes("response_body"), resultSet.getString("mapping"),
					resultSet.getString("content_type"));

			
			allUserDefinedResponses.add(userDefinedResponseFromDb);
		}

		return allUserDefinedResponses;
	}
	
	public UserDefinedResponse get(int id) throws SQLException {
		 PreparedStatement selectStatement = dbConnection.prepareStatement("SELECT * FROM user_defined_response where id = ? LIMIT 1;");
		 selectStatement.setInt(1, id);
		 ResultSet resultSet = selectStatement.executeQuery();
		
		UserDefinedResponse userDefinedResponseFromDb = null;
		
		if (resultSet.next())
			userDefinedResponseFromDb = new UserDefinedResponse(resultSet.getInt("id"),resultSet.getInt("status_code"),
					resultSet.getBytes("response_body"), resultSet.getString("mapping"),
					resultSet.getString("content_type"));
		
		return userDefinedResponseFromDb;
	}

	public boolean update(UserDefinedResponse userDefinedResponse) {
		try {
			PreparedStatement updateStatement = dbConnection.prepareStatement(
					"UPDATE user_defined_response SET (status_code, response_body, mapping, content_type) = (?,?,?,?) where id = ?");
			
			updateStatement.setInt(1, userDefinedResponse.getStatusCode());
			updateStatement.setBytes(2, userDefinedResponse.getResponseBody());
			updateStatement.setString(3, userDefinedResponse.getMapping());
			updateStatement.setString(4, userDefinedResponse.getContentType());
			updateStatement.setInt(5, userDefinedResponse.getId());
			
			return updateStatement.executeUpdate() > 0;
			
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean add(UserDefinedResponse userDefinedResponse) {
		try {
		PreparedStatement insertStatement = dbConnection.prepareStatement(
				"INSERT into user_defined_response (status_code, response_body, mapping, content_type) values (?,?,?,?);");
		insertStatement.setInt(1, userDefinedResponse.getStatusCode());
		insertStatement.setBytes(2, userDefinedResponse.getResponseBody());
		insertStatement.setString(3, userDefinedResponse.getMapping());
		insertStatement.setString(4, userDefinedResponse.getContentType());

		return (insertStatement.executeUpdate() > 0);
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean remove(int id) {
		try {
		PreparedStatement deleteStatement = dbConnection.prepareStatement(
				"DELETE from user_defined_response WHERE id = ?");
		deleteStatement.setInt(1, id);
		return deleteStatement.executeUpdate() > 0;
		} catch(SQLException e) {
			return false;
		}
	}
}
