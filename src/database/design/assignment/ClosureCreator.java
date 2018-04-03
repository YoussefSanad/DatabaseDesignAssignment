/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.design.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final MultiMap MAIN_RULES = new MultiMap();
    private static final String SEPERATOR = "-";
    private static final ArrayList<String> ATTRIBUTE_NAMES = new ArrayList<>(Arrays.asList("A", "B","C","D","E"));
    private String primayKey;
    private ArrayList<String> candidateKeys;

    public ClosureCreator() {
        this.primayKey = "";
        candidateKeys = new ArrayList<>();
    }

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
                MAIN_RULES.add(splitedData[0], splitedDependants[n]);
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
    
    private boolean existsIn(ArrayList<String> values, String targetDeterminant){
        if(targetDeterminant.length() == 1){    
            return values.indexOf(targetDeterminant) != -1;
        }
        else if(targetDeterminant.length() > 1){
            for(int i = 0; i < targetDeterminant.length(); i++){
                if(values.indexOf(targetDeterminant.charAt(i)) != -1){
                    return false;
                }
            }
        }
        return true;
        
    }
    
    private ArrayList<String> findDependants(String determinant, ArrayList<String> values){
        ArrayList<String> tempValues = new ArrayList<>();
        MAIN_RULES.map.forEach((currentDeterminant, currentValues) -> {
            if (determinant.equals(currentDeterminant))
            {
                
            }
            else if(existsIn(values, currentDeterminant))
                tempValues.addAll(currentValues);
        });
        return tempValues;
    }
    
    private void applyInferenceRules(){
        DEPENDANCY_MAP.map.forEach((determinant, values) -> {
            DEPENDANCY_MAP.add(determinant, determinant);
            DEPENDANCY_MAP.addValues(determinant, findDependants(determinant,values));

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
    
    public ArrayList<String> getDistinctVaribales()
    {
        ArrayList<String> attributesArray = new ArrayList<>();
        
        MAIN_RULES.map.forEach( (key,values) -> {
            String[] split = key.split("");
            for(String str : split)
            {
                if(!attributesArray.contains(str))
                {
                    attributesArray.add(str);
                }
            }
           
            for (String str : values) 
            {
                if(!attributesArray.contains(str))
                {
                    attributesArray.add(str);
                }
            }
        
        });
        
        return attributesArray;
        
    }  
    public void findCandidateKeys()
    {
        //ArrayList<String> attributesArray = ATTRIBUTE_NAMES;
        ArrayList<String> attributesArray = getDistinctVaribales();
      
        DEPENDANCY_MAP.map.forEach((determinant,dependantValues)-> {
               if(dependantValues.size() == attributesArray.size())
               {
                   candidateKeys.add(determinant);
               }
               if(primayKey.length() == 0)
               {
                   this.primayKey = determinant;
               }
               if(determinant.length() < primayKey.length())
               {
                   this.primayKey = determinant;
               }
        
        });
    }

    public String getPrimayKey() {
        return primayKey;
    }

    public ArrayList<String> getCandidateKeys() {
        return candidateKeys;
    }
    
    
}
