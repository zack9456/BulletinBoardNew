package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
	private Connection myConnection, databaseConnection;
	
	private void connectToDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/", "root", "admin");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to get connection");
		}
	}

	public Boolean databaseExisting() {
		Boolean exists = false;
		connectToDatabase();
		try {
			Statement stmt1 = myConnection.createStatement();
			String strSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'BulletinDatabase'";

			ResultSet rs1 = stmt1.executeQuery(strSQL);

			while (rs1.next()) {
				exists = true;
			}
			myConnection.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("Failed to get connection");
		}
		return exists;
	}

	public Connection openDatabase() {
		try {
			String strSQL = "jdbc:mysql://localhost:3306/BulletinDatabase?verifyServerCertificate=false&useSSL=false";
			databaseConnection = DriverManager.getConnection(strSQL, "root", "admin");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to get connection");
		}

		return databaseConnection;
	}

	private void createCategories() {
		try {
			Statement stmt1 = databaseConnection.createStatement();
			String strSQL = "insert into categories (categoryName, categoryDescription) values ('Chefer', 'Feedback angående cheferna')";
			stmt1.executeUpdate(strSQL);
			strSQL = "insert into categories (categoryName, categoryDescription) values ('Arbetsmiljö', 'Feedback angående arbetsmiljön')";
			stmt1.executeUpdate(strSQL);
			strSQL = "insert into categories (categoryName, categoryDescription) values ('IT', 'Feedback angående IT-avdelningen eller IT generellt')";
			stmt1.executeUpdate(strSQL);
			strSQL = "insert into categories (categoryName, categoryDescription) values ('Företagsledning', 'Feedback angående företagsledningen och företagsmål')";
			stmt1.executeUpdate(strSQL);
			strSQL = "insert into categories (categoryName, categoryDescription) values ('Matservering', 'Feedback angående maten')";
			stmt1.executeUpdate(strSQL);
			strSQL = "insert into categories (categoryName, categoryDescription) values ('Projekthantering', 'Feedback angående hanteringen av enskilda projekt')";
			stmt1.executeUpdate(strSQL);
			strSQL = "insert into categories (categoryName, categoryDescription) values ('Övrigt', 'Feedback som inte passar under övriga kategorier')";
			stmt1.executeUpdate(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to get connection");
		}
	}

	public void createDatabase() {
		try {
			connectToDatabase();
			Statement stmt1, stmt2;
			String strSQL;
			stmt1 = myConnection.createStatement();

			strSQL = "DROP DATABASE IF EXISTS BulletinDatabase";
			stmt1.executeUpdate(strSQL);
			strSQL = "CREATE DATABASE BulletinDatabase";
			stmt1.executeUpdate(strSQL);
			
			myConnection.close();

			this.openDatabase();

			stmt2 = databaseConnection.createStatement();

			strSQL = "DROP TABLE IF EXISTS categories";
			stmt2.executeUpdate(strSQL);
			strSQL = "CREATE TABLE categories (\n" 
					+ "  idCategories int(8) UNSIGNED NOT NULL AUTO_INCREMENT,\n"
					+ "  categoryName VARCHAR(30) NOT NULL,\n" 
					+ "  categoryDescription VARCHAR(100) NOT NULL,\n"
					+ "  PRIMARY KEY (idCategories),\n" 
					+ "  UNIQUE KEY idCategories_UNIQUE (idCategories)\n"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
			stmt2.executeUpdate(strSQL);

			strSQL = "DROP TABLE IF EXISTS bulletin";
			stmt2.executeUpdate(strSQL);
			strSQL = "CREATE TABLE bulletin (\n" 
			+ "  idBulletin int(8) UNSIGNED NOT NULL AUTO_INCREMENT,\n"
					+ "  body varchar(400) NOT NULL,\n" 
					+ "  subject varchar(50) NOT NULL,\n"
					+ "  priority varchar(50) NOT NULL,\n" 
					+ "  idCategoriesFK int(8) UNSIGNED NOT NULL,\n"
					+ "  PRIMARY KEY (idBulletin),\n" 
					+ "  UNIQUE KEY idBulletin_UNIQUE (idBulletin),\n"
					+ "  CONSTRAINT idCategoriesFK FOREIGN KEY (idCategoriesFK) REFERENCES categories (idCategories) ON DELETE NO ACTION ON UPDATE NO ACTION"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
			stmt2.executeUpdate(strSQL);

			strSQL = "DROP TABLE IF EXISTS bulletinhistory";
			stmt2.executeUpdate(strSQL);
			strSQL = "CREATE TABLE bulletinhistory (\n"
					+ "  idBulletinHistory int(8) UNSIGNED NOT NULL AUTO_INCREMENT,\n"
					+ "  state varchar(20) NOT NULL,\n" 
					+ "  created timestamp NOT NULL,\n"
					+ "  idBulletinFK int(8) UNSIGNED NOT NULL,\n" 
					+ "  idCurrentBulletinHistory int(8) UNSIGNED,\n"
					+ "  PRIMARY KEY (idBulletinHistory),\n"
					+ "  UNIQUE KEY idBulletinHistory_UNIQUE (idBulletinHistory),\n"
					+ "  CONSTRAINT idBulletinFK FOREIGN KEY (idBulletinFK) REFERENCES Bulletin (idBulletin) ON DELETE NO ACTION ON UPDATE NO ACTION,\n"
					+ "  CONSTRAINT idCurrentBulletinHistory FOREIGN KEY (idCurrentBulletinHistory) REFERENCES BulletinHistory (idBulletinHistory) ON DELETE NO ACTION ON UPDATE NO ACTION"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
			stmt2.executeUpdate(strSQL);

			this.createCategories();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to get connection");
		}
	}
}
