package cs174a;                                             // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// You may have as many imports as you need.
import java.util.concurrent.ThreadLocalRandom; //Random number
import java.util.*;
import java.text.SimpleDateFormat;

import javax.management.RuntimeErrorException;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;

import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

/**
 * The most important class for your application.
 * DO NOT CHANGE ITS SIGNATURE.
 */
public class App implements Testable
{
	private OracleConnection _connection;                   // Example connection object to your DB.

	/**
	 * Default constructor.
	 * DO NOT REMOVE.
	 */
	App()
	{
		// TODO: Any actions you need.
	}


	public ResultSet query(String s) throws SQLException{
			Statement statement =_connection.createStatement();
			ResultSet res = statement.executeQuery(s);
			return res;
	}

	/**
	 * This is an example access operation to the DB.
	 */
	void exampleAccessToDB()
	{
		// Statement and ResultSet are AutoCloseable and closed automatically.
		try( Statement statement = _connection.createStatement() )
		{
			try( ResultSet resultSet = statement.executeQuery( "select owner, table_name from all_tables" ) )
			{
				while( resultSet.next() )
					System.out.println( resultSet.getString( 1 ) + " " + resultSet.getString( 2 ) + " " );
			}
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
		}
	}

	////////////////////////////// Implement all of the methods given in the interface /////////////////////////////////
	// Check the Testable.java interface for the function signatures and descriptions.

	@Override
	public String initializeSystem()
	{
		// Some constants to connect to your DB.
		final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
		final String DB_USER = "c##dgamliel";
		final String DB_PASSWORD = "4372280";

		// Initialize your system.  Probably setting up the DB connection.
		Properties info = new Properties();
		info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
		info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
		info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );

		try
		{
			System.out.println("Attempting to connect to db....");
			OracleDataSource ods = new OracleDataSource();
			ods.setURL( DB_URL );
			ods.setConnectionProperties( info );
			_connection = (OracleConnection) ods.getConnection();

			// Get the JDBC driver name and version.
			DatabaseMetaData dbmd = _connection.getMetaData();
			System.out.println( "Driver Name: " + dbmd.getDriverName() );
			System.out.println( "Driver Version: " + dbmd.getDriverVersion() );

			// Print some connection properties.
			System.out.println( "Default Row Prefetch Value is: " + _connection.getDefaultRowPrefetch() );
			System.out.println( "Database Username is: " + _connection.getUserName() );
			System.out.println();

			return "0";
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	/**
	 * Example of one of the testable functions.
	 */
	@Override
	public String listClosedAccounts()
	{
		return "0 it works!";
	}

	/**
	 * Create a new checking or savings account.
	 * If customer is new, then their name and address should be provided.
	 * @param accountType New account's checking or savings type.
	 * @param id New account's ID.
	 * @param initialBalance Initial account balance.
	 * @param tin Account's owner Tax ID number - it may belong to an existing or new customer.
	 * @param name [Optional] If customer is new, this is the customer's name.
	 * @param address [Optional] If customer is new, this is the customer's address.
	 * @return a string "r aid type balance tin", where
	 *         r = 0 for success, 1 for error;
	 *         aid is the new account id;
	 *         type is the new account's type (see the enum codes above, e.g. INTEREST_CHECKING);
	 *         balance is the account's initial balance with 2 decimal places (e.g. 1000.34, as with %.2f); and
	 *         tin is the Tax ID of account's primary owner.
	 */
	@Override
	public String createCheckingSavingsAccount( AccountType accountType, String id, double initialBalance, String tin, String name, String address )
	{
		int random = ThreadLocalRandom.current().nextInt(1000, 9999 + 1); //Pick num between 1000 and 9999
		String encryptedPin = hashPin(random);

		String insertAccounts = "INSERT INTO Accounts " +
														"(aid, type, balance, closed) " +  
														"VALUES (" + id + ", \'" + accountType + "\', " + initialBalance + ", 0" + ")";
	
		String insertOwns = "INSERT INTO Owns " + 
												"VALUES (" + id + ", " + tin + ", "+ 1 + ")";	//prinmary owner (1) cause they created the account
							
		String insertCustomer = "INSERT INTO Clients " +
														"(cid, name, addr, pin) " + 
														"VALUES (" + tin + ", \'" + name + "\', \'" + address + "\', \'" + encryptedPin + "\')";	//primary owner (1) cause they created the account

		String insertAccType;


		if (accountType == AccountType.SAVINGS){
			insertAccType = "INSERT INTO Savings VALUES (" + id + ")";
		}

		else if (accountType == AccountType.INTEREST_CHECKING){	
			insertAccType = "INSERT INTO Checkings VALUES (" + id + ")";
		}


		try{
			Statement statement = _connection.createStatement();

			//Insert into accounts
			ResultSet resAccs = statement.executeQuery(insertAccounts);				
			System.out.println(insertAccounts);	

			//Insert into customer
			ResultSet res = statement.executeQuery(insertCustomer);				
			System.out.println(insertCustomer);

			//Insert into owns, linking accounts, customer
			ResultSet resOwns = statement.executeQuery(insertOwns);				
			System.out.println(insertOwns);

			return "0 " + id + " " + accountType + " " + initialBalance + " " + tin;		

		}catch(Exception e){
			e.printStackTrace();
			return "1 " + e.getMessage();
		}
	}


	/**
	 * Destroy all of the tables in your DB.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	@Override
	public String dropTables(){
		try{
			Statement statement = _connection.createStatement();
			statement.executeQuery("drop table Clients cascade constraints");
			statement.executeQuery("drop table Owns cascade constraints");
			statement.executeQuery("drop table Accounts cascade constraints");
			statement.executeQuery("drop table Transactions cascade constraints");
			statement.executeQuery("drop table Has_Interest cascade constraints");
			statement.executeQuery("drop table Interest cascade constraints");
			statement.executeQuery("drop table Links cascade constraints");
			statement.executeQuery("drop table Checkings cascade constraints");
			statement.executeQuery("drop table Savings cascade constraints");
			statement.executeQuery("drop table Pockets cascade constraints");
			statement.executeQuery("drop table SysMetaData cascade constraints");

			return "0";
		}
		catch(Exception e){
			return "1 " + e.getMessage(); 
		}
	}

	/**
	 * Create all of your tables in your DB.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	@Override
	public String createTables(){
		try{
			Statement statement = _connection.createStatement();

			//Create customer table
			
			/* CLIENTS TABLE */
			statement.executeQuery(
				"create table Clients(" +
							"cid integer," + 
							"name char(20)," + 
							"addr char(20)," + 
							"pin char(32)," + 
							"primary key (cid)" +  
							")" 
			);

			/* ACCOUNTS TABLE */
			statement.executeQuery(
				"create table Accounts(" +
							"aid integer," + 
							"type char(20)," + 
							"bname char(32)," + // primary = 1 if owner is primary else: primary = 0
							"balance real," + 
							"closed integer, " + //closed = 1 if account closed else: primary = 0
							"primary key (aid)" + 
							")" 
			 ); 

			/* OWNS TABLE */
			statement.executeQuery(
				"create table Owns(" +
							"aid integer," + 
							"cid integer," + 
							"primary_owner integer," + // primary = 1 if owner is primary else: primary = 0
							"foreign key (cid) references Clients," + 
							"foreign key (aid) references Accounts," + 
							"primary key (cid, aid)" + 
							")" 

			); 

			/* TRANSACTION TABLE */
			statement.executeQuery(
				"create table Transactions(" +
							"aid1 integer," + 
							"aid2 integer," + 
							"check_num integer," + // primary = 1 if owner is primary else: primary = 0
							"foreign key (aid1) references Accounts," + 
							"foreign key (aid2) references Accounts," + 
							"primary key (aid1, aid2)" + 
							")" 

			); 

			/* INTEREST TABLE */
			statement.executeQuery(
				"create table Interest(" +
							"intr real," + 
							"type char(10)," + 
							"primary key (type)" + //Only need the aid to correctly identify a row 
							")" 

			); 

			/* HAS_INTEREST TABLE */
			statement.executeQuery(
				"create table Has_Interest(" +
							"aid integer," + 
							"type char(10)," + 
							"foreign key (aid) references Accounts," + 
							"foreign key (type) references Interest," + 
							"primary key (aid)" + //Only need the aid to correctly identify a row 
							")" 

			); 

			/* LINKS TABLE */
			statement.executeQuery(
				"create table Links(" +
					"mainAid integer," +
					"linkedAid integer," +
					"isPocket integer," +
					"primary key (mainAid)" +
				")"			
			);


			/* CHECKING TABLE */
			statement.executeQuery(
				"create table Checkings(" +
					"aid integer," +
					"primary key (aid)," +
					"foreign key (aid) references Accounts" +
				")"
			);

			/* Savings TABLE */
			statement.executeQuery(
				"create table Savings(" +
					"aid integer," +
					"primary key (aid)," +
					"foreign key (aid) references Accounts" +
				")"
			);

			/* Pockets TABLE */
			statement.executeQuery(
				"create table Pockets(" +
					"aid integer," +
					"primary key (aid)," +
					"foreign key (aid) references Accounts" +
				")"
			);


			statement.executeQuery(
				"create table SysMetaData(" +
					"year integer,"  +
					"month integer," +
					"day integer, "  +
					"interestAccrued integer," +
					"primary key (year)" + 
				")"
			);

			////////// ASSERTIONS //////////
			/* I'll come back to this later  
			statement.executeQuery(
				"create assertion ONLY_ONE_DATE as CHECK("  +
					"select count(*) from SysMetaData <= 1" + 
				")"
			);
			*/

			return "0";
		}
		catch(Exception e){
			return "1 " + e.getMessage();
		}
	}

	/**
	 * Set system's date.
	 * @param year Valid 4-digit year, e.g. 2019.
	 * @param month Valid month, where 1: January, ..., 12: December.
	 * @param day Valid day, from 1 to 31, depending on the month (and if it's a leap year).
	 * @return a string "r yyyy-mm-dd", where r = 0 for success, 1 for error; and yyyy-mm-dd is the new system's date, e.g. 2012-09-16.
	 */
	@Override
	public String setDate( int year, int month, int day ){

		//Set the format of the date and cast our date to a string
		String strDate = year + "-" + month + "-" + day;
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd");

		try{

			//Create date object to see if the date is valid, else we will throw an error
			Date setDateTo = dateFmt.parse(strDate);

			Statement statement = _connection.createStatement();

			ResultSet resultSet = statement.executeQuery(
				"select interestAccrued from SysMetaData"
			);

			//If the date has yet to be set then we set the default to Jan 15th, 2019
			//Also state that we have yet to accrue interest 
			if (ResSize(resultSet) == 0)
			{
				statement.executeQuery("INSERT INTO SysMetaData " +
															 "VALUES (1, 15, 2019, 0)"
				);

				return "0 2019-1-15";
			}

			else {	
				statement.executeQuery("update SysMetaData  " +
															 "set year = " + year + ", " +
															 		"month = " + month + ", " +
																	  "day = " + day +
																"where year = " + year
				);
				return "0 " + strDate;
			}


		}catch(Exception e){
			e.printStackTrace();
			return "1 " + strDate;
		}	
	}


	/**
	 * Create a new pocket account.
	 * @param id New account's ID.
	 * @param linkedId Linked savings or checking account ID.
	 * @param initialTopUp Initial balance to be deducted from linked account and deposited into new pocket account.
	 * @param tin Existing customer's Tax ID number.  He/She will become the new pocket account's owner.
	 * @return a string "r aid type balance tin", where
	 *         r = 0 for success, 1 for error;
	 *         aid is the new account id;
	 *         type is the new account's type (see the enum codes above);
	 *         balance is the account's initial balance with up to 2 decimal places (e.g. 1000.12, as with %.2f); and
	 *         tin is the Tax ID of account's primary owner.
	 */
	@Override
	public String createPocketAccount( String id, String linkedId, double initialTopUp, String tin ){
		try{

			Statement statement = _connection.createStatement();

			String fmtAmount = String.format("%.2f", initialTopUp);

			//Insert into pockets
			String insertPocket = "INSERT INTO Pockets " +
														"VALUES (" + id + " )";
		
			String insertPocketToAccountTable = "INSERT INTO Accounts " +
																					"(aid, type, balance, closed)" + 
																					"VALUES (" + id + ", " + 
AccountType.POCKET + ", " + fmtAmount + ", 0)";
			//Now insert the account into the linked table to link the accounts
			
			String insertLinks = "INSERT INTO Links " + 
													 "(mainAid, linkedAid, isPocket)" + 
													 "VALUES (" + linkedId + "," + id + "," + "1" + ")";

			statement.executeQuery(insertPocket);
			statement.executeQuery(insertPocketToAccountTable);
			statement.executeQuery(insertLinks);

			//Check if the tin passed in is the primary owner of the account
			
			int isPrimary;

			ResultSet res = statement.executeQuery(
				"SELECT * "  +
				"FROM Owns " +
				"WHERE cid=" + tin + " AND " + "aid=" + linkedId +  " AND primary_owner=1"
			);

			if (ResSize(res) == 0){
				isPrimary = 0;
			}
			else{
				isPrimary = 1;
			}



		}catch(Exception e){
			e.printStackTrace();
			return "1 " + id + " " + AccountType.POCKET + " " + initialTopUp + " " + tin; 
		}
		return "0";
	}

	/**
	 * Create a new customer and link them to an existing checking or saving account.
	 * @param accountId Existing checking or saving account.
	 * @param tin New customer's Tax ID number.
	 * @param name New customer's name.
	 * @param address New customer's address.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	@Override
	public String createCustomer( String accountId, String tin, String name, String address ){
		try {

			//TODO: Change this function when we can read input from the bank teller to determine the pin
			int random = ThreadLocalRandom.current().nextInt(1000, 9999 + 1); //Pick num between 1000 and 9999
			String encryptedPin = hashPin(random);

			Statement statement = _connection.createStatement();
			statement.executeQuery(
				"INSTERT INTO Clients" +
				"VALUES (" + tin + ", " + name + ", " + address + ", " + encryptedPin + ")"
			);

			statement.executeQuery(
				"INSERT INTO Owns" +
				"VALUES (" + accountId + ", " + tin + ", " + "0" + ")" //0 is to specify we are NOT the primary owner
			);

			return "0";
		} catch (Exception e) {
			return "1 " + e.getMessage(); 
		}
	}

	/**
	 * Deposit a given amount of dollars to an existing checking or savings account.
	 * @param accountId Account ID.
	 * @param amount Non-negative amount to deposit.
	 * @return a string "r old new" where
	 *         r = 0 for success, 1 for error;
	 *         old is the old account balance, with up to 2 decimal places (e.g. 1000.12, as with %.2f); and
	 *         new is the new account balance, with up to 2 decimal places.
	 */
	@Override
	public String deposit( String accountId, double amount ){
		return "0";
	}

	/**
	 * Show an account balance (regardless of type of account).
	 * @param accountId Account ID.
	 * @return a string "r balance", where
	 *         r = 0 for success, 1 for error; and
	 *         balance is the account balance, with up to 2 decimal places (e.g. with %.2f).
	 */
	@Override
	public String showBalance( String accountId ){
		return "0";
	}

	/**
	 * Move a specified amount of money from the linked checking/savings account to the pocket account.
	 * @param accountId Pocket account ID.
	 * @param amount Non-negative amount to top up.
	 * @return a string "r linkedNewBalance pocketNewBalance", where
	 *         r = 0 for success, 1 for error;
	 *         linkedNewBalance is the new balance of linked account, with up to 2 decimal places (e.g. with %.2f); and
	 *         pocketNewBalance is the new balance of the pocket account.
	 */
	@Override
	public String topUp( String accountId, double amount ){
		return "0";
	}

	/**
	 * Move a specified amount of money from one pocket account to another pocket account.
	 * @param from Source pocket account ID.
	 * @param to Destination pocket account ID.
	 * @param amount Non-negative amount to pay.
	 * @return a string "r fromNewBalance toNewBalance", where
	 *         r = 0 for success, 1 for error.
	 *         fromNewBalance is the new balance of the source pocket account, with up to 2 decimal places (e.g. with %.2f); and
	 *         toNewBalance is the new balance of destination pocket account, with up to 2 decimal places.
	 */
	@Override
	public String payFriend( String from, String to, double amount ){
		return "0";
	}

	private String hashPin(int pin){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); 
  
			String strPin = Integer.toString(pin);

			// digest() method is called 
			// to calculate message digest of the input string 
			// returned as array of byte 
			byte[] messageDigest = md.digest(strPin.getBytes()); 
	
			// Convert byte array into signum representation 
			BigInteger no = new BigInteger(1, messageDigest); 
	
			// Convert message digest into hex value 
			String hashtext = no.toString(16); 
	
			return hashtext.substring(0,10);
		
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR - UNABLE TO HASH PIN";
		}
	}

	private int ResSize(ResultSet res){

		int resSize = 0;
		try{
			while(res.next()){
				resSize++;
			}
		}catch(Exception e){
			return -1;
		}
		return resSize;
	}
}





