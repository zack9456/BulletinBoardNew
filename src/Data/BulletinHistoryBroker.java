package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BulletinHistoryBroker implements BaseBroker{
    private final DatabaseUtil utils;
    
    public BulletinHistoryBroker(){
        this.utils = new DatabaseUtil();
    }
    
    @Override
    public DTO save(DTO newBulletinHistoryDTO){
        Connection conn = utils.openDatabase();
    	BulletinHistoryDTO castedBulletinHistoryDTO = (BulletinHistoryDTO) newBulletinHistoryDTO;
        PreparedStatement ps1;
        String strSQL = "INSERT INTO bulletinHistory (state, created, idBulletinFK, idCurrentBulletinHistory) VALUES (?,?,?,?)";
        
        try 
        {
            ps1 = conn.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, castedBulletinHistoryDTO.currentState);
            ps1.setTimestamp(2, castedBulletinHistoryDTO.created);
            ps1.setInt(3, castedBulletinHistoryDTO.bulletinDTO.id);
            if(castedBulletinHistoryDTO.currentBulletinHistory == null){
                ps1.setNull(4, java.sql.Types.INTEGER);
            }
            else{
                ps1.setInt(4, castedBulletinHistoryDTO.currentBulletinHistory);
            }
            
            ps1.execute();
            
            try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newBulletinHistoryDTO.id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Lyckades inte inserta raden");
                }
            }
            conn.close();
        } 
        catch (SQLException ex) 
        {
        	ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        return newBulletinHistoryDTO;
    }
    
    
    @Override
    public DTO getSingle(DTO dto){
        Connection conn = utils.openDatabase();
        BulletinHistoryDTO bulletinHistoryDTO = new BulletinHistoryDTO();
        
        try
        {
            String strSQL = "SELECT * FROM bulletinhistory WHERE idCurrentBulletinHistory IS NULL AND idBulletinFK = ?";
            PreparedStatement ps = conn.prepareStatement(strSQL);
            ps.setInt(1, dto.id);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
            	bulletinHistoryDTO.currentState = rs.getString("state");
    			bulletinHistoryDTO.created = rs.getTimestamp("created");
                bulletinHistoryDTO.id = rs.getInt("idBulletinHistory");
                bulletinHistoryDTO.bulletinDTO = (BulletinDTO) dto;
            }
            conn.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        
        return bulletinHistoryDTO;
    }
    
    @Override
    public DTO[] getAll(DTO bulletinDTO, DTO[] returnArray) {
    	int limit = 24;
    	Connection conn = utils.openDatabase();
    	BulletinDTO bulletin = (BulletinDTO) bulletinDTO;
    	int rowCount = getRowCount(bulletin, limit), i = 0;
    	returnArray = new BulletinHistoryDTO[rowCount];
    	PreparedStatement ps1;
    	
    	try
    	{
    		String strSQL = "SELECT state, created, idBulletinFK, idCurrentBulletinHistory FROM bulletinhistory where idBulletinFK = ? order by idBulletinHistory desc limit ?";
    		ps1 = conn.prepareStatement(strSQL);
    		ps1.setInt(1, bulletin.id);
    		ps1.setInt(2, limit);
    		
    		ResultSet rs = ps1.executeQuery();
    		while (rs.next())
    		{
    			BulletinHistoryDTO bulletinHistory = new BulletinHistoryDTO();
    			bulletinHistory.currentState = rs.getString("state");
    			bulletinHistory.created = rs.getTimestamp("created");
    			bulletinHistory.bulletinDTO = bulletin;
    			bulletinHistory.currentBulletinHistory = rs.getInt("idCurrentBulletinHistory");
    			returnArray[i] = bulletinHistory;
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
    
    public void updateSecondLatestHistory(DTO currentLatestBH, DTO newBulletinHistoryDTO){
    	Connection conn = utils.openDatabase();
    	PreparedStatement ps1;
    	String strSQL = "UPDATE bulletinhistory SET idCurrentBulletinHistory = ? WHERE idBulletinHistory = ?";
    	
    	try
    	{
    		ps1 = conn.prepareStatement(strSQL);
    		ps1.setInt(1, newBulletinHistoryDTO.id);
    		ps1.setInt(2, currentLatestBH.id);
    		
    		ps1.execute();
    		conn.close();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    
    private int getRowCount(DTO dto, int limit)
    {
        Connection conn2 = utils.openDatabase();
        int rowCount = 0;
        PreparedStatement ps1;
        
        try 
        {
            String strSQL = "SELECT * FROM bulletinhistory where idBulletinFK = ? limit ?";
        
            ps1 = conn2.prepareStatement(strSQL);
            ps1.setInt(1, dto.id);
            ps1.setInt(2, limit);
            
            ResultSet rs = ps1.executeQuery();
            rowCount = rs.last() ? rs.getRow() : 0;
            conn2.close();
        } 
        catch (SQLException ex) 
        {
        	ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        return rowCount;
    }
}
