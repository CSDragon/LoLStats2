package lolstats2;

import com.google.gson.*;
import java.io.*;
import com.robrua.orianna.type.core.match.Match;

/**
 * A class that takes a match, and combs it for relevant information
 * @author Chris
 */
public class Matchdata 
{
    private String summonerName;
    private long playerID;
    private int participantID;
    private long matchID;
    private int minutes;
    private int[] totalGoldEachMinute;
    private int[] individualGEM;
    private int[] totalCreepsEachMinute;
    private int[] individualCEM;
    private int gpm;
    private double cpm;
    private boolean victory;
    long champion;
    long time;

    
    /**
     * This method creates an empty matchdata.
     * It should only be called when an error causes a real match to not work.
     * Because it's empty, it will not affect the graph data.
     */
    public Matchdata()
    {
    }

    /**
     * Creates a matchdata out of a match object.
     * 
     * @param match The match that's being analyzed.
     * @param _matchID The ID of the match
     * @param _summonerName The name of the summoner
     * @param _playerID  The id of the summoner
     */
    public Matchdata(Match match, long _matchID, String _summonerName, long _playerID)
    {
        matchID = _matchID;
        summonerName = _summonerName;
        playerID = _playerID;

        
        participantID = getIdentityFromSummonerID(match);
        totalGoldEachMinute = goldEachMinute(match, participantID);
        minutes = totalGoldEachMinute.length;
        
        individualGEM = new int[totalGoldEachMinute.length];
        for(int i = totalGoldEachMinute.length-1; i>0; i--)
        {
            individualGEM[i] = totalGoldEachMinute[i]-totalGoldEachMinute[i-1];
        }
        individualGEM[0] = totalGoldEachMinute[0];
        
        
        totalCreepsEachMinute = creepsEachMinute(match, participantID);
        
        individualCEM = new int[totalCreepsEachMinute.length];
        for(int i = totalCreepsEachMinute.length-1; i>0; i--)
        {
            individualCEM[i] = totalCreepsEachMinute[i]-totalCreepsEachMinute[i-1];
        }
        individualCEM[0] = totalCreepsEachMinute[0];
        
        
        
        gpm = totalGoldEachMinute[totalGoldEachMinute.length-1]/totalGoldEachMinute.length;
        cpm = totalCreepsEachMinute[totalCreepsEachMinute.length-1]/totalCreepsEachMinute.length;
        
        //get(participantID-1) might seem weird, but it's how getParticipants is stored.
        victory = match.getParticipants().get(participantID-1).getStats().getWinner();
        champion = match.getParticipants().get(participantID-1).getChampionID();
        time = System.currentTimeMillis();
        
    }

    /**
     * Saves a Matchdata as a JSON object, as a .txt file.
     * 
     * @param out The Matchdata we're saving
     * @param region The region the summoner plays in
     */
    public static void saveMD(Matchdata out, String region)
    {
        Gson outGS = new Gson();
        String outStream = outGS.toJson(out);
        
        
        FileOutputStream outputStream;

        try 
        {
          outputStream = new FileOutputStream("Saves/"+region+"/"+out.getPlayerName()+"/"+Long.toString(out.getMatchID())+".txt");
          outputStream.write(outStream.getBytes());
          outputStream.close();
        } 
        
        catch (Exception e) 
        {
          e.printStackTrace();
        }
        
    }
    
    /**
     * Loads a .txt file into a JSON object, into a Matchdata
     * 
     * @param matchID the match to be loaded
     * @param UserName the name of the player
     * @param region The region the summoner plays in
     * @return the match
     */
    public static Matchdata loadMD(long matchID, String UserName, String region)
    {
        FileReader fis;
        StringBuilder sb = new StringBuilder();
        
        try
        {
            fis = new FileReader("Saves/"+region+"/"+UserName+"/"+Long.toString(matchID)+".txt");
            BufferedReader bufferedReader = new BufferedReader(fis);



            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
                sb.append(line);
            }
            
            fis.close();
        }
        
        
        catch(Exception e)
        {
            return new Matchdata();
        }


        String json = sb.toString();
        Gson gson = new Gson();
        Matchdata in = gson.fromJson(json, Matchdata.class);
        
        
        return in;
    }
    
    /**
     * In a Match, combs through it and finds what "Identity" value the Match is calling the player.
     * 
     * @param match The Match being analyzed
     * @return The Identity number
     */
    public int getIdentityFromSummonerID(Match match)
    {
        int playerNum=-1;

        for(int i = 0; i<10; i++)
        {
            if(match.getParticipants().get(i).getSummonerID() == playerID)
                playerNum = match.getParticipants().get(i).getParticipantID();

        }

        return playerNum;
    }
    
    /**
     * In a Match, combs through it and finds what "Identity" value the Match is calling the player.
     * 
     * @param match The Match being analyzed
     * @return The Identity number
     */
    public int getIdentityFromSummonerName(Match match)
    {
        int playerNum=-1;

        for(int i = 0; i<10; i++)
        {
            if(match.getParticipants().get(i).getSummonerName().equals(summonerName))
                playerNum=match.getParticipants().get(i).getParticipantID();

        }

        return playerNum;
    }
    
    /**
     * Calculates the amount of gold accrued each minute over the course of the match 
     * @param match The match being calculated on
     * @param participantId the Identity number
     * @see getIdentityFromSummonerID
     * @return an int array with each gold number
     */
    public static int[] goldEachMinute(Match match, int participantId)
    {
        int length = match.getTimeline().getFrames().size();
        
        int[] totalGoldAtMin = new int[length];
        
        for(int i = 0; i<length; i++)
        {
            totalGoldAtMin[i] = match.getTimeline().getFrames().get(i).getParticipantFrames().get(participantId).getTotalGold();
        }
        
        return totalGoldAtMin;
        
    }
    
        /**
     * Calculates the amount of creeps accrued each minute over the course of the match 
     * @param match The match being calculated on
     * @param participantId the Identity number
     * @see getIdentityFromSummonerID
     * @return an int array with each creep number
     */
    public static int[] creepsEachMinute(Match match, int participantId)
    {
        int length = match.getTimeline().getFrames().size();
        
        int[] totalCreepsAtMin = new int[length];
        
        for(int i = 0; i<length; i++)
        {
            totalCreepsAtMin[i] = match.getTimeline().getFrames().get(i).getParticipantFrames().get(participantId).getMinionsKilled();
        }
        
        return totalCreepsAtMin;
        
    }
    
    
    
    //getters
    public long getMatchID()
    {
        return matchID;
    }
    
    public String getPlayerName()
    {
        return summonerName;
    }

    public int[] getTotalGoldEachMinute()
    {
        return totalGoldEachMinute;
    }
    
    public int[] getIndividualGEM()
    {
        return individualGEM;
    }
    
    public int[] getTotalCreepsEachMinute()
    {
        return totalCreepsEachMinute;
    }
    
    public int[] getIndividualCEM()
    {
        return individualCEM;
    }
    
    public int getGPM()
    {
        return gpm;
    }
    
    public double getCPM()
    {
        return cpm;
    }
    
    public int getNumMinutes()
    {
        return minutes;
    }
    
    public boolean getVictory()
    {
        return victory;
    }
    
}


