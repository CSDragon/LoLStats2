/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;

public class GoldAnalyst 
{
    private int[] countPerMin;
    private int[] totalGoldPerMin;
    private int[] GPMPerMin;
    private double[] totalCreepsPerMin;
    private double[] CPMPerMin;
    private int gpm;
    private double cpm;
    private int totalGames;
    private int nonSupGames;
    private int maxTime;
    
    public GoldAnalyst(ArrayList<Matchdata> data)
    {
        
        totalGames = data.size();
        
        DecimalFormat df = new DecimalFormat("#.00");

        maxTime=0;
        
        for(int i = 0; i<totalGames; i++)
        {
            if(maxTime < data.get(i).getNumMinutes() && data.get(i).getCPM() > 3)
                maxTime = data.get(i).getNumMinutes();
        }
        
        System.out.println("MT:"+maxTime);
        
        countPerMin = new int[maxTime];
        totalGoldPerMin = new int[maxTime];
        GPMPerMin = new int[maxTime];
        totalCreepsPerMin = new double[maxTime];
        CPMPerMin = new double[maxTime];
        gpm = 0;
        cpm = 0;
        nonSupGames=0;
        
        for(int i=0; i<totalGames; i++)
        {
            if(data.get(i).getCPM() > 3)
            {
                for(int j=1; j<data.get(i).getNumMinutes(); j++)
                {
                    countPerMin[j]++;
                    totalGoldPerMin[j] += data.get(i).getTotalGoldEachMinute()[j];
                    GPMPerMin[j] += data.get(i).getIndividualGEM()[j];
                    totalCreepsPerMin[j] += data.get(i).getTotalCreepsEachMinute()[j];
                    CPMPerMin[j] += data.get(i).getIndividualCEM()[j];
                }

                gpm += data.get(i).getGPM();
                cpm += data.get(i).getCPM();
                nonSupGames++;
            }
        }
        
        for(int i = 1; i<maxTime; i++)
        {
            totalGoldPerMin[i]/=countPerMin[i];
            GPMPerMin[i]/=countPerMin[i];
            totalCreepsPerMin[i] = Double.valueOf(df.format(totalCreepsPerMin[i]/countPerMin[i]));
            CPMPerMin[i] = Double.valueOf(df.format(CPMPerMin[i]/countPerMin[i]));
        }
        
        
        if(nonSupGames !=0)
        {
            gpm/=nonSupGames;
            cpm/=nonSupGames;
        }
        
    }
    
    public void print()
    {
        System.out.println(Arrays.toString(getTotalGoldPerMin()));
        System.out.println(Arrays.toString(getGPMPerMin()));
        System.out.println(Arrays.toString(getTotalCreepsPerMin()));
        System.out.println(Arrays.toString(getCPMPerMin()));
        System.out.println(getGpm());
        System.out.println(String.format("%.2f", getCpm()));
    }
    
    public void printCreepsByMin()
    {
        System.out.println("awk" + maxTime);
        for(int i = 0; i<maxTime; i++)
        {
            System.out.println(String.format("%2d",i) + ": " + String.format("%2.2f",getCPMPerMin()[i]) + " " + getTotalCreepsPerMin()[i]);
        }
    }
    
    
    /**
     * @return the countPerMin
     */
    public int[] getCountPerMin()
    {
        return countPerMin;
    }

    /**
     * @return the totalGoldPerMin
     */
    public int[] getTotalGoldPerMin() 
    {
        return totalGoldPerMin;
    }

    /**
     * @return the GPMPerMin
     */
    public int[] getGPMPerMin() 
    {
        return GPMPerMin;
    }

    /**
     * @return the totalCreepsPerMin
     */
    public double[] getTotalCreepsPerMin() 
    {
        return totalCreepsPerMin;
    }

    /**
     * @return the CPMPerMin
     */
    public double[] getCPMPerMin() 
    {
        return CPMPerMin;
    }

    /**
     * @return the gpm
     */
    public int getGpm() 
    {
        return gpm;
    }

    /**
     * @return the cpm
     */
    public double getCpm() 
    {
        return cpm;
    }

    /**
     * @return the totalGames
     */
    public int getTotalGames() 
    {
        return totalGames;
    }

    /**
     * @return the nonSupGames
     */
    public int getNonSupGames() 
    {
        return nonSupGames;
    }
    
    
}
