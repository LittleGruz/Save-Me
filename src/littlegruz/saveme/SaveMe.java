package littlegruz.saveme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveMe extends JavaPlugin {
   Logger log = Logger.getLogger("This is MINECRAFT!");
   private File playerFile;
   private HashMap<String, Location> playerMap;
   
   public void onEnable(){
      // Create the directory if needed
      new File(getDataFolder().toString()).mkdir();
      playerFile = new File(getDataFolder().toString() + "/savePlayers.txt");
      
      BufferedReader br;
      playerMap = new HashMap<String, Location>();
      // Load up the players from file
      try{
         br = new BufferedReader(new FileReader(playerFile));
         String input;
         StringTokenizer st;
         Location loc;
         
         // Load player file data into the player HashMap
         while((input = br.readLine()) != null){
            String name;
            st = new StringTokenizer(input, " ");
            name = st.nextToken();
            loc = new Location(getServer().getWorld(UUID.fromString(st.nextToken())), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
            playerMap.put(name, loc);
         }
         br.close();
         
      }catch(FileNotFoundException e){
         log.info("No original Save Me! player file found. One will be created for you");
      }catch(IOException e){
         log.info("Error reading Save Me! player file");
      }catch(Exception e){
         log.info("Incorrectly formatted Save Me! player file");
      }
      
      getServer().getPluginManager().registerEvents(new SavePlayerListener(this), this);
      log.info("Save Me! v0.1 enabled");
   }
   
   public void onDisable(){
      BufferedWriter bw;
      try{
         bw = new BufferedWriter(new FileWriter(playerFile));
         
         // Save all players to file
         Iterator<Map.Entry<String, Location>> it = playerMap.entrySet().iterator();
         while(it.hasNext()){
            Entry<String, Location> player = it.next();
            bw.write(player.getKey() + " "
                  + player.getValue().getWorld().getUID().toString() + " "
                  + Double.toString(player.getValue().getX()) + " "
                  + Double.toString(player.getValue().getY()) + " "
                  + Double.toString(player.getValue().getZ()) + "\n");
         }
         bw.close();
      }catch(IOException e){
         log.info("Error saving Save Me! players");
      }
      
      log.info("Save Me! v0.1 disabled");
   }

   public HashMap<String, Location> getPlayerMap() {
      return playerMap;
   }
}
