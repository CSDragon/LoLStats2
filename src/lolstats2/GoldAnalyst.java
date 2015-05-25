/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;
import com.robrua.orianna.type.core.common.Lane;
import com.robrua.orianna.type.core.common.Role;
import com.robrua.orianna.type.core.common.GameMap;

/**
 * A class that takes a series of Matchdatas and turns it into graphable information.
 * 
 * @author Chris
 */
public class GoldAnalyst 
{
    private ArrayList<Matchdata> data;
    private int[] countPerMin;
    private int[] totalGoldPerMin;
    private int[] GPMPerMin;
    private double[] totalCreepsPerMin;
    private double[] CPMPerMin;
    private int gpm;
    private double cpm;
    private int totalGames;
    private int analyzedGames;
    private int maxTime;
    private double accuracyRating;
    
    /**
     * Creates a GoldAnalyst object, with basic restrictions (Avoid using whenever possible)
     * 
     * @param _data A list of Matchdatas the gold analyst will be looking through
     */
    public GoldAnalyst(ArrayList<Matchdata> _data)
    {
        data = _data;
        
        totalGames = data.size();
        
        for(int i = 0; i<totalGames; i++)
            accuracyRating += data.get(i).getRoleSanity();
        accuracyRating/=totalGames;
        
        DecimalFormat df = new DecimalFormat("#.00");

        maxTime=0;
        
        for(int i = 0; i<totalGames; i++)
        {
            if(maxTime < data.get(i).getNumMinutes())
                maxTime = data.get(i).getNumMinutes();
        }
        
        countPerMin = new int[maxTime];
        totalGoldPerMin = new int[maxTime];
        GPMPerMin = new int[maxTime];
        totalCreepsPerMin = new double[maxTime];
        CPMPerMin = new double[maxTime];
        gpm = 0;
        cpm = 0;
        analyzedGames=0;
        
        for(int i=0; i<totalGames; i++)
        {
            if(data.get(i).getRole() != Role.DUO_SUPPORT && data.get(i).getRole() != Role.DUO)
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

                analyzedGames++;
            }
        }
        
        for(int i = 1; i<maxTime; i++)
        {
            totalGoldPerMin[i]/=countPerMin[i];
            GPMPerMin[i]/=countPerMin[i];
            totalCreepsPerMin[i] = Double.valueOf(df.format(totalCreepsPerMin[i]/countPerMin[i]));
            CPMPerMin[i] = Double.valueOf(df.format(CPMPerMin[i]/countPerMin[i]));
        }
        
        
        if(analyzedGames !=0)
        {
            gpm/=analyzedGames;
            cpm/=analyzedGames;
        }
        
    }
    
   /**
     * Creates a GoldAnalyst object, with restrictions
     * 
     * @param _data A list of Matchdatas the gold analyst will be looking through
     * @param victory a flag to check if we want wins only, losses only or both
     */
    public GoldAnalyst(ArrayList<Matchdata> _data, int victory)
    {
        data = _data;
        
        boolean countWins = true;
        boolean countLosses = true;
        if(victory == 1)
            countLosses = false;
        if(victory == 2)
            countWins = false;
        
        totalGames = data.size();
        
        for(int i = 0; i<totalGames; i++)
            accuracyRating += data.get(i).getRoleSanity();
        accuracyRating/=totalGames;
        
        DecimalFormat df = new DecimalFormat("#.00");

        maxTime=0;
        
        for(int i = 0; i<totalGames; i++)
        {
            if(maxTime < data.get(i).getNumMinutes())
                maxTime = data.get(i).getNumMinutes();
        }
        

        countPerMin = new int[maxTime];
        totalGoldPerMin = new int[maxTime];
        GPMPerMin = new int[maxTime];
        totalCreepsPerMin = new double[maxTime];
        CPMPerMin = new double[maxTime];
        gpm = 0;
        cpm = 0;
        analyzedGames=0;
        
        for(int i=0; i<totalGames; i++)
        {
            boolean winLossCheck = (countWins && countLosses)||(countWins && data.get(i).getVictory())||(countLosses && !data.get(i).getVictory());
            
            if(winLossCheck && data.get(i).getRole() != Role.DUO_SUPPORT && data.get(i).getRole() != Role.DUO)
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
                analyzedGames++;
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
        
        
        if(analyzedGames !=0)
        {
            gpm/=analyzedGames;
            cpm/=analyzedGames;
        }
        
    }
    
    /**
     * Gives a GoldAnalysit new restrictions
     * @param victory a flag to check if we want wins only, losses only or both
     * @param minTimeFilter a flag to dictate the minimum time the game must have been played at
     * @param maxTimeFilter a flag to dictate the maximum time the game must have been played at
     * @param topCheck a flag to dictate if Top lane games are allowed
     * @param jungleCheck a flag to dictate if jungle games are allowed
     * @param midCheck a flag to dictate if Mid lane games are allowed
     * @param adcCheck a flag to dictate if ADC games are allowed
     * @param supportCheck a flag to dictate if Support games are allowed
     */
    public void recast(int victory, long minTimeFilter, long maxTimeFilter, boolean topCheck, boolean jungleCheck, boolean midCheck, boolean adcCheck, boolean supportCheck, int map)
    {

        boolean countWins = true;
        boolean countLosses = true;
        if(victory == 1)
            countLosses = false;
        if(victory == 2)
            countWins = false;
        
        boolean countDuo = (adcCheck&&supportCheck);
        boolean countAllRoles = (topCheck && jungleCheck && midCheck && adcCheck && supportCheck);
        
        
        DecimalFormat df = new DecimalFormat("#.00");

        
        countPerMin = new int[maxTime];
        totalGoldPerMin = new int[maxTime];
        GPMPerMin = new int[maxTime];
        totalCreepsPerMin = new double[maxTime];
        CPMPerMin = new double[maxTime];
        gpm = 0;
        cpm = 0;
        analyzedGames=0;
        
        for(int i=0; i<totalGames; i++)
        {
            boolean winLossCheck = (countWins && countLosses)||(countWins && data.get(i).getVictory())||(countLosses && !data.get(i).getVictory());
            boolean timeLow = minTimeFilter < data.get(i).getTime();
            boolean timeHigh = maxTimeFilter > data.get(i).getTime();
            
            //this is gonna be a lot of comparisons but it's easier to bring it out here.
            boolean topOK = ((data.get(i).getLane() == Lane.TOP) && topCheck);
            boolean jungleOK = ((data.get(i).getLane() == Lane.JUNGLE) && jungleCheck);
            boolean midOK = ((data.get(i).getLane() == Lane.MID) && midCheck);
            boolean adcOK = ((data.get(i).getRole() == Role.DUO_CARRY) && adcCheck);
            boolean supportOK = ((data.get(i).getRole() == Role.DUO_SUPPORT) && supportCheck);
            //If both ADC and Support are ok, we can allow the nebulous "Duo" role.
            boolean botOK = ((adcCheck && supportCheck) && (data.get(i).getRole() == Role.DUO));

            //if ANY ONE OF THESE was true, then that means we found the role we were looking for.
            boolean roleOK = (countAllRoles || topOK || jungleOK || midOK || adcOK || supportOK || botOK);
            
            
            
            
            boolean srOK = (map == 0 && data.get(i).getMap() == GameMap.SUMMONERS_RIFT);
            boolean aramOK = (map == 1 && data.get(i).getMap() == GameMap.HOWLING_ABYSS);
            boolean ttOK = (map == 2 && data.get(i).getMap() == GameMap.TWISTED_TREELINE);
            boolean domOK = (map == 3 && data.get(i).getMap() == GameMap.THE_CRYSTAL_SCAR);
            
            boolean mapOK = (map == 4 || srOK || aramOK || ttOK || domOK);
            
            
            if(winLossCheck && timeLow && timeHigh && roleOK &&mapOK)
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
                analyzedGames++;
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
        
        
        if(analyzedGames !=0)
        {
            gpm/=analyzedGames;
            cpm/=analyzedGames;
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
     * @return the analyzedGames
     */
    public int getNonSupGames() 
    {
        return analyzedGames;
    }
    
    public double getAccuracyRating()
    {
        return accuracyRating;
    }
    
}
