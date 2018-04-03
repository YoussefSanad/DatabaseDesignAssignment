/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.design.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author cdc
 */
public class DependancyChecker {
    String TABLE_NAME = "R";
    String determenant;
    String dependant;
    
    HashMap<String, String> attributeMap;
    DatabaseManager manager;
    
    public DependancyChecker(String attributeOne, String attributeTwo){
        attributeMap = new HashMap<>();
        determenant = attributeOne;
        dependant = attributeTwo;
        manager = new DatabaseManager();
    }
    
    public String constructQuery(){
        return String.format("select %s, %s from table %s", determenant , dependant, TABLE_NAME);
    }
    
    public boolean handelInstance(String determenant, String dependant){
        String value = attributeMap.get(determenant);
        if (value == null)
        {
            attributeMap.put(determenant, dependant);
            return true;
        }
        else return value.equals(dependant);
            
    }
    
    public boolean checkDependancy() throws SQLException{
        ResultSet rs = manager.select(TABLE_NAME);
        String currentDetermenant, currentDependant;
        while(rs.next())
        {
            currentDetermenant = rs.getString(this.determenant);
            currentDependant = rs.getString(this.dependant);
            if(!handelInstance(currentDetermenant, currentDependant))
                return false;
        }
        return true;
    }
}
