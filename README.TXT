How to use:

	Just double click LoL GPM Analyzer.jar. You have to have Java installed, get that at https://java.com/en/download/
	Once the program is running, just type in a summoner name and hit enter. 
	Click the tabs to see different stats. Click options to filter out games. 

	
	
What this App is:

	This app pulls your gold stats from your match history, saves it, and compiles it into graphs and charts for your GPM and CPM (Creeps Per Minute). 
	Over time, this will show the patterns in your gameplay, and how they change over the course of the game.
	This allows you to more easily identify the phases of the game that you need to most improve on. Meaning if you have low GPM or CPM spikes after recording many games, that means you consistantly underperform at that stage of the game and should focus your attention there.
	By default, support games are disabled (Min CPM 4, no max CPM), but this is configurable.



FAQ:

1) What's the purpose of this app?
	To give you a sense of where you need to improve yourself, in your average game. If you see a low spike in gold income at 21 minutes, take a look at the habits you've formed around that time, and improve in those areas.

2) Some matches take longer than others to download!
	This is the API sending "Key Overused" Responces, which means the program's gotta wait a few seconds before trying again. It just means too many people are using this service right now.

3) These numbers don't add up! It says for minute 32 I had a GPM of X, but my total gold went up only Y
	This is a discrpency bettween how I calculate GPM and how the game records your Gold. In the final minute of the game, you don't get a full minute, so you get less gold that minute. This means, each time a game ends, the minute it ends on will have an incongruent GPM and gold gained for that minute, which can make the stats look a few points off. Increasing your number of games recorded should fix this over time.
	
4) Match Details doesn't work?
	Match Details proved to be a bit of a hastle. It's something I'll do at a later date.
	
5) What else is coming later?
	Tons of stuff, including auto-refreshing stats, searching for specific champions, and only including matches within a specific date.

6) My summoner name has special characters will it work?
	I don't know yet. Please email me if you have special characters in your summoner name and are getting errors.

7) Why is the minimum CPM by default set to 4/My games are being recorded but not analyzed.
	In general, if you have less than 4 CPM, you're support or a low-farming jungler. If your CPM is consistantly below 4 on a laner or farm jungler, this app won't be able to help you much, but you can always lower the CPM requirement in Options.
	
8) Why only 35 minutes?
	Usually at that point, the game is pretty much over and waiting to end. GPM after that point is artificially inflated/deflated, and low sample size.
	
Got questions? Found bugs? Email me at CSDragon31@gmail.com

Thank you all very much!
