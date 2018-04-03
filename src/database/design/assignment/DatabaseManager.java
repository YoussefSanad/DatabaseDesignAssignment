
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.design.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdc
 */
public class DatabaseManager {
    
    private static final String url = "jdbc:mysql://localhost:3306/ass1";
    private static final String user = "root";
    private static final String password = "root";
    
    private static Connection con;
    
    public DatabaseManager() {
        connect();
    }
    
    private boolean connect(){
        try {
            //singleton pattern
            if(con == null)
            {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, user, password);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    //CRUD Methods
    
    public ResultSet select(String tableName)
    {
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM " + tableName;
        try {
            Statement statement = con.createStatement();
            rs = statement.executeQuery(selectQuery);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public ResultSet select(String tableName, HashMap<String, String> input){
        ResultSet rs = null;
        //getting the keys and the attributes in the crossponding Lists
         ArrayList<String> attributes = new ArrayList<>();
         attributes.addAll(input.keySet());
         ArrayList<String> values = new ArrayList<>();
         for(int i = 0; i < attributes.size(); i++){
             values.add(  input.get(attributes.get(i))  );
         }
         
        String selectQuery = "Select * From survey_db." + tableName + " where ";
        for(int i = 0; i < attributes.size(); i++){
            if(i != attributes.size() -1 )
                selectQuery += attributes.get(i) + " = '" + values.get(i) + "' and ";
            else
                selectQuery += attributes.get(i) + " = '" + values.get(i) + "';";
        }
        
        System.out.println(selectQuery);
        
        
        try {
            Statement Stmt;
            Stmt = con.createStatement();
            rs = Stmt.executeQuery(selectQuery);
        } catch (SQLException ex) {
            return rs;
        }
        return rs;
        
    }
    
    public int delete(String tableName, int id){
        
        String deleteQueryForSurvey = "DELETE FROM " + tableName + " Where id = " + id + ";";
        String deleteQueryForQuestion = "DELETE FROM question Where survey = " + id + ";";
        String deleteQueryForAnswer = "DELETE FROM answer Where survey_id = " + id + ";";
        String deleteQueryForReports = "DELETE FROM reports Where survey = " + id + ";";
        int rowsAffected = -1;
        try {
            Statement statement = con.createStatement();
            if(tableName.equals("surveys")){
                rowsAffected = statement.executeUpdate(deleteQueryForAnswer);
                rowsAffected = statement.executeUpdate(deleteQueryForQuestion);
                rowsAffected = statement.executeUpdate(deleteQueryForReports);
            }
            rowsAffected = statement.executeUpdate(deleteQueryForSurvey);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return rowsAffected;
        }
        return rowsAffected;
        
    }
    
    
    
}
