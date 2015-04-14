
package lolstats2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import com.robrua.orianna.api.core.RiotAPI;
import java.util.List;


/**
 *
 * @author Chris
 */
public class LolStats2 
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        //I can't publicly show my key, so you'll have to put your own key in key.txt
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("key.txt"));
            String s = br.readLine();
            RiotAPI.setAPIKey(s);
            RiotAPI.setRegion(com.robrua.orianna.type.core.common.Region.NA);
            RiotAPI.setMirror(com.robrua.orianna.type.core.common.Region.NA);
        }
        catch(Exception e)
        {
            RiotAPI.setAPIKey("YOUR_API_KEY_HERE");
        }
        

        SwingUI ui = new SwingUI();
        
    }
    
    
    public static void run(String playerName, String region)
    {
        long playerID = 0;
        //this has been commented out for offline testing
        playerID = RiotAPI.getSummonerByName(playerName).getID();

   
        
        
        
        createSaveFolder();  
        createRegionFolder(region);
        createPersonalSaveFolder(playerName, region);
        
        //this has been commented out for offline testing
        getNewMatches(playerName, playerID, region);
        
        GoldAnalyst gpmem = new GoldAnalyst(loadRecordedMatches(playerName, region),0,3,-1);
        gpmem.print();
        gpmem.printCreepsByMin();
        
        
        
        SwingUI.getGui().createGraph(gpmem);
        SwingUI.getGui().showInner();
        SwingUI.getGui().repaint();
  
    }
    
    
    
    


 

    
    
    
    public static ArrayList<Long> listRecordedMatches(String username, String region)
    {
        
        ArrayList<Long> list = new ArrayList<Long>();
        
        File folder = new File("Saves/"+region+"/"+username);
        File[] listOfFiles = folder.listFiles();

        
        
        for (File curFile : listOfFiles) {
            if (curFile.isFile()) {
                list.add(Long.decode(curFile.getName().substring(0, curFile.getName().length() - 4)));
            } 
        }
        
        return list;
    }
    
    public static ArrayList<Matchdata> loadRecordedMatches(String username, String region)
    {
        ArrayList<Long> matchList = listRecordedMatches(username,region);
        
        ArrayList<Matchdata> matches = new ArrayList<Matchdata>();
        
        for (Long curMatch : matchList) 
        {
            matches.add(Matchdata.loadMD(curMatch, username, region));
        }
        
        
        return matches;
    }
    
    
    public static void getNewMatches(String username, long playerID, String region)
    {
        
        List<com.robrua.orianna.type.core.matchhistory.MatchSummary> mh = RiotAPI.getMatchHistory(playerID);
        
        
        //if player doesn't have a match history
        if(mh.isEmpty())
        {
            System.out.println("Riot API found an empty match history for this Summoner. It's probably been too long since they've played a game.");
            return;
        }
        
        
        ArrayList<Long> recordedMatches = listRecordedMatches(username, region);
        
        ArrayList<Long> MHMatches = new ArrayList<Long>();
        
        for(int i = 0; i<mh.size(); i++)
        {
            MHMatches.add(mh.get(i).getID());
        }
        
        MHMatches.removeAll(recordedMatches);
        
        
        SwingUI.getGui().changeStatus("Downloading Matches...");
        
        for (Long curMatchID : MHMatches) 
            Matchdata.saveMD(new Matchdata(curMatchID, username, playerID), region);

        
        SwingUI.getGui().resetStatus();
        
        
        
    }
    
    
    public static void createSaveFolder()
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
    
    public static void createPersonalSaveFolder(String name, String region)
    {
        File saveDirectory = new File("Saves/"+region+"/"+name);
        if(!saveDirectory.exists())
        {
            System.out.println("Creating the save folder for user "+name);
            
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
    
    
    
    
 
    
    
    
    
    
    //TODO
    private static String[] roles = {"SOLOTOP", "NONEJUNGLE", "SOLOMID", "DUO_CARRYBOT","DUO_SUPPORTBOT"};
    public static boolean roleSanityCheck(dto.Match.MatchDetail match, int participantId)
    {
        String role = match.getParticipants().get(participantId).getTimeline().getRole() + match.getParticipants().get(participantId).getTimeline().getLane();
       
       
       
       
        return false;
    }
    
    
    
    
    
    
}
