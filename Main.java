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
			//Create tables
			r = app.createTables();
			System.out.println("CREATE TABLES " + r);

			System.out.println("RIGHT BEFORE DUMMY VARS");
			app.createDummyValues();
			System.out.println("RIGHT AFTER DUMMY VARS");
		

			

			StartScreen s = new StartScreen(app);
			app.GenerateMonthlyStatement("212431965");
			
			r = app.dropTables();
			System.out.println("DROP TABLES " + r);

		}
	}
	//!### FINALIZAMOS
}
