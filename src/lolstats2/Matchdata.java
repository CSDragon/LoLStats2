package lolstats2;

import constant.Region;
import main.java.riotapi.RiotApi;
import com.google.gson.*;
import java.io.*;





public class Matchdata 
{
    private String playerName;
    private long playerID;
    private int participantID;
    private long matchID;
    private int minutes;
    private int[] totalGoldEachMinute;
    private int[] individualGEM;
    private int[] totalCreepsEachMinute;
    private int[] individualCEM;
    private int gpm;
    private int cpm;
    private boolean victory;

    
    
    public Matchdata()
    {
    }
    
    
    public Matchdata(long _matchID, String _playerName, long _playerID)
    {
        this.matchID = _matchID;
        this.playerName = _playerName;
        this.playerID = _playerID;
        dto.Match.MatchDetail match = null;
        
        try
        {
            match = LolStats2.api.getMatch(Region.NA, _matchID ,true);
        }
        catch(Exception e)
        {
            //temp sollution
            System.out.println("Error: Could not accuire match");
            System.exit(-1);
        }
        
        
        participantID = getIdentityFromSummonerID(match, playerID);
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
        
        victory = match.getParticipants().get(participantID-1).getStats().isWinner();
    }

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
    
    
    public static int getIdentityFromSummonerID(dto.Match.MatchDetail match, long playerID)
    {
        int playerNum=-1;

        for(int i = 0; i<10; i++)
        {
            if(match.getParticipantIdentities().get(i).getPlayer().getSummonerId() == playerID)
                playerNum=match.getParticipantIdentities().get(i).getParticipantId();

        }

        return playerNum;
    }
    
    
    public static int getIdentityFromSummonerName(dto.Match.MatchDetail match, String playerName)
    {
        int playerNum=-1;

        for(int i = 0; i<10; i++)
        {
            if(match.getParticipantIdentities().get(i).getPlayer().getSummonerName().equals(playerName))
                playerNum=match.getParticipantIdentities().get(i).getParticipantId();

        }

        return playerNum;
    }
    
    
    public static int[] goldEachMinute(dto.Match.MatchDetail match, int participantId)
    {
        int length = match.getTimeline().getFrames().size();
        
        int[] totalGoldAtMin = new int[length];
        
        for(int i = 0; i<length; i++)
        {
            totalGoldAtMin[i] = match.getTimeline().getFrames().get(i).getParticipantFrames().get(Integer.toString(participantId)).getTotalGold();
        }
        
        return totalGoldAtMin;
        
    }
    
    
        
    public static int[] creepsEachMinute(dto.Match.MatchDetail match, int participantId)
    {
        int length = match.getTimeline().getFrames().size();
        
        int[] totalCreepsAtMin = new int[length];
        
        for(int i = 0; i<length; i++)
        {
            totalCreepsAtMin[i] = match.getTimeline().getFrames().get(i).getParticipantFrames().get(Integer.toString(participantId)).getMinionsKilled();
        }
        
        return totalCreepsAtMin;
        
    }
    
    
    
    public long getMatchID()
    {
        return matchID;
    }
    
    public String getPlayerName()
    {
        return playerName;
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
    
    public int getCPM()
    {
        return cpm;
    }
    
    
    public int getNumMinutes()
    {
        return minutes;
    }
    
}


