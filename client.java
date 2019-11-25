import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

class Client{

    private int my_aid;
    //private DB dataBase;

    public Client(){
        this.my_aid = -1;
    }

    public Client(int aid){
        this.my_aid = aid;
        // try{
        //     this.dataBase = new DB();
        // }
        // catch (Exception e){
        //     System.out.println("Unable to initialize DB");
        // }
    }

    //COMMENTING OUT CAUSE NO ACCESS TO DB FOR ME
    public boolean VerifyPin(int taxId, int pin){
        // try(
        //     ResultSet res = DB.query('SELECT C.pin FROM Client WHERE C.taxId = ' + Integer.toString(taxId) + 'AND C.pin = ' + Integer.toString(pin) )
        // )
        // {
        //     //one or more rows but IC should ensure that there will only be one;
        //     if (res.next()){
        //         return true;
        //     }
        // }
        
        // return false;
        System.out.println("Entered Verify Pin");
        return true;
    }

    //COMMENTING OUT CAUSE NO ACCESS TO DB FOR ME
    public boolean SetPin(int taxId, int pin){
        // try(
        //     ResultSet res = DB.query('UPDATE Clients C SET C.pin = ' + Integer.toString(taxId) + ' WHERE C.taxId = ' + Integer.toString(taxId))
        // )
        // {
        //     //one or more rows but IC should ensure that there will only be one;
        //     if (res.next()){
        //         return true;
        //     }
        // }
        
        return true;
    }
    



    public void wire(){
        return;
    }
    
    public void purchase(int amt){
        return;
    }

    public void withdraw(int amt){
        return;
    }

    public void topUp(int amt){
        return;
    }

    public void deposit(int amt){
        return;
    }

    //TODO: Consider taking in our AID as parameter
    public void transfer(int src_aid, int dest_aid, double amt){
        return;
    }

    public void payFriend(){
        return;
    }

    public static void main(String[] args) {

    }
}