package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MappedResponseRepository {
	private Connection dbConnection;

	public MappedResponseRepository(Connection connection) {
		this.dbConnection = connection;
	}

	public MappedResponse getForUrl(String url) throws SQLException {
		PreparedStatement selectStatement = dbConnection.prepareStatement("SELECT * FROM mapped_responses WHERE url = ? LIMIT 1");
		selectStatement.setString(1, url);
		ResultSet resultSet = selectStatement.executeQuery();
		
		if (resultSet.next()) {
			MappedResponse mappedResponseFromDb = new MappedResponse(resultSet.getInt("id"),resultSet.getInt("status_code"),
					resultSet.getBytes("response_body"), resultSet.getString("url"),
					resultSet.getString("content_type"));
			return mappedResponseFromDb;
		}
		
		return null;
	}

	public Collection<MappedResponse> getAll() throws SQLException {
		ResultSet resultSet = dbConnection.prepareStatement("SELECT * FROM mapped_responses ORDEr BY id DESC;").executeQuery();
		Collection<MappedResponse> allMappedResponses = new ArrayList<>();
		
		while (resultSet.next()) {
			MappedResponse mappedResponseFromDb = new MappedResponse(resultSet.getInt("id"),resultSet.getInt("status_code"),
					resultSet.getBytes("response_body"), resultSet.getString("url"),
					resultSet.getString("content_type"));

			
			allMappedResponses.add(mappedResponseFromDb);
		}

		return allMappedResponses;
	}
	
	public MappedResponse get(int id) throws SQLException {
		 PreparedStatement selectStatement = dbConnection.prepareStatement("SELECT * FROM mapped_responses where id = ? LIMIT 1;");
		 selectStatement.setInt(1, id);
		 ResultSet resultSet = selectStatement.executeQuery();
		
		MappedResponse mappedResponseFromDb = null;
		
		if (resultSet.next())
			mappedResponseFromDb = new MappedResponse(resultSet.getInt("id"),resultSet.getInt("status_code"),
					resultSet.getBytes("response_body"), resultSet.getString("url"),
					resultSet.getString("content_type"));
		
		return mappedResponseFromDb;
	}

	public boolean update(MappedResponse mappedResponse) {
		try {
			PreparedStatement updateStatement = dbConnection.prepareStatement(
					"UPDATE mapped_responses SET (status_code, response_body, url, content_type) = (?,?,?,?) where id = ?");
			
			updateStatement.setInt(1, mappedResponse.getStatusCode());
			updateStatement.setBytes(2, mappedResponse.getResponseBody());
			updateStatement.setString(3, mappedResponse.getUrl());
			updateStatement.setString(4, mappedResponse.getContentType());
			updateStatement.setInt(5, mappedResponse.getId());
			
			return updateStatement.executeUpdate() > 0;
			
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean add(MappedResponse mappedResponse) {
		try {
		PreparedStatement insertStatement = dbConnection.prepareStatement(
				"INSERT into mapped_responses (status_code, response_body, url, content_type) values (?,?,?,?);");
		insertStatement.setInt(1, mappedResponse.getStatusCode());
		insertStatement.setBytes(2, mappedResponse.getResponseBody());
		insertStatement.setString(3, mappedResponse.getUrl());
		insertStatement.setString(4, mappedResponse.getContentType());

		return (insertStatement.executeUpdate() > 0);
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean remove(int id) {
		try {
		PreparedStatement deleteStatement = dbConnection.prepareStatement(
				"DELETE from mapped_responses WHERE id = ?");
		deleteStatement.setInt(1, id);
		return deleteStatement.executeUpdate() > 0;
		} catch(SQLException e) {
			return false;
		}
	}
}
