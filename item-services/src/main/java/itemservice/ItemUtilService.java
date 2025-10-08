package itemservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ItemUtilService {
	
	private DataSource dataSource;
	
	public ItemUtilService(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	public List<Item> getAllItem() throws SQLException{
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			
			String sql = "select * from item order by id";
			ResultSet resultSet = statement.executeQuery(sql);
			
			List<Item> items = new ArrayList();
			while (resultSet.next()) {
					Item item = new Item();
				
					item.setId(resultSet.getInt("id"));
					item.setName(resultSet.getString("name"));
					item.setPrice(resultSet.getDouble("price"));
					item.setTotalNumber(resultSet.getInt("total_number"));
					
					items.add(item);
			}
			
			return items;
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(connection != null)
			{
			connection.close();
			}
			if (statement != null)
			{
			statement.close();
			}
		}
		return new ArrayList();
	}
	 
	public void saveItem(Item item) throws SQLException{
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dataSource.getConnection();
			
			String sql = "INSERT INTO item (NAME,PRICE,TOTAL_NUMBER)"
						+ " VALUES (?, ?, ?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1, item.getName());
			statement.setDouble(2, item.getPrice());
			statement.setInt(3, item.getTotalNumber());
			
			statement.execute();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(connection != null)
			{
			connection.close();
			}
			if (statement != null)
			{
			statement.close();
			}
		}
	}
	
	
	public Item findItemById(int id) throws SQLException{
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dataSource.getConnection();
			
			String sql = "select * from item where id = ?";
			
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			
			Item item = new Item();
			if (resultSet.next()) {
					item.setId(resultSet.getInt("id"));
					item.setName(resultSet.getString("name"));
					item.setPrice(resultSet.getDouble("price"));
					item.setTotalNumber(resultSet.getInt("total_number"));
			}
			
			return item;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(connection != null)
			{
			connection.close();
			}
			if (statement != null)
			{
			statement.close();
			}
		}
		return null;
	}
	
	public void deleteItem(int id) throws SQLException{
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dataSource.getConnection();
			
			String sql = "DELETE from Item where id = ?";
			statement = connection.prepareStatement(sql);
			
			statement.setInt(1, id);
			
			statement.execute();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(connection != null)
			{
			connection.close();
			}
			if (statement != null)
			{
			statement.close();
			}
		}
	}

	public void updateItem(Item item) throws SQLException{
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dataSource.getConnection();
			
			String sql = "UPDATE ITEM SET NAME =?, PRICE=?, TOTAL_NUMBER=? WHERE id = ?";
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, item.getName());
			statement.setDouble(2, item.getPrice());
			statement.setInt(3, item.getTotalNumber());
			statement.setInt(4, item.getId());
			statement.execute();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(connection != null)
			{
			connection.close();
			}
			if (statement != null)
			{
			statement.close();
			}
		}
	}
	
	public ShowItemDetails getShowItemDetailsById(int id) throws SQLException {
	    String sql =
	        "SELECT i.id, i.name, i.price, i.total_number, " +
	        "       d.item_details_id, d.model, d.type_model, d.description " +
	        "FROM item i " + 
	        "LEFT JOIN item_details d ON d.item_id = i.id " +
	        "WHERE i.id = ?";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (!rs.next()) return null;

	            ShowItemDetails row = new ShowItemDetails();
	            // item
	            row.setId(rs.getInt("id"));
	            row.setName(rs.getString("name"));
	            row.setPrice(rs.getDouble("price"));
	            row.setTotalNumber(rs.getInt("total_number"));

	            int detId = rs.getInt("item_details_id");
	            if (rs.wasNull()) {
	                detId = 0;
	            }
	            row.setItemDetailsId(detId);
	            row.setModel(rs.getString("model"));
	            row.setType(rs.getString("type_model"));     // DB column -> Java field `type`
	            row.setDescription(rs.getString("description"));
	            row.setItemId(id);

	            return row;
	        }
	    }
	    catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		return null;
	}

	
	public void addDetails(ItemDetails d) throws SQLException {
	    String sql = "INSERT INTO item_details (model, type_model, description, item_id) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setString(1, d.getModel());
	        ps.setString(2, d.getType());
	        ps.setString(3, d.getDescription());
	        ps.setInt(4, d.getItemId());
	        
	        ps.executeUpdate();
	        
	    } catch (SQLException e) {
	        System.err.println("Error inserting item details: " + e.getMessage());
	        throw e; // ✅ Re-throw to notify controller of failure
	    }
	}

	public void updateDetails(ItemDetails d) throws SQLException {
	    String sql = "UPDATE item_details SET model = ?, type_model = ?, description = ? WHERE item_id = ?";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, d.getModel());
	        ps.setString(2, d.getType());
	        ps.setString(3, d.getDescription());
	        ps.setInt(4, d.getItemId());

	        int rows = ps.executeUpdate();
	        if (rows == 0) {
	            throw new SQLException("No details found for item_id=" + d.getItemId());
	        }

	    } catch (SQLException e) {
	        System.err.println("Error updating item details: " + e.getMessage());
	        throw e;
	    }
	}

	public void deleteDetailsByItemId(int itemId) throws SQLException {
	    String sql = "DELETE FROM item_details WHERE item_id = ?";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, itemId);
	        int rows = ps.executeUpdate();

	        if (rows == 0) {
	            System.out.println("No details found to delete for item_id=" + itemId);
	        } else {
	            System.out.println("Deleted " + rows + " detail(s) for item_id=" + itemId);
	        }

	    } catch (SQLException e) {
	        System.err.println("Error deleting item details: " + e.getMessage());
	        throw e;
	    }
	}


	public List<ShowItemDetails> getAllItemsWithDetailsFlag() throws SQLException {
	    String sql = "SELECT i.id, i.name, i.price, i.total_number, d.item_details_id " +
	                 "FROM item i " +
	                 "LEFT JOIN item_details d ON d.item_id = i.id " +
	                 "ORDER BY i.id";

	    List<ShowItemDetails> list = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            ShowItemDetails r = new ShowItemDetails();
	            r.setId(rs.getInt("id"));
	            r.setName(rs.getString("name"));
	            r.setPrice(rs.getDouble("price"));
	            r.setTotalNumber(rs.getInt("total_number"));

	            int detailsId = rs.getInt("item_details_id");
	            if (rs.wasNull()) detailsId = 0;
	            r.setItemDetailsId(detailsId);

	            list.add(r);
	        }
	    }
	    return list;
	}

}