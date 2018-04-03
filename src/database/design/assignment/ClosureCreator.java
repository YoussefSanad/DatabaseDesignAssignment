/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.design.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdc
 */
public class ClosureCreator {
    private static final MultiMap DEPENDANCY_MAP = new MultiMap();
    private static final String SEPERATOR = "-";
    
    public ArrayList<String> readDependanciesFile(){
        final String FILE_PATH = "dependancies.txt";
        ArrayList<String> dependancies = new ArrayList<>();
        File ddlScript = new File(FILE_PATH);
        Scanner in;
        try {
            in = new Scanner(ddlScript);
            while(in.hasNext())
            dependancies.add(in.nextLine());
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DependancyChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dependancies;
    }
    
    public void loadDependancies(){
        ArrayList<String> functionalDependancies = readDependanciesFile();
        for(int i = 0; i < functionalDependancies.size(); i++)
        {
            String [] splitedData = functionalDependancies.get(i).split(SEPERATOR);
            String [] splitedDependants = splitedData[1].split("");
            for(int n = 0;n < splitedDependants.length; n++){
                DEPENDANCY_MAP.add(splitedData[0], splitedDependants[n]);
            }
        }
    }
    private HashMap<String,Integer> getMappedValuesCount(){
        HashMap<String,Integer> valueCount = new HashMap<>();
        DEPENDANCY_MAP.map.forEach((determinant, values) -> {
            valueCount.put(determinant,values.size() );
        });
        return valueCount;
    }
    
    private void applyInferenceRules(){
        DEPENDANCY_MAP.map.forEach((determinant, values) -> {
            ArrayList<String> tempValues = new ArrayList<>();
            Iterator<String> iter = values.iterator();
            while(iter.hasNext())
            {
                String str = iter.next();
                System.out.println(str);
                if(DEPENDANCY_MAP.map.containsKey(str))
                {
                    tempValues.addAll(DEPENDANCY_MAP.map.get(str));
                }
            }
            DEPENDANCY_MAP.addValues(determinant, tempValues);
        });
    }
    
    private void removeDuplicates(){
        DEPENDANCY_MAP.map.forEach((determinant, values) -> {
           Set<String> removeDuplictes = new HashSet<>();
           removeDuplictes.addAll(values);
           values.clear();
           values.addAll(removeDuplictes);
        });
    }
    
    private void checkBaseCase(HashMap<String,Integer> dependanciesVCBefore){
        HashMap<String,Integer> dependanciesVCAfter = getMappedValuesCount();
        DEPENDANCY_MAP.map.forEach((determinant, values) -> {
            dependanciesVCAfter.put(determinant,values.size() );
            if(dependanciesVCBefore.get(determinant) != dependanciesVCAfter.get(determinant))
            {
                createClosure();
            }
        });
    }
    public void createClosure(){
        //value conuts could be done wihtout a map ... for later if there's time
        HashMap<String,Integer> dependantsValueCount = getMappedValuesCount();
        applyInferenceRules();
        removeDuplicates();
        checkBaseCase(dependantsValueCount);
    }
    
    public void printMap(){
        DEPENDANCY_MAP.printMap();
    }
}
