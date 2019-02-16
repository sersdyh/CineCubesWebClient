package cubemanager.starschema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
	/**
	 * @uml.property  name="dBName"
	 */
	private String DBName;
	/**
	 * @uml.property  name="tbl"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="CubeMgr.StarSchema.Table"
	 */
	private List<Table> Tbl;

	/**
	 * @uml.property  name="connectionString"
	 */
	private String ConnectionString;
	/**
	 * @uml.property  name="dBMS"
	 */
	private String DBMS;
	/**
	 * @uml.property  name="connect"
	 */
	private Connection connect;
	/**
	 * @uml.property  name="username"
	 */
	private String Username;
	/**
	 * @uml.property  name="password"
	 */
	private String Password;

	public Database() {
		setConnectionString("jdbc:mysql://localhost:3306/adult_no_dublic");
		DBMS = "com.mysql.jdbc.Driver";
		Tbl = new ArrayList<Table>();
	}

	public Database(String ConnStr, String dbtype) {
		setConnectionString(ConnStr);
		DBMS = dbtype;
		Tbl = new ArrayList<Table>();
	}

	public void registerDatabase() {
		try {
			try {
				Class.forName(DBMS).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		try {
			setConnection(DriverManager.getConnection(ConnectionString,
					Username, Password));
		} catch (SQLException ex) {
			System.out.println(ConnectionString);
			System.out.println("Connection Failed! Check output console");
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("LocalState: " + ex.getLocalizedMessage());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void GenerateTableList() {
		try {
			DatabaseMetaData Metadata = connect.getMetaData();
			ResultSet rs = Metadata.getTables(null, null, "%", null);
			;
			while (rs.next()) {
				Table tmp = new Table(rs.getString(3));
				tmp.setAttribute(connect);
				this.Tbl.add(tmp);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			(new ErrorClass()).printErrorMessage(ex.getMessage());
		}
	}

	public void PrintTableList() {
		for (Table item : this.Tbl) {
			System.out.println(item. getTableName());
			item.printColumns();
		}
	}

	/**
	 * @return
	 * @uml.property  name="connectionString"
	 */
	public String getConnectionString() {
		return ConnectionString;
	}

	/**
	 * @param connectionString
	 * @uml.property  name="connectionString"
	 */
	public void setConnectionString(String connectionString) {
		ConnectionString = connectionString;
	}

	public Connection getConnection() {
		return connect;
	}

	public void setConnection(Connection connect) {
		this.connect = connect;
	}

	/**
	 * @return
	 * @uml.property  name="username"
	 */
	public String getUsername() {
		return Username;
	}

	/**
	 * @param username
	 * @uml.property  name="username"
	 */
	public void setUsername(String username) {
		Username = username;
	}

	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		Password = password;
	}

	/**
	 * @return
	 * @uml.property  name="dBName"
	 */
	public String getDBName() {
		return DBName;
	}

	/**
	 * @param dBName
	 * @uml.property  name="dBName"
	 */
	public void setDBName(String dBName) {
		DBName = dBName;
	}

	/**
	 * @return
	 * @uml.property  name="dBMS"
	 */
	public String getDBMS() {
		return DBMS;
	}

	/**
	 * @param dbms
	 * @uml.property  name="dBMS"
	 */
	public void setDBMS(String dbms) {
		DBMS = dbms;
	}

	public Table getDBTableInstance(String nameTbl) {
		Table retTbl = null;
		for (int i = 0; i < this.Tbl.size(); i++) {
			if (this.Tbl.get(i). getTableName().equals(nameTbl))
				retTbl = this.Tbl.get(i);
		}
		if (retTbl == null) {
			System.err.println("Sql Table no exist");
			System.exit(1);
		}
		return retTbl;
	} 

	public Attribute getFieldOfSqlTable(String table, String field) {
		return this.getDBTableInstance(table).getAttribute(field);

	}

	public ResultSet executeSql(String query) {
		ResultSet res = null;
		try {
			Statement createStatement = connect.createStatement();
			res = createStatement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

}
