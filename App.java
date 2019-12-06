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
import java.util.Arrays;

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


	/*
	public ResultSet query(String s) throws SQLException{
			Statement statement =_connection.createStatement();
			ResultSet res = statement.executeQuery(s);
			return res;
	}
	*/

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
	 * Generate list of closed accounts.
	 * @return a string "r id1 id2 ... idn", where
	 *         r = 0 for success, 1 for error; and
	 *         id1 id2 ... idn is a list of space-separated closed account IDs.
	 */	
	@Override
	public String listClosedAccounts()
	{

		try{

			//Query all accounts where closed = true
			String queryClosedAccounts = "SELECT aid " + 
																	 "FROM Accounts " + 
																	 "WHERE closed=1";

		
			Statement statement	= _connection.createStatement();

			
			ResultSet res = statement.executeQuery(queryClosedAccounts);

			String idString = "";

			while(res.next()){
				idString = idString + " " + res.getString(1); 
			}

			return "0 " + idString;
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
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
		//int random = ThreadLocalRandom.current().nextInt(1000, 9999 + 1); //Pick num between 1000 and 9999
		int defaultPin = 1717;
		String encryptedPin = hashPin(defaultPin);

		String insertAccounts = "INSERT INTO Accounts " +
														"(aid, type, balance, closed) " +  
														"VALUES (\'" + id + "\', \'" + accountType + "\', " + initialBalance + ", 0" + ")";
	
		String insertOwns = "INSERT INTO Owns " + 
												"VALUES (\'" + id + "\', \'" + tin + "\', "+ 1 + ")";	//prinmary owner (1) cause they created the account
							
		String insertCustomer = "INSERT INTO Clients " +
														"(cid, name, addr, pin) " + 
														"VALUES (\'" + tin + "\', \'" + name + "\', \'" + address + "\', \'" + encryptedPin + "\')";	//primary owner (1) cause they created the account

		String insertAccType;


		if (accountType == AccountType.SAVINGS){
			insertAccType = "INSERT INTO Savings VALUES (\'" + id + "\')";
		}

		else if (accountType == AccountType.INTEREST_CHECKING){	
			insertAccType = "INSERT INTO Checkings VALUES (\'" + id + "\')";
		}

		else{
			System.out.println("App.createCheckingSavingsAccount()::197 - Improper account type");
			return "1";
		}


		try{
			Statement statement = _connection.createStatement();

			//Insert into accounts
			ResultSet resAccs = statement.executeQuery(insertAccounts);				


			ResultSet resAccType = statement.executeQuery(insertAccType);

			//Insert into customer
			ResultSet res = statement.executeQuery(insertCustomer);				
			//System.out.println(insertCustomer);

			//Insert into owns, linking accounts, customer
			ResultSet resOwns = statement.executeQuery(insertOwns);				

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
			// statement.executeQuery("drop table Has_Interest cascade constraints");
			statement.executeQuery("drop table Interest cascade constraints");
			statement.executeQuery("drop table Links cascade constraints");
			statement.executeQuery("drop table Checkings cascade constraints");
			statement.executeQuery("drop table Savings cascade constraints");
			statement.executeQuery("drop table Pockets cascade constraints");
			statement.executeQuery("drop table SysMetaData cascade constraints");

			return "0";
		}
		catch(Exception e){
			e.printStackTrace();
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
							"cid varchar(20)," + 
							"name varchar(20)," + 
							"addr varchar(20)," + 
							"pin varchar(32)," + 
							"primary key (cid)" +  
							")" 
			);

			/* ACCOUNTS TABLE */
			statement.executeQuery(
				"create table Accounts(" +
							"aid varchar(20)," + 
							"type varchar(20)," + 
							"bname varchar(32)," + // primary = 1 if owner is primary else: primary = 0
							"balance real," + 
							"closed integer, " + //closed = 1 if account closed else: primary = 0
							"primary key (aid)" + 
							")" 
			 ); 

			/* OWNS TABLE */
			statement.executeQuery(
				"create table Owns(" +
							"aid varchar(20)," + 
							"cid varchar(20)," + 
							"primary_owner integer," + // primary = 1 if owner is primary else: primary = 0
							"foreign key (cid) references Clients," + 
							"foreign key (aid) references Accounts," + 
							"primary key (cid, aid)" + 
							")" 

			); 

			/* TRANSACTION TABLE */
			statement.executeQuery(
				"create table Transactions(" +
							"aid1 varchar(20)," + 
							"aid2 varchar(20)," + 
							"check_num integer," + // primary = 1 if owner is primary else: primary = 0
							"amount real, " + 
							"year integer, " +
							"month integer, " +
							"day integer, " +
							"transType varchar(32)," +
							"foreign key (aid1) references Accounts," + 
							"foreign key (aid2) references Accounts," + 
							"primary key (aid1, aid2, amount, year, month, day, transType)" + 
							")" 

			); 

			/* INTEREST TABLE */
			statement.executeQuery(
				"create table Interest(" +
							"intr real," + 
							"type varchar(20)," + 
							"primary key (type)" + //Only need the aid to correctly identify a row 
							")" 

			); 

			/* HAS_INTEREST TABLE */
			// statement.executeQuery(
			// 	"create table Has_Interest(" +
			// 				"aid varchar(20)," + 
			// 				"type varchar(20)," + 
			// 				"foreign key (aid) references Accounts," + 
			// 				"foreign key (type) references Interest," + 
			// 				"primary key (aid)" + //Only need the aid to correctly identify a row 
			// 				")" 

			// ); 

			/* LINKS TABLE */
			statement.executeQuery(
				"create table Links(" +
					"mainAid varchar(20)," +
					"linkedAid varchar(20)," +
					"isPocket integer," +
					"primary key (mainAid)," +
					"unique (linkedAid)" + 
				")"			
			);


			/* CHECKING TABLE */
			statement.executeQuery(
				"create table Checkings(" +
					"aid varchar(20)," +
					"primary key (aid)," +
					"foreign key (aid) references Accounts" +
				")"
			);

			/* Savings TABLE */
			statement.executeQuery(
				"create table Savings(" +
					"aid varchar(20)," +
					"primary key (aid)," +
					"foreign key (aid) references Accounts" +
				")"
			);

			/* Pockets TABLE */
			statement.executeQuery(
				"create table Pockets(" +
					"aid varchar(20)," +
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


			setDate(2019, 3, 14);

			return "0";
		}
		catch(Exception e){
			e.printStackTrace();
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
															 "VALUES (3, 14, 2019, 0)"
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
		/* TODO: INSERT ROW IN TRANSACTION ON CREATION OF THE POCKET ACCOUNT */
		try{

			Statement statement = _connection.createStatement();

			String fmtAmount = String.format("%.2f", initialTopUp);

			//Insert into pockets
			String insertPocket = "INSERT INTO Pockets " +
														"(aid) " +
														"VALUES (\'" + id + "\')";
		
			String insertPocketToAccountTable = "INSERT INTO Accounts " +
																					"(aid, type, balance, closed) " + 
																					"VALUES (\'" + id + "\', \'" + AccountType.POCKET + "\', " + fmtAmount + ", 0)";

			//Now insert the account into the linked table to link the accounts
			String insertLinks = "INSERT INTO Links " + 
													 "(mainAid, linkedAid, isPocket)" + 
													 "VALUES (\'" + linkedId + "\',\'" + id + "\'," + "1" + ")";

			//Execute the crafted queries
			statement.executeQuery(insertPocketToAccountTable);
			statement.executeQuery(insertPocket);
			statement.executeQuery(insertLinks);

			//Check if the tin passed in is the primary owner of the account
			int isPrimary;

			ResultSet res = statement.executeQuery(
				"SELECT * "  +
				"FROM Owns " +
				"WHERE cid=\'" + tin + "\' AND " + "aid=\'" + linkedId +  "\' AND primary_owner=1"
			);

			if (ResSize(res) == 0){
				isPrimary = 0;
			}
			else{
				isPrimary = 1;
			}

			//Finally insert the account into owns
			String insertOwns = String.format("INSERT INTO Owns(aid, cid, primary_owner) VALUES (\'%s\', \'%s\', %d)", id, tin, isPrimary);

			statement.executeQuery(insertOwns);

			return "0 " + id +  " "  + linkedId + " " + initialTopUp + " " + tin;

		}catch(Exception e){
			e.printStackTrace();
			return "1 " + id + " " + AccountType.POCKET + " " + initialTopUp + " " + tin; 
		}
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
			//int random = ThreadLocalRandom.current().nextInt(1000, 9999 + 1); //Pick num between 1000 and 9999
			int defaultPin = 1717;
			String encryptedPin = hashPin(defaultPin);

			Statement statement = _connection.createStatement();
			statement.executeQuery(
				"INSERT INTO Clients " +
				"VALUES (\'" + tin + "\', \'" + name + "\', \'" + address + "\', \'" + encryptedPin + "\')"
			);

			////// ASSUME THAT IF ACCOUNTID DOES NOT EXIST IN FOREIGN KEY CONSTRAINT WILL THROW ERROR ////// 
			statement.executeQuery(
				"INSERT INTO Owns " +
				"VALUES (\'" + accountId + "\', \'" + tin + "\', " + "0" + ")" //0 is to specify we are NOT the primary owner
			);

			return "0";
		} catch (Exception e) {
			e.printStackTrace();
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

		/*
		 *	Basic Algorithm
		 *	1) Fetch the account information
		 *	2) From the account information grab the current amount
		 *	3) Store the old account amount 
		 *	4) Add the new amount to the old amount
		 *	5) Update the account information with the new 
		 */

		if (amount < 0.0){
			System.out.println("app.deposit()::581 - Attempting to deposit a negative amount");
			return "1";
		}

		try{
			
			//Init the statement
			Statement statement = _connection.createStatement();

			//Craft the query to be executed
			String getOldAmount = "SELECT A.balance FROM Accounts A WHERE A.aid=\'" + accountId + "\'";

			//Get the amount listed
			ResultSet res = statement.executeQuery(getOldAmount);


			//Check that resultant set not empty
			if (res.next()){
				double oldAmt = Double.parseDouble(res.getString(1));

				//Add new amount to the old amount
				double newAmount = oldAmt + amount;

				//Format as a string
				String fmtAmount    = String.format("%.2f", amount);
				String fmtNewAmount = String.format("%.2f", newAmount);

				//Update the database 
				String updateNewAmount = "UPDATE Accounts A SET A.balance=" + fmtNewAmount + " WHERE A.aid=\'" + accountId + "\'"; 

				//Execute the update query 
				statement.executeQuery(updateNewAmount);

				////// Input into transactions /////
				String insertTransaction = String.format("INSERT INTO Transactions (aid1, aid2, amount, transType) VALUES (\'%s\', \'%s\', %f, \'DEPOSIT\')", accountId, accountId, amount);
				statement.executeQuery(insertTransaction); 

				return "0 " + oldAmt + " " + newAmount;
			}

			//If we fetch no users in the DB we return an error
			else{
				return "1";
			}
		//General DB fuckery 
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
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
	
		String queryBalance = String.format("SELECT A.balance FROM Accounts A WHERE A.aid=\'%s\'",accountId);

		try{
			Statement statement = _connection.createStatement();
			ResultSet res = statement.executeQuery(queryBalance);

			if (res.next()){
				double value     = Double.valueOf(res.getString(1));				
				String formatVal = String.format("%.2f", value);

				return "0 " + formatVal;
			}

			else{
				return "1";
			}

		}catch(Exception e){
			e.printStackTrace();
			return "1 " + e.getMessage();
		}
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

		try{

			String mainAid;
			double fetchedAmount, newMainAmount, newPockAmount, fetchedPockAmount;

			Statement statement = _connection.createStatement();

			//Craft query for main account linked to pocket
			String queryLinkedForMain = String.format(
				"SELECT L.mainAid FROM Links L WHERE L.linkedAid=\'%s\'", 
				accountId
			);

			//String queryLinkedForMain = "SELECT * FROM Links";


			//System.out.println("App.topUp()::690 queryLinkedForMain - " + queryLinkedForMain);
			ResultSet res = statement.executeQuery(queryLinkedForMain);
		


			//Get main account linked to pocket
			if (res.next()){
				mainAid = res.getString(1);
				//System.out.println("Found mainAid of " + mainAid + " from linked account " + accountId);
			}else{
				System.out.println("App.topUp()::696 - Pocket account has no linked main account");
				return "1";
			}
			
			String getAmountFromMainAccounts = String.format(
				"SELECT A.balance FROM Accounts A WHERE A.aid=\'%s\'",
				mainAid
			);

			String getAmountFromPockAccounts = String.format(
				"SELECT A.balance FROM Accounts A WHERE A.aid=\'%s\'",
				accountId
			);

			res = statement.executeQuery(getAmountFromMainAccounts);

			//Get balance of main account
			if (res.next()){
				fetchedAmount = res.getDouble(1);	
				//System.out.println("Fetched amount in main account is " + fetchedAmount);
			}else{
				System.out.println("App.topUp()::724 - Could not find Checkings/Savings account");
				return "1";
			}

			//Get Balance of pocket account
			res = statement.executeQuery(getAmountFromPockAccounts);

			if (res.next()){
				fetchedPockAmount = res.getDouble(1);
				//System.out.println("Fetched amount in pocket account is " + fetchedPockAmount);
			}else{
				System.out.println("App.topUp()::735 - Could not find Pocket account");
				return "1";
			}

			//The amount requested to be moved is more than is in the account
			if (amount > fetchedAmount){
				System.out.println("Error: Move requested more money than in account");
				return "1";
			}			

			newMainAmount = fetchedAmount - amount;
			newPockAmount = fetchedPockAmount + amount;

			String subtractFromMain = String.format(
				"UPDATE Accounts A SET A.balance=%.2f WHERE A.aid=\'%s\'",
				newMainAmount,
				mainAid
			);

			String addToPocket = String.format(
				"UPDATE Accounts A SET A.balance=%.2f WHERE A.aid=\'%s\'",
				newPockAmount,
				accountId
			);


			////// ADD WITHDRAWL AND DEPOSIT TO TRANSACTION LIST //////
			String addTransaction = String.format(
				"INSERT INTO Transactions (aid1, aid2, amount, transType) VALUES (\'%s\', \'%s\', %.2f, \'TOPUP\')",
				mainAid,
				accountId,
				amount
			);

			statement.executeQuery(subtractFromMain);
			statement.executeQuery(addToPocket);
			statement.executeQuery(addTransaction);


			return "0 " + newMainAmount + " " + newPockAmount;

		}catch(Exception e){
			e.printStackTrace();
			return "1 " + e.getMessage();
		}
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

		///////TODO: Determine if we need to verify that the accounts maintain 1 similar co-owner ///// 

		/*
		 *	Algorithm
		 *	1) Ensure both accounts are pocket accounts
		 *	2) Update from with balanceFrom - amount
		 *	3) Update to with balanceTo + amount
		 *	4) Record in transactions
		 *		a) Withdrawl in from
		 *		b) Deposit in to 
		 *
		 */

		try{
			double fromBalance, toBalance;

			Statement statement = _connection.createStatement();

			//printf("SELECT P.aid FROM Pockets P WHERE P.aid=%s OR P.aid=%s", from, to);
			String ensurePockets = String.format(
				"SELECT P.aid FROM Pockets P WHERE P.aid=\'%s\' OR P.aid=\'%s\'",
				from,
				to
			);

			ResultSet res = statement.executeQuery(ensurePockets);

			if (ResSize(res) != 2)
			{
				//We have both AIDs
				return "1";
			}

			String getToBalance= String.format(
				"SELECT A.balance FROM Accounts A WHERE A.aid=\'%s\'",
				to
			);

			String getFromBalance= String.format(
				"SELECT A.balance FROM Accounts A WHERE A.aid=\'%s\'",
				from
			);

			//////// Get the balance of to and from account ////////
			res = statement.executeQuery(getToBalance);

			if (res.next()){
				toBalance = res.getDouble(1);
			}
			else {
				return "1";
			}

			res = statement.executeQuery(getFromBalance);

			if (res.next()){
				fromBalance = res.getDouble(1);
			}
			else {
				return "1";
			}
			//////// End logical block ////////


			if (fromBalance < amount){
				System.out.println("App.payFriend()::866 - Insufficient funds from account " + from);
				return "1";
			}

			double newFromBalance = fromBalance - amount;
			double newToBalance = toBalance + amount;



			String updateFrom= String.format(
				"UPDATE Accounts A SET A.balance=%.2f WHERE A.aid=\'%s\'",
				newFromBalance,
				from
			);

			String updateTo = String.format(
				"UPDATE Accounts A SET A.balance=%.2f WHERE A.aid=\'%s\'",
				newToBalance,
				to
			);

			statement.executeQuery(updateFrom);
			statement.executeQuery(updateTo);

			String insertTransaction = String.format(
				"INSERT INTO Transactions (aid1, aid2, amount, transType) VALUES (\'%s\', \'%s\', %.2f, \'PAYFRIEND\')",
				from,
				to,
				amount
			);

			statement.executeQuery(insertTransaction);

			return String.format("0 %.2f %.2f", newFromBalance, newToBalance);
		}catch(Exception e){
			e.printStackTrace();
			return "1";
		}
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
			e.printStackTrace();
			return -1;
		}
		return resSize;
	}

	//CLIENT VERIFYPIN FUNCTION
	public boolean VerifyPin(int tid, int pin){

		String hashedPin = hashPin(pin);

		String inputPin = String.format(
					"SELECT pin FROM Clients WHERE cid=\'%s\' AND pin=\'%s\'",
					tid,
					hashedPin
				);


		try{
			Statement statement = _connection.createStatement();

			ResultSet res = statement.executeQuery(inputPin);

			//Will return true if something is fetched from the DB otherwise false
			return res.next();


		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

	//CLIENT DEPOSIT FUNCTION
	//3-1-2011 Joe Pepsi deposits $1,200 to account 17431
	//TODO: CHECK IF OUR ACCOUNT IS A CHECKINGS OR SAVINGS
	public void ClientDeposit(int month, int day, int year, String name, double amount, String account)
	{
		//Well probably want to return his total balance maybe - this is why i said int CD
		//If we dont care, well make it boolean

		double newAmount, fetchedAmount;

		//Should be a string?
		String accId = account;
		
		String withdrawTransactions = String.format(
			"INSERT INTO Transactions (aid1, aid2, amount, year, month, day, transType) VALUES (\'%s\', \'%s\', %.2f, %d, %d, %d, \'DEPOSIT\')",
			accId,
			accId,
			amount,
			year,
			month,
			day
		);

		String checkOwner = String.format(
			"SELECT * FROM Accounts A WHERE A.aid IN (SELECT O.aid from Owns O WHERE O.cid IN (SELECT C.cid FROM Clients C WHERE C.name=\'%s\'))",
			name
		);

		String getOldBalance = String.format(
			"SELECT A.balance from Accounts A WHERE A.aid=\'%s\'", 
			accId
		);


		try{
			Statement statement = _connection.createStatement();

			//Check owner and see errors
			ResultSet res = statement.executeQuery(checkOwner);
			if(!res.next()){
				System.out.println("Customer " + name + " does not own account " + accId);
				return;
			}

			//Get old balance and see errors
			res = statement.executeQuery(getOldBalance);
			if (!res.next()){
				System.out.println( "Unable to find balance of account " + accId);
				return;
			}
			else{
				fetchedAmount = res.getDouble(1);	
			}

			newAmount = fetchedAmount + amount;

			String updateAmount = String.format(
				"UPDATE Accounts SET balance=%.2f WHERE aid=\'%s\'", 
				newAmount,
				accId
			);

			//Update amount and log transaction
			statement.executeQuery(updateAmount);
			statement.executeQuery(withdrawTransactions);

			System.out.println( "Owner " + name + " deposited amount " + amount + ". New Amount: " + updateAmount);
			return;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println( "Error! " + e.getMessage());
			return;
		}
	}

	//CLIENT TOP UP FUNCTION
	//3-1-2011 Pit Wilson tops-ups $20 to account 60413 from account 43942
	public void ClientTopup(int month, int day, int year, String name, double amount, String FromAccount, String toAccount){

		return;
	}

	//CLIENT WITHDRAWS FUNCTION
	//3-3-2011 Elizabeth Sailor withdraws $3,000 from account 54321
	public void ClientWithdraw(int month, int day, int year, String name, double amount, String account){

		/*
		 *	Algorithm
		 *	1) Check if name owns account
		 *		a) if not return error
		 *	TODO: CHECK IF OUR ACCOUNT IS A CHECKINGS OR SAVINGS
		 *	2) Check if we have enough money to withdraw from account
		 *		a) if not return error
		 *	3) Get current amount 
		 *	4) Update current amount with current amount - withdrawl amount
		 *	5) Log transaction
		 *
		 *
		 */

		double newAmount, fetchedAmount;

		//Should be a string?
		// String accId = Integer.toString(account);
		String accId = account;

		ArrayList<Integer> date = getDate();

		int _day, _month, _year;
		_day   = date.get(1); /* month index 0, day index 1, year index 2 */
		_month = date.get(0);
		_year  = date.get(2);
		
		String withdrawTransactions = String.format(
			"INSERT INTO Transactions (aid1, aid2, amount, year, month, day, transType) VALUES (\'%s\', \'%s\', %.2f, %d, %d, %d, \'WITHDRAWL\')",
			accId,
			accId,
			-amount,
			_year,
			_month,
			_day
		);

		String checkOwner = String.format(
			"SELECT COUNT(*) FROM Owns O WHERE O.cid IN (SELECT C.cid FROM Clients C WHERE C.name=\'%s\') AND O.aid=\'%s\'", 
			name,
			accId
		);

		String getOldBalance = String.format(
			"SELECT A.balance from Accounts A WHERE A.aid=\'%s\'", 
			accId
		);




		try{

			Statement statement = _connection.createStatement();

			ResultSet res = statement.executeQuery(checkOwner);

			//Check that we own the account
			if(!(res.next() && res.getInt(1) == 1)){
				System.out.println( "Customer " + name + " does not own account " + accId);
				return;
			}

			res = statement.executeQuery(getOldBalance);
				
			if (!res.next()){
				System.out.println( "Unable to find balance of account " + accId);
				return;
			}
			else{
				fetchedAmount = res.getDouble(1);	
			}

			//Customer attemps to withdraw more money than is in their account	
			if (fetchedAmount < amount){
				System.out.println( "Funds low, unable to fetch this amount of money");
				return;
			}

			newAmount = fetchedAmount - amount;

			String updateAmount = String.format(
				"UPDATE Accounts SET balance=%.2f WHERE aid=\'%s\'", 
				newAmount,
				accId
			);

			//Update amount and log transaction
			statement.executeQuery(updateAmount);
			statement.executeQuery(withdrawTransactions);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println( "Error! " + e.getMessage());
			return;
		}

		return;
	}
		

	//CLIENT PURCHASES FUNCTION
	//3-5-2011 David Copperfill purchases $5 from account 53027
	public void ClientPurchase(int month, int day, int year, String name, double amount, String account){
		double newAmount, fetchedAmount;

		//Should be a string?
		// String accId = Integer.toString(account);
		String accId = account;

		ArrayList<Integer> date = getDate();

		int _day, _month, _year;
		_day   = date.get(1); /* month index 0, day index 1, year index 2 */
		_month = date.get(0);
		_year  = date.get(2);


		String withdrawTransactions = String.format(
			"INSERT INTO Transactions (aid1, aid2, amount, year, month, day, transType) VALUES (\'%s\', \'%s\', %.2f, %d, %d, %d, \'PURCHASE\')",
			accId,
			accId,
			-amount,
			_year,
			_month,
			_day
		);

		String checkOwner = String.format(
			"SELECT COUNT(*) FROM Owns O WHERE O.cid IN (SELECT C.cid FROM Clients C WHERE C.name=\'%s\') AND O.aid=\'%s\'", 
			name,
			accId
		);

		String getOldBalance = String.format(
			"SELECT A.balance from Accounts A WHERE A.aid=\'%s\' AND A.aid IN (SELECT P.aid FROM Pockets P)", 
			accId
		);


		try{

			Statement statement = _connection.createStatement();

			ResultSet res = statement.executeQuery(checkOwner);

			//Check that we own the account
			if(!(res.next() && res.getInt(1) == 1)){
				System.out.println( "Customer " + name + " does not own account " + accId);
				return;
			}

			res = statement.executeQuery(getOldBalance);
				
			if (!res.next()){
				System.out.println( "Unable to find balance of account " + accId);
				return;
			}
			else{
				fetchedAmount = res.getDouble(1);	
			}

			//Customer attemps to withdraw more money than is in their account	
			if (fetchedAmount < amount){
				System.out.println( "Funds low, unable to fetch this amount of money");
				return;
			}

			newAmount = fetchedAmount - amount;

			String updateAmount = String.format(
				"UPDATE Accounts SET balance=%.2f WHERE aid=\'%s\'", 
				newAmount,
				accId
			);
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	//CLIENT COLLECTS FUNCTION
	//3-6-2011 Li Kung collects $10 from account 43947 to account 29107
	public void ClientCollects(int month, int day, int year, String name, double amount, String fromAccount, String toAccount){
		return;
	}

	////CLIENT TRANSFERS FUNCTION
	//3-7-2011 Ivan Lendme transfers $289 from account 43942 to account 17431
	public void ClientTransfer(int month, int day, int year, String name, double amount, String fromAccount, String toAccount){       
		return;
	}

	////CLIENT PAYFRIEND FUNCTION
	//3-8-2011 Pit Wilson pay-friends $10 from account 60413 to account 67521
	public void ClientPayfriend(int month, int day, int year, String name, double amount, String fromAccount, String toAccount){
		return;
	}

	////CLIENT PAYFRIEND FUNCTION
        //3-9-2011 Fatal Castro wires $4,000 from account 41725 to account 32156
	public void ClientWire(int month, int day, int year, String name, double amount, String fromAccount, String toAccount){


	}

	/*Given a customer, do the following for each account she owns (including accounts which have closed but have not been deleted): 
	 * 1) generate a list of all transactions which have occurred in the last month.  
	 * 2) This statement should list the names and addresses of all owners of the account.
	 * 3) The  initial  and  final  account  balance  is  to  be  included.   
	 * 4) If  the  sum  of  the  balances  of  theaccounts of which the customer is the primary owner exceeds $100,000, a message should be includedin the statement to warn the customer that the limit of the insurance has been reached.
	 */
	public void GenerateMonthlyStatement(String cid){
		try{
			Statement statement = _connection.createStatement();

			ArrayList<String> aidsOwned = new ArrayList<String>();
			Set<String> commonOwners = new HashSet<String>();

			/* Current date */
			String craftCurrDate = "SELECT (year, month, day) FROM SysMetaData";


			/* Craft query to get all aids of accounts we own */
			String getAidsOwned = String.format(
				"SELECT aid from Owns WHERE cid=\'%s\'",
				cid
			);

			/* put all AIDS into our arrayList */
			ResultSet res = statement.executeQuery(getAidsOwned);
			while(res.next()){
				aidsOwned.add(res.getString(1));
			}

			/* Get all owners from people who own the accounts */
			for (String aid : aidsOwned){
				String getCommonOwners = String.format(
						"SELECT DISTINCT C.name, C.addr " + 
						"FROM Clients C " +
						"WHERE C.cid IN (SELECT DISTINCT O.cid FROM Owns O WHERE O.aid=\'%s\')",
						aid
				);

				/* Get the common owners of the account */
				res = statement.executeQuery(getCommonOwners);
				while(res.next()){
					String toAdd = res.getString(1) + " " + res.getString(2);
					commonOwners.add(toAdd);
				}


				System.out.println("****** Account " + aid + " ******");

				/* Print out common owners */
				for (String s : commonOwners){
					System.out.println(" Owner : " + s);
				}
				System.out.println("\n");


				/* GET THE DATE */
				ArrayList<Integer> date = getDate();

				int day, currMonth, prevMonth;
				day = date.get(1); /* month index 0, day index 1, year index 2 */
				currMonth = date.get(0);
				prevMonth = currMonth - 1;



				/*TODO: FIX THIS TO ONLY GRAB FROM THE CURRENT MONTH */
				String getAllTransactions = String.format(
					"SELECT * FROM Transactions WHERE aid1=\'%s\' OR aid2=\'%s\' AND month=%d AND day >= %d " + 
					"UNION " + 
					"SELECT * FROM Transactions WHERE aid1=\'%s\' OR aid2=\'%s\' AND month=%d AND day <= %d" ,
					aid, /*aid1 */
					aid, /*adi2 */
					prevMonth,
					day,
					aid,
					aid,
					currMonth,
					day
				);

				//System.out.println("SYSDATE " + getAllTransactions);

				/* GET ALL TRANSACTIONS - NEEDS TO BE TWEAKED FOR THE PREVIOUS MONTH */
				res = statement.executeQuery(getAllTransactions);
				while(res.next()){
					String to     = res.getString(1);
					String from   = res.getString(2);
					String check  = res.getString(3); 
					String amt    = res.getString(4);
					String year   = res.getString(5);
					String month  = res.getString(6);
					String dayStr = res.getString(7); /*Already defined when we call date */
 
					if(check == null)
						check = "NULL";

					String printStr = year + "-" + month + "-" + dayStr + " " + to + " " + from + " " + amt + " " + check;

					System.out.println(printStr);

				}

				commonOwners.clear();
			}


			/* GET PREVIOUS MONTH */
			int year, month, day;
			String getCurrentDate = "SELECT year, month, day FROM SysMetaData";
			res = statement.executeQuery(getCurrentDate);

			if(res.next()){
				year  = res.getInt(1);
				month = res.getInt(2);
				day   = res.getInt(3);
			}
			else{
				System.out.println("GenerateMonthlyStatement()::1272 - Unable to get current date!");
				return;
			}


			/* Get the current and start balance */
			for(String aid : aidsOwned){
				double oldBalance = 0.0;

				/* Call show balance and parse from string to double */
				String stringBalance = showBalance(aid);
				String[] doubleList = stringBalance.split("\\s+");

				/* Current balance */
				String currBalance    = doubleList[doubleList.length-1];
				double valCurrBalance = Double.valueOf(currBalance); 

				/* Old balance */
				String getAllLosses = String.format(
					"SELECT SUM(T.amount) FROM Transactions T WHERE T.aid1=\'%s\' AND T.aid2 <> \'%s\'",
					aid,
					aid
				);

				String getAllGains = String.format(
					"SELECT SUM(T.amount) FROM Transactions T WHERE T.aid1 <> \'%s\' AND T.aid2=\'%s\'",
					aid, 
					aid
				);

				String getAllSelfTransfers = String.format(
					"SELECT SUM(T.amount) FROM Transactions T WHERE T.aid1=\'%s\' AND T.aid2=\'%s\'",
					aid, 
					aid
				);



				res = statement.executeQuery(getAllLosses);
				if (res.next()){
					oldBalance -= res.getDouble(1);
				}
				res = statement.executeQuery(getAllGains);
				if (res.next()){
					oldBalance += res.getDouble(1);
				}
				res = statement.executeQuery(getAllSelfTransfers);
				if (res.next()){
					oldBalance += res.getDouble(1);
				}

				oldBalance = valCurrBalance - oldBalance;

				System.out.println("Account: " + aid + " ... current balance: " + currBalance + " ... old balance: " + oldBalance);
			}



			/* Check if the balance of all accounts that I am the primary owner of is over 100,000 */
			String queryTotalBalance = String.format(
				"SELECT SUM(A.balance) FROM Accounts A WHERE A.aid IN (SELECT O.aid FROM Owns O WHERE O.primary_owner=1 AND O.cid=\'%s\')",
				cid
			);


			res = statement.executeQuery(queryTotalBalance);


			if(res.next() && res.getDouble(1) > 100000)
				System.out.println("############# WARNING: NO LONGER INSURED PASSED $100,000 ############# ");


		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	//For all open accounts, add the appropriate amount of monthly interest to the balance. 
	//Thereshould be a record in your database that interest has been added this month. 
	//So a repeated “Add Interest”transaction would report a warning and do nothing else.
	public void AddInterest(){
		return;
	}

	//Generate a list of all accounts associated with a customer and indicate whether the accounts are opened or closed 
	public void GenerateCustomerReport(String cid){		
		try {
			
			ArrayList<String> accountInfo = new ArrayList<String>();

			String getAllAccounts = String.format(
				"SELECT * FROM Accounts A WHERE A.aid IN (SELECT O.aid FROM Owns O WHERE O.cid=\'%s\')",
				cid
			);
			
			Statement statement = _connection.createStatement();
			ResultSet res = statement.executeQuery(getAllAccounts);

			System.out.println("\n###### CUSTOMER REPORT FOR " + cid + " ######");

			while (res.next()){
				String aid, type, bname;
				double balance;
				int closed;
				aid = res.getString(1);
				type = res.getString(2);
				bname = res.getString(3);
				balance = res.getDouble(4);
				closed = res.getInt(5);

				System.out.println(" " + aid + " " + type + " " + bname + " " + balance + " " + closed);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return;
	}


	/* By federal law, deposits over $10,000 for a single customer in all (owned or jointly owned) accounts within one month must be reported to the government. 
	 * Generate a list of all customers which have a sum of deposits, transfers and wires duringthe current month, over all owned accounts (active or closed), of over $10,000. 
	 * (How to handle joint accounts?)
	 */
	public void DTER(){
		try{
			Statement statement = _connection.createStatement();




		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	public void CloseAccount(String aid, double balance, String bname, String accType, String owners, String linked)
	{
		return;
	}
	
	public void DeleteTransaction()
	{
		System.out.println("DROPPING TABLES");
		return;
	}	

	public void CreateAccount(String aid, double balance, String bname, String accType, String owners, String linked){
		 try {

		// 	//TODO: 
		// 	//1. Split Owners into substrings
		// 	//2. Insert account details into account table
		// 	//3. For loop through Owns table to insert account with each owner

			int isclosed = 0;
			String[] ownerCSV = owners.split(",");

			for(int i = 0 ; i < ownerCSV.length; i++){
				ownerCSV[i] = ownerCSV[i].strip();
			}


			ArrayList<String> ownerCids = new ArrayList<String>();

			Statement statement = _connection.createStatement();
			ResultSet res;


			/* Get all CIDS from owner names */
			for (int i = 0; i < ownerCSV.length; i++){
				String formatstring = String.format("SELECT C.cid FROM Clients C WHERE C.name=\'%s\'", ownerCSV[i]);
				res = statement.executeQuery(formatstring);
				
				while(res.next()){
					ownerCids.add(res.getString(1));
				}
			}

			String insertAccounts = String.format(
				"INSERT INTO Accounts (aid, type, bname, balance, closed) VALUES (\'%s\', \'%s\', \'%s\', %.2f, %d)",
				aid,
				accType,
				bname,
				balance,
				0
			);
			statement.executeQuery(insertAccounts);

			int isPrimary;
			for (int i =0; i < ownerCids.size(); i++)
			{
				if (i == 0){
					isPrimary = 1;
				}
				else {
					isPrimary = 0;
				}
				String insertOwns = String.format(
					"INSERT INTO Owns (aid, cid, primary_owner) VALUES (\'%s\', \'%s\', %d)",
						aid,
						ownerCids.get(i),
						isPrimary
				);


				statement.executeQuery(insertOwns);
			}


			if (accType.equals("Pocket"))
			{
				String insertPocket = String.format(
				"INSERT INTO Pockets (aid) VALUES (\'%s\')",
				aid);
				statement.executeQuery(insertPocket);

				String insertPocketLinks = String.format(
				"INSERT INTO Links (mainAid, linkedAid, isPocket) VALUES (\'%s\', \'%s\', %d)",
				aid,
				linked,
				1
				);
				statement.executeQuery(insertPocketLinks);
			}

			if(accType.equals("Savings"))
			{
				String insertSavings = String.format(
				"INSERT INTO Savings (aid) VALUES (\'%s\')",
				aid);
				statement.executeQuery(insertSavings);
			}

			if (accType.equals("Student-Checking")){
				String insertStudentChecking = String.format(
				"INSERT INTO Checkings (aid) VALUES (\'%s\')",
				aid);
				statement.executeQuery(insertStudentChecking);
			}

			if (accType.equals("Interest-Checking"))
			{
				String insertInterestChecking = String.format(
				"INSERT INTO Checkings (aid) VALUES (\'%s\')",
				aid);
				statement.executeQuery(insertInterestChecking);
			}

			ArrayList<Integer> date = getDate();

			int day, month, year;
			day   = date.get(1); /* month index 0, day index 1, year index 2 */
			month = date.get(0);
			year  = date.get(2);

			String InitialDeposit = String.format(
				"INSERT INTO Transactions (aid1, aid2, amount, year, month, day, transType) VALUES (\'%s\', \'%s\', %.2f, %d, %d, %d, \'DEPOSIT\')",
				aid,
				aid,
				balance,
				year,
				month,
				day
			);
			statement.executeQuery(InitialDeposit);

			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void DeleteAccount(String aid, double balance, String bname, String accType, String owners, String linked){
		// try {

		// 	//TODO: 
		// 	//1. Split Owners into substrings
		// 	//2. Insert account details into account table
		// 	//3. For loop through Owns table to insert account with each owner

		// 	int isclosed = 0;
		// 	String[] ownerCSV = owners.split(",");

		// 	// "SELECT COUNT(*) FROM Owns O WHERE O.cid=\'(SELECT C.cid FROM Clients C WHERE C.name=\'%s\')\' AND O.aid=\'%s\'", 
		// 	// name,
		// 	// accId

		// 	Statement statement = _connection.createStatement();
		// 	String DeleteAccounts = String.format(
		// 		"DELETE * FROM Accounts A WHERE A.aid=\'%s\'",
		// 		aid
		// 	);
		// 	statement.executeQuery(DeleteAccounts);


		// 	for (int i =0; i < ownerCSV.length; i++)
		// 	{
		// 		String DeleteOwns = String.format(
		// 			"DELETE * FROM Owns O WHERE O.aid=\'%s\' AND O.cid=\'%s\'",
		// 				aid,
		// 				ownerCSV[i]
		// 		);

		// 		System.out.println(ownerCSV[i]);
		// 		statement.executeQuery(DeleteOwns);
		// 	}

		// 	if (accType.equals("Pocket"))
		// 	{
		// 		String DeletePocket = String.format(
		// 		"DELETE * FROM Pocket A WHERE A.aid=\'%s\'",
		// 		aid);
		// 		statement.executeQuery(DeletePocket);
		// 	}

		// 	if(accType.equals("Savings"))
		// 	{
		// 		String DeleteSavings = String.format(
		// 		"DELETE * FROM Savings A WHERE A.aid=\'%s\'",
		// 		aid);
		// 		statement.executeQuery(DeleteSavings);
		// 	}

		// 	if (accType.equals("Student-Checking")){
		// 		String DeleteStudentChecking = String.format(
		// 		"DELETE * FROM Checking A WHERE A.aid=\'%s\'",
		// 		aid);
		// 		statement.executeQuery(DeleteStudentChecking);
		// 	}

		// 	if (accType.equals("Interest-Checking"))
		// 	{
		// 		String DeleteInterestChecking = String.format(
		// 		"DELETE * FROM Checking A WHERE A.aid=\'%s\'",
		// 		aid);
		// 		statement.executeQuery(DeleteInterestChecking);
		// 	}

		// 	return;
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// 	return;
		// }
	}

	public void setInterestRate(String AccType, Double InterestRate)
	{
		System.out.println("ENTERED INTEREST RATE FUNCTION");
		System.out.println("ACCOUNT TYPE IS: " + AccType +" AND INTEEST RATE IS: " + InterestRate);
	}

	public void createDummyValues(){
		try{

			String cid, name, addr, pin;

			Statement statement = _connection.createStatement();

			/// Customer 1: 361721022 Alfred Hitchcock 6667 El Colegio #40 1234 ///
			cid = "361721022";
			name = "Alfred Hitchcock";
			addr = "6667 El Colegio #40";
			pin = hashPin(1234);

			String insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);


			/// Customer 2: 231403227 Billy Clinton 5777 Hollister 1468 ///
			cid = "231403227";
			name = "Billy Clinton";
			addr = "5777 Hollister";
			pin = hashPin(1468);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);


			/// Customer 3: 412231856 Cindy Laugher 7000 Hollister 3764 ///
			cid = "412231856";
			name = "Cindy Laugher";
			addr = "7000 Hollister";
			pin = hashPin(3764);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

			/// Customer 4: 207843218 David Copperfill 1357 State St 8582 ///
			cid = "207843218";
			name = "David Copperfill";
			addr = "1357 State St";
			pin = hashPin(8582);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

			/// Customer 5: 122219876 Elizabeth Sailor 4321 State St 3856///
			cid = "122219876";
			name = "Elizabeth Sailor";
			addr = "4321 State St";
			pin = hashPin(3856);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

			/// Customer 6: 401605312 Fatal Castro 3756 La Cumbre Plaza 8193 ///
			cid = "401605312";
			name = "Fatal Castro";
			addr = "3756 La Cumbre Plaza";
			pin = hashPin(8193);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

				/// Customer 7: 201674933 George Brush 5346 Foothill Av 9824 ///
			cid = "201674933";
			name = "George Brush";
			addr = "5346 Foothill Av";
			pin = hashPin(9824);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);


			/// Customer 8:  212431965 Hurryson Ford 678 State St 3532///
			cid = "212431965";
			name = "Hurryson Ford";
			addr = "678 State St";
			pin = hashPin(3532);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

			/// Customer 9:  322175130 Ivan Lendme 1235 Johnson Dr 8471///
			cid = "322175130";
			name = "Ivan Lendme";
			addr = "1235 Johnson Dr";
			pin = hashPin(8471);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

				/// Customer 10:  344151573 Joe Pepsi 3210 State St 3692///
			cid = "344151573";
			name = "Joe Pepsi";
			addr = "3210 State St";
			pin = hashPin(3692);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

			/// Customer 11:  209378521 Kelvin Costner Santa Cruz #3579 4659///
			cid = "209378521";
			name = "Kelvin Costner";
			addr = "Santa Cruz #3579";
			pin = hashPin(4659);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

					/// Customer 12:  212116070 Li Kung 2 People's Rd Beijing 9173///
			cid = "212116070";
			name = "Li Kung";
			addr = "2 Peoples Rd Beijing";
			pin = hashPin(9173);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

			/// Customer 13:  188212217 Magic Jordon 3852 Court Rd 7351///
			cid = "188212217";
			name = "Magic Jordon";
			addr = "3852 Court Rd";
			pin = hashPin(7351);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

				/// Customer 14:  203491209 Nam-Hoi Chung 1997 People's St HK 5340///
			cid = "203491209";
			name = "Nam-Hoi Chung";
			addr = "1997 Peoples St HK";
			pin = hashPin(5340);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

				/// Customer 15: 210389768 Olive Stoner 6689 El Colegio #151 8452 ///
			cid = "210389768";
			name = "Olive Stoner";
			addr = "6689 El Colegio #151";
			pin = hashPin(8452);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);

				/// Customer 16: 400651982 Pit Wilson 911 State St 1821 ///
			cid = "400651982";
			name = "Pit Wilson";
			addr = "911 State St";
			pin = hashPin(1821);

			insert = String.format(
				"INSERT INTO Clients (cid, name, addr, pin) VALUES (\'%s\', \'%s\', \'%s\',\'%s\')",
				cid,
				name,
				addr,
				pin
			);
			statement.executeQuery(insert);
			//
			//END OF CUSTOMER INSERTION
			//


			//
			//BEGIN ACCOUNT INSERTION
			//
			String aid = "";
			String AccType = "";
			String bname = "";
			double balance = 1000.00;
			String owners = "";
			String linked = "";

				/// Account 1:  17431 Student-Checking San Francisco Joe Pepsi
															// Cindy Laugher
															// Ivan Lendme///
			aid = "17431";
			AccType = "Student-Checking";
			bname = "San Francisco";
			owners = "Joe Pepsi, Cindy Laugher, Ivan Lendme";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);


				/// Account 2:  54321 Student-Checking Los Angeles Hurryson Ford
																	// Cindy Laugher
																	// Elizabeth Sailor
																	// Nam-Hoi Chung
			aid = "54321";
			AccType = "Student-Checking";
			bname = "Los Angeles";
			owners = "Hurryson Ford, Cindy Laugher, Elizabeth Sailor, Nam-Hoi Chung";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

				/// Account 3: 12121 Student-Checking Goleta David Copperfill
			aid = "12121";
			AccType = "Student-Checking";
			bname = "Goleta";
			owners = "David Copperfill";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

							/// Account 4: 41725 Interest-Checking Los Angeles George Brush
																			// Fatal Castro
																			// Billy Clinton
			aid = "41725";
			AccType = "Interest-Checking";
			bname = "Los Angeles";
			owners = "George Brush, Fatal Castro, Billy Clinton";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

							/// Account 5: 76543 Interest-Checking Santa Barbara Li Kung
																			// Magic Jordon
			aid = "76543";
			AccType = "Interest-Checking";
			bname = "Santa Barbara";
			owners = "Li Kung, Magic Jordon";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

										/// Account 6: 93156 Interest-Checking Goleta Kelvin Costner
																						// Magic Jordon
																						// Olive Stoner
																						// Elizabeth Sailor
																						// Nam-Hoi Chung
			aid = "93156";
			AccType = "Interest-Checking";
			bname = "Goleta";
			owners = "Kelvin Costner, Magic Jordon, Olive Stoner, Elizabeth Sailor, Nam-Hoi Chung";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

										/// Account 7: 43942 Savings Santa Barbara Alfred Hitchcock
																						// Pit Wilson
																						// Hurryson Ford
																						// Ivan Lendme
			aid = "43942";
			AccType = "Savings";
			bname = "Santa Barbara";
			owners = "Alfred Hitchcock, Pit Wilson, Hurryson Ford, Ivan Lendme";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

									/// Account 8: 29107 Savings Los Angeles Kelvin Costner
																					// Li Kung
																					// Olive Stoner
			aid = "29107";
			AccType = "Savings";
			bname = "Los Angeles";
			owners = "Kelvin Costner, Li Kung, Olive Stoner";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);


												/// Account 9: 19023 Savings San Francisco Cindy Laugher
																							// George Brush
																							// Fatal Castro
			aid = "19023";
			AccType = "Savings";
			bname = "San Francisco";
			owners = "Cindy Laugher, George Brush, Fatal Castro";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);	


												/// Account 10: 32156 Savings Goleta Magic Jordon
																					// David Copperfill
																					// Elizabeth Sailor
																					// Joe Pepsi
																					// Nam-Hoi Chung
																					// Olive Stoner
			aid = "32156";
			AccType = "Savings";
			bname = "Goleta";
			owners = "Magic Jordon, David Copperfill, Elizabeth Sailor, Joe Pepsi, Nam-Hoi Chung, Olive Stoner";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

												/// Account 11: 53027 Pocket (12121) Goleta David Copperfill
			aid = "53027";
			linked = "12121";
			AccType = "Pocket";
			bname = "Goleta";
			owners = "David Copperfill";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

												/// Account 12: 43947 Pocket (29107) Isla Vista Li Kung
			aid = "43947";
			linked = "29107";
			AccType = "Pocket";
			bname = "Isla Vista";
			owners = "Li Kung";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

			/// Account 13: 60413 Pocket (43942) Santa Cruz Pit Wilson
			aid = "60413";
			linked = "43942";
			AccType = "Pocket";
			bname = "Santa Cruz";
			owners = "Pit Wilson";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);

			/// Account 14: 67521 Pocket (19023) Santa Barbara Fatal Castro
			aid = "67521";
			linked = "19023";
			AccType = "Pocket";
			bname = "Santa Barbara";
			owners = "Fatal Castro";
			CreateAccount( aid,  balance,  bname,  AccType,  owners, linked);


			//
			//INSERT TRANSACTIONS NOW
			//

			String toAID;
			String fromAID;
			double amount;
			int year;
			int month;
			int day;

			/// Transactions 1: 3-1-2011 Joe Pepsi deposits $1,200 to account 17431
			toAID = "17431";
			amount = 1200;
			name = "Joe Pepsi";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

			/// Transactions 2: 3-1-2011 Hurryson Ford deposits $21,000 to account 54321
			toAID = "54321";
			amount = 21000;
			name = "Hurryson Ford";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);


			/// Transactions 3: 3-1-2011 David Copperfill deposits $1,200 to account 12121
			toAID = "12121";
			amount = 1200;
			name = "David Copperfill";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);


			/// Transactions 4: 3-1-2011 George Brush deposits $15,000 to account 41725
			toAID = "41725";
			amount = 15000;
			name = "George Brush";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

						/// Transactions 5:3-1-2011 Kelvin Costner deposits $2,000,000 to account 93156
			toAID = "93156";
			amount = 2000000;
			name = "Kelvin Costner";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

							/// Transactions 6:3-1-2011 David Copperfill top-ups $50 to account 53027 from account 12121
			toAID = "53027";
			fromAID = "12121";
			amount = 50;
			name = "David Copperfill";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientTopup(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 7:Alfred Hitchcock deposits $1,289 to account 43942
			toAID = "43942";
			amount = 1289;
			name = "Alfred Hitchcock";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

			/// Transactions 8:  Kelvin Costner deposits $34,000 to account 29107
			toAID = "29107";
			amount = 34000;
			name = "Kelvin Costner";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

			/// Transactions 9:  Cindy Laugher deposits $2,300 to account 19023
			toAID = "19023";
			amount = 2300;
			name = "Cindy Laugher";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

			/// Transactions 10:  Pit Wilson tops-ups $20 to account 60413 from account 43942
			toAID = "60413";
			fromAID = "43942";
			amount = 20;
			name = "Pit Wilson";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientTopup(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 11:  Magic Jordon deposits $1,000 to account 32156

			toAID = "32156";
			amount = 1000;
			name = "Magic Jordon";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);


			/// Transactions 12:  Li Kung deposits $8,456 to account 76543

			toAID = "76543";
			amount = 8456;
			name = "Li Kung";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

			/// Transactions 13:  Li Kung top-ups $30 to account 43947 from account 29107
			toAID = "43947";
			fromAID = "29107";
			amount = 30;
			name = "Li Kung";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientTopup(month, day, year, name, amount, fromAID, toAID);

						/// Transactions 13:  Fatal Castro top-ups $100 to account 67521 from account 19023
			toAID = "67521";
			fromAID = "19023";
			amount = 100;
			name = "Fatal Castro";
			month = 3;
			day = 1;
			year = 2011;
			
			ClientTopup(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 12:  3-2-2011 Joe Pepsi deposits $8,800 to account 17431

			toAID = "17431";
			amount = 8800;
			name = "Joe Pepsi";
			month = 3;
			day = 2;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);


			/// Transactions 13:  3-3-2011 Elizabeth Sailor withdraws $3,000 from account 54321

			toAID = "54321";
			amount = 3000;
			name = "Elizabeth Sailor";
			month = 3;
			day = 3;
			year = 2011;
			
			ClientWithdraw(month, day, year, name, amount, toAID);

			/// Transactions 14: 3-5-2011 Li Kung withdraws $2,000 from account 76543

			toAID = "76543";
			amount = 2000;
			name = "Li Kung";
			month = 3;
			day = 5;
			year = 2011;
			
			ClientWithdraw(month, day, year, name, amount, toAID);


			/// Transactions 15: 3-5-2011 David Copperfill purchases $5 from account 53027

			toAID = "53027";
			amount = 5;
			name = "David Copperfill";
			month = 3;
			day = 5;
			year = 2011;
			
			ClientPurchase(month, day, year, name, amount, toAID);

			/// Transactions 16: 3-6-2011 Magic Jordon withdraws $1,000,000 from account 93156

			toAID = "93156";
			amount = 1000000;
			name = "Magic Jordon";
			month = 3;
			day = 6;
			year = 2011;
			
			ClientPurchase(month, day, year, name, amount, toAID);


			/// Transactions 17: 3-6-2011 Kelvin Costner writes a check in amount of $950,000 from account 93156

			toAID = "93156";
			amount = 1000000;
			name = "Kelvin Costner";
			month = 3;
			day = 6;
			year = 2011;
			
			ClientWire(month, day, year, name, amount, toAID, toAID);

			/// Transactions 18: 3-6-2011 Li Kung withdraws $4,000 from account 29107

			toAID = "29107";
			amount = 4000;
			name = "Li Kung";
			month = 3;
			day = 6;
			year = 2011;
			
			ClientWithdraw(month, day, year, name, amount, toAID);

			/// Transactions 19: 3-6-2011 Li Kung collects $10 from account 43947 to account 29107

			toAID = "29107";
			fromAID = "43947";
			amount = 10;
			name = "Li Kung";
			month = 3;
			day = 6;
			year = 2011;
			
			ClientCollects(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 20: 3-6-2011 Li Kung top-ups $30 to account 43947 from account 29107

			toAID = "43947";
			fromAID = "29107";
			amount = 30;
			name = "Li Kung";
			month = 3;
			day = 6;
			year = 2011;
			
			ClientTopup(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 21: 3-7-2011 Ivan Lendme transfers $289 from account 43942 to account 17431

			toAID = "17431";
			fromAID = "43942";
			amount = 289;
			name = "Ivan Lendme";
			month = 3;
			day = 7;
			year = 2011;
			
			ClientTransfer(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 22: 3-7-2011 Pit Wilson withdraws $289 from account 43942
			toAID = "43942";
			fromAID = "43942";
			amount = 289;
			name = "Pit Wilson";
			month = 3;
			day = 7;
			year = 2011;
			
			ClientTransfer(month, day, year, name, amount, fromAID, toAID);

			ClientTransfer(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 23: 3-8-2011 Pit Wilson pay-friends $10 from account 60413 to account 67521
			toAID = "67521";
			fromAID = "60413";
			amount = 10;
			name = "Pit Wilson";
			month = 3;
			day = 8;
			year = 2011;
			
			ClientPayfriend(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 24: 3-8-2011 Olive Stoner deposits $50,000 to account 93156
			toAID = "93156";
			fromAID = "93156";
			amount = 50000;
			name = "Olive Stoner";
			month = 3;
			day = 8;
			year = 2011;
			
			ClientDeposit(month, day, year, name, amount, toAID);

			/// Transactions 25: 3-8-2011 David Copperfill writes a check in the amount of $200 from account
																									//12121
			toAID = "12121";
			fromAID = "12121";
			amount = 200;
			name = "David Copperfill";
			month = 3;
			day = 8;
			year = 2011;
			
			ClientWire(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 26: 3-8-2011 George Brush transfers $1,000 from account 41725 to account 19023
																									
			toAID = "19023";
			fromAID = "41725";
			amount = 1000;
			name = "George Brush";
			month = 3;
			day = 8;
			year = 2011;
			
			ClientTransfer(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 27: 3-9-2011 Fatal Castro wires $4,000 from account 41725 to account 32156
																									
			toAID = "32156";
			fromAID = "41725";
			amount = 4000;
			name = "Fatal Castro";
			month = 3;
			day = 9;
			year = 2011;

			ClientWire(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 28: 3-9-2011 David Copperfill pay-friends $10 from account 53027 to account 60413
																									
			toAID = "60413";
			fromAID = "53027";
			amount = 10;
			name = "David Copperfill";
			month = 3;
			day = 9;
			year = 2011;

			ClientPayfriend(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 29: 3-10-2011 Pit Wilson purchases $15 from account 60413
																									
			toAID = "60413";
			fromAID = "60413";
			amount = 15;
			name = "Pit Wilson";
			month = 3;
			day = 10;
			year = 2011;

			ClientPurchase(month, day, year, name, amount, fromAID);

			/// Transactions 30: 3-12-2011 Nam-Hoi Chung withdraws $20,000 from account 93156
																									
			toAID = "93156";
			fromAID = "93156";
			amount = 20000;
			name = "Nam-Hoi Chung";
			month = 3;
			day = 12;
			year = 2011;

			ClientWithdraw(month, day, year, name, amount, toAID);

			/// Transactions 31: 3-12-2011 Magic Jordon writes a check in the amount of $456 from account 76543
																									
			toAID = "76543";
			fromAID = "76543";
			amount = 456;
			name = "Magic Jordon";
			month = 3;
			day = 12;
			year = 2011;

			ClientWire(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 32: 3-12-2011 Fatal Castro top-ups $50 to account 67521 from account 19023
																									
			toAID = "67521";
			fromAID = "19023";
			amount = 50;
			name = "Fatal Castro";
			month = 3;
			day = 12;
			year = 2011;

			ClientTopup(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 33: 3-14-2011 Fatal Castro pay-friends $20 from account 67521 to account 53027
																									
			toAID = "53027";
			fromAID = "67521";
			amount = 20;
			name = "Fatal Castro";
			month = 3;
			day = 14;
			year = 2011;

			ClientPayfriend(month, day, year, name, amount, fromAID, toAID);

			/// Transactions 34: 3-14-2011 Li Kung collects $15 from account 43947 to account 29107
																									
			toAID = "29107";
			fromAID = "43947";
			amount = 15;
			name = "Li Kung";
			month = 3;
			day = 14;
			year = 2011;

			ClientCollects(month, day, year, name, amount, fromAID, toAID);








		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	public ArrayList<Integer> getDate(){
		try{
			Statement statement = _connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT year, month, day FROM SysMetaData");

			ArrayList<Integer> retList = new ArrayList<Integer>();

			if(res.next()){
				retList.add(res.getInt(1));
				retList.add(res.getInt(2));
				retList.add(res.getInt(3));

				return retList;
			}

			return new ArrayList<Integer>();

		}catch(Exception e){
			return new ArrayList<Integer>();
		}
	}

}





