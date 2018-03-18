package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BulletinBroker implements BaseBroker {

    private final DatabaseUtil utils;

    public BulletinBroker() {
        this.utils = new DatabaseUtil();
    }

    @Override
    public DTO save(DTO newBulletin) {
        Connection conn = utils.openDatabase();
        BulletinDTO castedBulletinDTO = (BulletinDTO) newBulletin;
        PreparedStatement ps1;
        String strSQL = "INSERT INTO bulletin (body, subject, priority, idCategoriesFK) VALUES (?,?,?,?)";
        try {
            ps1 = conn.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, castedBulletinDTO.body);
            ps1.setString(2, castedBulletinDTO.subject);
            ps1.setString(3, castedBulletinDTO.priority);
            ps1.setInt(4, castedBulletinDTO.categoryDTO.id);
            ps1.execute();

            try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newBulletin.id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Lyckades inte inserta raden");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }

        return newBulletin;
    }

    @Override
    public DTO getSingle(DTO dto) {
        Connection conn = utils.openDatabase();
        CategoryBroker cr = new CategoryBroker();
        BulletinDTO bulletinDTO = new BulletinDTO();

        try {
            DTO catDTO = new CategoryDTO();
            String strSQL = "SELECT * from bulletin where idBulletin = ?";
            PreparedStatement ps = conn.prepareStatement(strSQL);
            ps.setInt(1, dto.id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                catDTO.id = rs.getInt("idCategoriesFK");
                bulletinDTO.subject = rs.getString("subject");
                bulletinDTO.body = rs.getString("body");
                bulletinDTO.categoryDTO = (CategoryDTO) cr.getSingle(catDTO);
                bulletinDTO.priority = rs.getString("priority");
                bulletinDTO.id = rs.getInt("idBulletin");
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bulletinDTO;
    }

    @Override
    public DTO[] getAll(DTO dto, DTO[] returnArray) {
        Connection conn = utils.openDatabase();
        int rowCount = this.getRowCount(), i = 0;
        returnArray = new BulletinDTO[rowCount];

        try {
            DTO catDTO = new CategoryDTO();
            String strSQL = "SELECT b.idBulletin, b.subject, b.body, b.priority, b.idCategoriesFK FROM bulletin b inner join categories c on b.idCategoriesFK = c.idCategories order by b.priority, c.categoryName, b.subject";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSQL);

            while (rs.next()) {
                catDTO.id = rs.getInt("b.idCategoriesFK");
                catDTO = DataFacade.getInstance().getSingle(catDTO);
                BulletinDTO newBulletin = new BulletinDTO();
                newBulletin.subject = rs.getString("b.subject");
                newBulletin.body = rs.getString("b.body");
                newBulletin.categoryDTO = (CategoryDTO) catDTO;
                newBulletin.priority = rs.getString("b.priority");
                newBulletin.id = rs.getInt("b.idBulletin");
                returnArray[i] = newBulletin;
                i++;
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }

        return returnArray;
    }

    private int getRowCount() {
        Connection conn = utils.openDatabase();
        int rowCount = 0;

        try {
            String strSQL = "SELECT * FROM bulletin";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(strSQL);
            rowCount = rs.last() ? rs.getRow() : 0;
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed to get connection");
        }
        return rowCount;
    }
}
