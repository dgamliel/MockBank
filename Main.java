package cs174a; // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// DO NOT REMOVE THIS IMPORT.
import cs174a.Testable.*;
import cs174a.*;
/**
 * This is the class that launches your application.
 * DO NOT CHANGE ITS NAME.
 * DO NOT MOVE TO ANY OTHER (SUB)PACKAGE.
 * There's only one "main" method, it should be defined within this Main class, and its signature should not be changed.
 */
public class Main
{
	/**
	 * Program entry point.
	 * DO NOT CHANGE ITS NAME.
	 * DON'T CHANGE THE //!### TAGS EITHER.  If you delete them your program won't run our tests.
	 * No other function should be enclosed by the //!### tags.
	 */
	//!### COMENZAMOS
	public static void main( String[] args )
	{
		System.out.println("APP STARTED");
		App app = new App();                        // We need the default constructor of your App implementation.  Make sure such
													// constructor exists.
		String r = app.initializeSystem();          // We'll always call this function before testing your system.
		if( r.equals( "0" ) )
		{
			app.exampleAccessToDB();                // Example on how to connect to the DB.

			// Example tests.  We'll overwrite your Main.main() function with our final tests.

			System.out.println("\n\n///////////// MY TESTS /////////////\n");
			// Create tables
			r = app.createTables();
			System.out.println("CREATE TABLES " + r);
			
			//Create dummy checkings Savings Accounts
			r = app.createCheckingSavingsAccount( AccountType.INTEREST_CHECKING, "acc1", 1000.00, "1", "Im YoungMing", "Known" );
			System.out.println( "CREATE ACCOUNT ACC1 - " + r );
			
			//Create new seperate checking account
			r = app.createCheckingSavingsAccount( AccountType.INTEREST_CHECKING, "acc2", 1000.00, "2", "Danny Dave", "Gangy Wangy Lane" );
			System.out.println( "CREATE ACCOUNT ACC2 - " + r );

			// Test create pocket account to link to acc1
			r = app.createPocketAccount("pocket1", "acc1", 1000.00, "1");
			System.out.println("CREATE POCKET ACCOUNT pocket1 - " + r );
			
			r = app.createPocketAccount("pocket2", "acc2", 1000.00, "2");
			System.out.println("CREATE POCKET ACCOUNT pocket2 - " + r );

			// Test Create customer to co-own account "acc1"
			r = app.createCustomer("acc1", "customer1", "Chumbo Wumbo", "1919 Gang Lane");
			System.out.println("CREATING NEW CUSTOMER TO CO-OWN ACCOUNT \"acc1\" - " + r);


			// Test Show balance before deposit
			r = app.showBalance("acc1");
			System.out.println( "SHOW BALANCE ACC1 - " + r );


			r = app.showBalance("acc2");
			System.out.println( "SHOW BALANCE ACC2 - " + r );

			// Test deposit
			//System.out.println("DEPOSITING ADDITIONAL 200 INTO ACC1");
			r = app.deposit("acc1", 200.00);
			System.out.println( "DEPOSITING ADDITIONAL 200 INTO ACC1 - " + r );

			// Test top up on account 1
			r = app.topUp("pocket1", 500.00);
			System.out.println("Testing top up on acc1 -" + r );

			// Test top up on account 2 
			r = app.topUp("pocket2", 500.00);
			System.out.println("Testing top up on acc2 -" + r );
			
			// Test Show balance before deposit
			r = app.showBalance("acc1");
			System.out.println("Balance MAIN 1 - " + r );

			r = app.showBalance("pocket1");
			System.out.println("Balance POCKET 1 - " + r );


			// Test Show balance before deposit
			r = app.showBalance("acc2");
			System.out.println("Balance MAIN 2 - " + r );

			r = app.showBalance("pocket2");
			System.out.println("Balance POCKET 2 - " + r );


			//Should return null but who knows
			r = app.listClosedAccounts();
			System.out.println("LISTING CLOSED ACCOUNTS - " + r );

			r = app.dropTables();
			System.out.println("DROP TABLES " + r);

			//StartScreen s = new StartScreen(app);
			

		}
	}
	//!### FINALIZAMOS
}
