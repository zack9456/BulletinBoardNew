package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoryBroker implements BaseBroker
{    
    private final DatabaseUtil utils;
    
    public CategoryBroker()
    {
        utils = new DatabaseUtil();
    }
    
	@Override
	public DTO save(DTO newDTO) {
		Connection conn = utils.openDatabase();
		CategoryDTO castedCategoryDTO = (CategoryDTO) newDTO;
	    PreparedStatement ps1;
	    String strSQL = "INSERT INTO categories (categoryName, categoryDescription) VALUES (?,?)";
	    try 
	    {
	        ps1 = conn.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
	        ps1.setString(1, castedCategoryDTO.categoryName);
	        ps1.setString(2, castedCategoryDTO.description);
	        
	        ps1.execute();
	        conn.close();
	    } 
	    catch (SQLException ex) 
	    {
	        ex.printStackTrace();
	        System.out.println("Failed to get connection");
	    }
    
	    return castedCategoryDTO;
	}
    
    @Override
    public DTO getSingle(DTO dto)
    {
        Connection conn = utils.openDatabase();
        CategoryDTO category = null;
        
        try
        {
            String strSQL = "SELECT * FROM categories where idCategories = ?";
            PreparedStatement ps = conn.prepareStatement(strSQL);
            ps.setInt(1, dto.id);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                category = new CategoryDTO();
                category.categoryName = rs.getString("categoryName");
                category.description = rs.getString("categoryDescription");
                category.id = rs.getInt("idCategories");
            }
            conn.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        
        return category;
    }

    @Override
    public DTO[] getAll(DTO dto, DTO[] returnArray) 
    {
        Connection conn = utils.openDatabase();
        int rowCount = this.getRowCount(), i = 0;
        returnArray = new CategoryDTO[rowCount];
        
        try
        {
            String strSQL = "SELECT * FROM categories order by categoryName collate utf8_swedish_ci";
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(strSQL);
            while (rs.next())
            {
            	CategoryDTO catDTO = new CategoryDTO();
            	catDTO.categoryName = rs.getString("categoryName");
            	catDTO.description = rs.getString("categoryDescription");
            	catDTO.id = rs.getInt("idCategories");
            	returnArray[i] = catDTO;
                i++;
            }
            conn.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        
        return returnArray;
    }
    
    public int getRowCount()
    {
        Connection conn = utils.openDatabase();
        int rowCount = 0;
        
        try 
        {
            String strSQL = "SELECT * FROM categories";
        
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSQL);
            rowCount = rs.last() ? rs.getRow() : 0;
            conn.close();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        return rowCount;
    }
}
