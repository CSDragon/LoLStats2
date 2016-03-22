
package lolstats2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.GameMode;
import com.robrua.orianna.type.core.common.GameType;
import com.robrua.orianna.type.core.common.QueueType;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import java.util.List;
import javax.swing.JOptionPane;


/**
 * A program to monitor GPM and CPM
 * 
 * @author Chris
 */
public class LolStats2
{
    private static SwingUI ui;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        //This is for me only.
        RiotAPI.setAPIKey(keyScrambler.decode("e:d<<6<<-5b66-0c27-df8;-5bc5=:;l=@;<"));
        
        
        //I can't publicly show my key, so you'll have to put your own key in key.txt
        /*try
        {
            BufferedReader br = new BufferedReader(new FileReader("key.txt"));
            String s = br.readLine();
            RiotAPI.setAPIKey(s);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Critical Error:\nYou need a Riot API Key in \"key.txt\"\nSee readme for details.");
            System.exit(-1);
        }*/
        
        
        RiotAPI.setRegion(Region.NA);
        RiotAPI.setMirror(Region.NA);

        ui = new SwingUI();
        
        
    }
    
    /**
     * Gets new matches, loads old ones, creates a GoldAnalyst and gives it to the GUI to display as stats.
     * 
     * @param summonerName The Summoner name we will be viewing
     * @param region The Region the summoner is from
     * @return The GoldAnalyst we created
     */
    public static GoldAnalyst run(String summonerName, String region)
    {
        long playerID = 0;
        
        RiotAPI.setRegion(Region.valueOf(region));

        //this has been commented out for offline testing
        playerID = RiotAPI.getSummonerByName(summonerName).getID();
        
        
        createSavesFolder();  
        createRegionFolder(region);
        createPersonalSaveFolder(summonerName, region);
        
        //this has been commented out for offline testing
        getNewMatches(summonerName, playerID, region);
        
        GoldAnalyst gpmem = new GoldAnalyst(loadRecordedMatches(summonerName, region),0);
        
        return gpmem;
        
    }
    
    /**
     * Gives you a list of the Match IDs of all locally stored matches for the summoner we want. (used for indexing)
     * 
     * @param summonerName The Summoner name we will be viewing
     * @param region The Region the summoner is from
     * @return A list of all matches stored locally for this summoner as Longs
     */
    public static ArrayList<Long> listRecordedMatches(String summonerName, String region)
    {
        
        ArrayList<Long> list = new ArrayList<Long>();
        
        File folder = new File("Saves/"+region+"/"+summonerName);
        File[] listOfFiles = folder.listFiles();

        
        
        for (File curFile : listOfFiles) {
            if (curFile.isFile()) {
                list.add(Long.decode(curFile.getName().substring(0, curFile.getName().length() - 4)));
            } 
        }
        
        return list;
    }
    
    /**
     * Gives you a list of all the matches, in Matchdata format, stored locally for the summoner we want.
     * 
     * @param summonerName The Summoner name we will be viewing
     * @param region The Region the summoner is from
     * @return A list of all matches stored locally for this summoner as Matchdatas.
     */
    public static ArrayList<Matchdata> loadRecordedMatches(String summonerName, String region)
    {
        ArrayList<Long> matchList = listRecordedMatches(summonerName,region);
        
        ArrayList<Matchdata> matches = new ArrayList<Matchdata>();
        
        for (Long curMatch : matchList) 
        {
            matches.add(Matchdata.loadMD(curMatch, summonerName, region));
        }
        
        
        return matches;
    }
    
    /**
     * Looks up new matches and stores them locally
     * 
     * @param summonerName The Summoner name we will be viewing
     * @param playerID The ID of the Summoner we will be viewing
     * @param region The Region the summoner is from
     */
    public static void getNewMatches(String summonerName, long playerID, String region)
    {
        
        List<MatchReference> mh = RiotAPI.getMatchList(playerID);
        
        //if player doesn't have a match history
        if(mh.isEmpty())
        {
            System.out.println("Riot API found an empty match history for this Summoner. It's probably been too long since they've played a game.");
            return;
        }
        
        
        ArrayList<Long> recordedMatches = listRecordedMatches(summonerName, region);
        
        ArrayList<Long> MHMatches = new ArrayList<Long>();
        
        for(int i = 0; i<mh.size(); i++)
        {
            MHMatches.add(mh.get(i).getID());
        }
        
        MHMatches.removeAll(recordedMatches);
        
        
        ui.getGui().changeStatus("Downloading Matches...");
        
        //Hard cap it at 60 games or it will try to download thousands of games.
        for (int i = 0; i< MHMatches.size() && i < 60; i++) 
        {
            Long curMatchID = MHMatches.get(i);
            ui.getGui().changeStatus("Downloading Match "+curMatchID+"... One moment please");
            Match match =  RiotAPI.getMatch(curMatchID);
            boolean queueTypeOk = (match.getQueueType() == QueueType.RANKED_SOLO_5x5 || match.getQueueType() == QueueType.RANKED_TEAM_5x5 || match.getQueueType() == QueueType.TEAM_BUILDER_DRAFT_RANKED_5x5 || match.getQueueType() == QueueType.TEAM_BUILDER_DRAFT_UNRANKED_5x5 || match.getQueueType() == QueueType.RANKED_TEAM_5x5 || match.getQueueType() == QueueType.NORMAL_5x5_BLIND || match.getQueueType() == QueueType.NORMAL_5x5_DRAFT);
            if(match.getType() == GameType.MATCHED_GAME && match.getMode() == GameMode.CLASSIC && queueTypeOk)
                Matchdata.saveMD(new Matchdata(match, curMatchID, summonerName, playerID), region);
            try
            {
                Thread.sleep(250);
            }
            catch(Exception e) {}
        }
        
        
        
        
    }
    
    /**
     * Creates the Saves folder if it doesn't already exist
     */
    public static void createSavesFolder()
    {
        File saveDirectory = new File("Saves");
        if(!saveDirectory.exists())
        {
            System.out.println("Creating the save folder");
            
            try
            {
                saveDirectory.mkdir();
            }
            catch(SecurityException se)
            {
                System.out.println("Folder creation failed. Check your permissions.");
                System.exit(-1);
            }
        }
    }
    
    /**
     * Creates a region folder if it doesn't already exist, in Saves
     * 
     * @param region The Region the summoner is from
     */
    public static void createRegionFolder(String region)
    {
        File saveDirectory = new File("Saves/"+region);
        if(!saveDirectory.exists())
        {
            System.out.println("Creating the region folder");
            
            try
            {
                saveDirectory.mkdir();
            }
            catch(SecurityException se)
            {
                System.out.println("Folder creation failed. Check your permissions.");
                System.exit(-1);
            }
        }
    }
    
    /**
     * Creates the individual Summoner's save folder, in the region folder, in Saves
     * 
     * @param summonerName The Summoner name we will be viewing
     * @param region The Region the summoner is from
     */
    public static void createPersonalSaveFolder(String summonerName, String region)
    {
        File saveDirectory = new File("Saves/"+region+"/"+summonerName);
        if(!saveDirectory.exists())
        {
            System.out.println("Creating the save folder for user "+summonerName);
            
            try
            {
                saveDirectory.mkdir();
            }
            catch(SecurityException se)
            {
                System.out.println("Folder creation failed. Check your permissions.");
                System.exit(-1);
            }
        }
    }
    
 
}
