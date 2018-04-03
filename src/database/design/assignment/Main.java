/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.design.assignment;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdc
 */
public class Main {
    
    public static void main(String[] args) throws FileNotFoundException {
        ClosureCreator creator = new ClosureCreator();
        creator.loadDependancies();
        creator.printMap();
        creator.createClosure();
        creator.printMap();
        
        creator.findCandidateKeys();
        System.out.println("Canditate keys = " + creator.getCandidateKeys());
        System.out.println("Primary Key = " + creator.getPrimayKey());
        
    }
    
}
