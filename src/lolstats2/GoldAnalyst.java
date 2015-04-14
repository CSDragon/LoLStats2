/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;

/**
 * A class that takes a series of Matchdatas and turns it into graphable information.
 * 
 * @author Chris
 */
public class GoldAnalyst 
{
    ArrayList<Matchdata> data;
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
    
    /**
     * Creates a GoldAnalyst object, with basic restrictions
     * 
     * @param _data A list of Matchdatas the gold analyst will be looking through
     */
    public GoldAnalyst(ArrayList<Matchdata> _data)
    {
        data = _data;
        
        totalGames = data.size();
        
        DecimalFormat df = new DecimalFormat("#.00");

        maxTime=0;
        
        for(int i = 0; i<totalGames; i++)
        {
            if(maxTime < data.get(i).getNumMinutes() && data.get(i).getCPM() >= 4)
                maxTime = data.get(i).getNumMinutes();
        }
        
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
            if(data.get(i).getCPM() >= 3)
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
    
   /**
     * Creates a GoldAnalyst object, with restrictions
     * 
     * @param _data A list of Matchdatas the gold analyst will be looking through
     * @param victory a flag to check if we want wins only, losses only or both
     * @param minCreeps a flag to dictate the minimum number of cpm for a game to count
     * @param maxCreeps a flag to dictate the maximum number of cpm for a game to count.
     */
    public GoldAnalyst(ArrayList<Matchdata> _data, int victory, int minCreeps, int maxCreeps)
    {
        data = _data;
        
        boolean countWins = true;
        boolean countLosses = true;
        if(victory == 1)
            countLosses = false;
        if(victory == 2)
            countWins = false;
        
        totalGames = data.size();
        
        DecimalFormat df = new DecimalFormat("#.00");

        maxTime=0;
        
        for(int i = 0; i<totalGames; i++)
        {
            if(maxTime < data.get(i).getNumMinutes() && data.get(i).getCPM() >= minCreeps)
                maxTime = data.get(i).getNumMinutes();
        }
        

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
            boolean winLossCheck = (countWins && countLosses)||(countWins && data.get(i).getVictory())||(countLosses && !data.get(i).getVictory());
            boolean creepLow = (minCreeps == -1 || data.get(i).getCPM() >= minCreeps);
            boolean creepHigh = (maxCreeps == -1 || data.get(i).getCPM() <= maxCreeps);
            
            if(creepLow && creepHigh && winLossCheck)
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
        
        //Just fixing a derp.
        totalGoldPerMin[0] = 475;
        
        
        if(nonSupGames !=0)
        {
            gpm/=nonSupGames;
            cpm/=nonSupGames;
        }
        
    }
    
    /**
     * Gives a GoldAnalysit new restrictions
     * @param victory a flag to check if we want wins only, losses only or both
     * @param minCreeps a flag to dictate the minimum number of cpm for a game to count
     * @param maxCreeps a flag to dictate the maximum number of cpm for a game to count.
     */
    public void recast(int victory, int minCreeps, int maxCreeps)
    {

        boolean countWins = true;
        boolean countLosses = true;
        if(victory == 1)
            countLosses = false;
        if(victory == 2)
            countWins = false;
        
        
        DecimalFormat df = new DecimalFormat("#.00");

        
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
            boolean winLossCheck = (countWins && countLosses)||(countWins && data.get(i).getVictory())||(countLosses && !data.get(i).getVictory());
            boolean creepLow = (minCreeps == -1 || data.get(i).getCPM() >= minCreeps);
            boolean creepHigh = (maxCreeps == -1 || data.get(i).getCPM() <= maxCreeps);
            
            if(creepLow && creepHigh && winLossCheck)
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
            if(countPerMin[i] != 0)
            {
                totalGoldPerMin[i]/=countPerMin[i];
                GPMPerMin[i]/=countPerMin[i];
                totalCreepsPerMin[i] = Double.valueOf(df.format(totalCreepsPerMin[i]/countPerMin[i]));
                CPMPerMin[i] = Double.valueOf(df.format(CPMPerMin[i]/countPerMin[i]));
            }
        }
        
        //Just fixing a derp.
        totalGoldPerMin[0] = 475;
        
        
        if(nonSupGames !=0)
        {
            gpm/=nonSupGames;
            cpm/=nonSupGames;
        }
        
    }
    
    /**
     * prints out the GoldAnalyist, for testing purposes
     */
    public void print()
    {
        System.out.println(Arrays.toString(getTotalGoldPerMin()));
        System.out.println(Arrays.toString(getGPMPerMin()));
        System.out.println(Arrays.toString(getTotalCreepsPerMin()));
        System.out.println(Arrays.toString(getCPMPerMin()));
        System.out.println(getGpm());
        System.out.println(String.format("%.2f", getCpm()));
    }
    
    /**
     * prints out CPMPerMin, for testing purposes
     */
    public void printCreepsByMin()
    {
        System.out.println("awk" + maxTime);
        for(int i = 0; i<maxTime; i++)
        {
            System.out.println(String.format("%2d",i) + ": " + String.format("%2.2f",getCPMPerMin()[i]) + " " + getTotalCreepsPerMin()[i]);
        }
    }
    
    
    
    //getters
    
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
