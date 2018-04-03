/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.design.assignment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author cdc
 */
public class MultiMap {
    public final HashMap<String , ArrayList<String> >  map;

    public MultiMap() {
        this.map = new HashMap<>();
    }
    
    
    
    public void add(String key, String value)
    {
        ArrayList<String> dependants = map.get(key);
        if (dependants == null)
        {
            dependants = new ArrayList<>();
            dependants.add(value);
            this.map.put(key, dependants); 
        }
        else
        {
            dependants.add(value);
        }      
    }
    
    public void addValues(String key, ArrayList<String> values)
    {
        ArrayList<String> dependants = map.get(key);
        if (dependants == null)
        {
            this.map.put(key, values); 
        }
        else
        {
            dependants.addAll(values);
        }      
    }
    
    public ArrayList<String> getValuesForKey(String key)
    {
        return map.get(key);
    }
    
    public Integer count()
    {
        return map.size();
    }
    
    public void printMap(){
        System.out.println(map);
    }
}
