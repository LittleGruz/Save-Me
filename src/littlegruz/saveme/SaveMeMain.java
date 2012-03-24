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

import littlegruz.saveme.listeners.SaveBlockListener;
import littlegruz.saveme.listeners.SavePlayerListener;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveMeMain extends JavaPlugin {
   Logger log = Logger.getLogger("This is MINECRAFT!");
   private File playerFile;
   private File blockFile;
   private File worldFile;
   private HashMap<String, Location> playerMap;
   private HashMap<Location, String> blockMap;
   private HashMap<String, String> worldMap;
   
   public void onEnable(){
      // Create the directory if needed
      new File(getDataFolder().toString()).mkdir();
      playerFile = new File(getDataFolder().toString() + "/savePlayers.txt");
      blockFile = new File(getDataFolder().toString() + "/saveBlocks.txt");
      worldFile = new File(getDataFolder().toString() + "/saveWorlds.txt");
      
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

      blockMap = new HashMap<Location, String>();
      // Load up the blocks from file
      try{
         br = new BufferedReader(new FileReader(blockFile));
         String input;
         StringTokenizer st;
         
         // Load block file data into the block HashMap
         while((input = br.readLine()) != null){
            st = new StringTokenizer(input, " ");
            blockMap.put(new Location(getServer().getWorld(UUID.fromString(st.nextToken())), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())), "Stringy");
         }
         br.close();
         
      }catch(FileNotFoundException e){
         log.info("No original Save Me! block file found. One will be created for you");
      }catch(IOException e){
         log.info("Error reading Save Me! block file");
      }catch(Exception e){
         log.info("Incorrectly formatted Save Me! block file");
      }
      
      worldMap = new HashMap<String, String>();
      // Load up the worlds from file
      try{
         br = new BufferedReader(new FileReader(worldFile));
         String input;
         
         // Load world file data into the world HashMap
         while((input = br.readLine()) != null){
            worldMap.put(input, input);
         }
         br.close();
         
      }catch(FileNotFoundException e){
         log.info("No original Save Me! world file found. One will be created for you");
      }catch(IOException e){
         log.info("Error reading Save Me! world file");
      }catch(Exception e){
         log.info("Incorrectly formatted Save Me! world file");
      }
      
      getServer().getPluginManager().registerEvents(new SavePlayerListener(this), this);
      getServer().getPluginManager().registerEvents(new SaveBlockListener(this), this);
      log.info("Save Me! v1.0 enabled");
   }
   
   public void onDisable(){
      
      // Save ALL the data!
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
      
      try{
         bw = new BufferedWriter(new FileWriter(blockFile));
         
         // Save all block locations to file
         Iterator<Map.Entry<Location, String>> it = blockMap.entrySet().iterator();
         while(it.hasNext()){
            Entry<Location, String> loc = it.next();
            bw.write(loc.getKey().getWorld().getUID().toString() + " "
                  + Double.toString(loc.getKey().getX()) + " "
                  + Double.toString(loc.getKey().getY()) + " "
                  + Double.toString(loc.getKey().getZ()) + "\n");
         }
         bw.close();
      }catch(IOException e){
         log.info("Error saving Save Me! blocks");
      }
      
      try{
         bw = new BufferedWriter(new FileWriter(worldFile));
         Iterator<Map.Entry<String, String>> it = worldMap.entrySet().iterator();
         
         // Save all world UUIDs to file
         while(it.hasNext()){
            Entry<String, String> world = it.next();
            bw.write(world.getValue() + "\n");
         }
         bw.close();
      }catch(IOException e){
         log.info("Error saving Save Me! worlds");
      }
      
      log.info("Save Me! v1.0 disabled");
   }
   
   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
      if(cmd.getName().compareToIgnoreCase("addcheckpointworld") == 0){
         Player player;
         if(sender instanceof Player){
            if(sender.hasPermission("saveme.savetheworld")){
               player = (Player) sender;
               if(worldMap.get(player.getWorld().getUID().toString()) != null)
                  player.sendMessage("This world is already added");
               else{
                  worldMap.put(player.getWorld().getUID().toString(), player.getWorld().getUID().toString());
                  player.sendMessage("World added");
               }
            }
            else
               sender.sendMessage("You do not have permissions for this command");
         }
         else
            sender.sendMessage("Please do not use this command from the console");
      }
      else if(cmd.getName().compareToIgnoreCase("removecheckpointworld") == 0){
         Player player;
         if(sender instanceof Player){
            if(sender.hasPermission("saveme.savetheworld")){
               player = (Player) sender;
               if(worldMap.get(player.getWorld().getUID().toString()) != null){
                  worldMap.remove(player.getWorld().getUID().toString());
                  player.sendMessage("World removed");
               }else
                  player.sendMessage("This world has not been added yet");
            }
            else
               sender.sendMessage("You do not have permissions for this command");
         }
         else
            sender.sendMessage("Please do not use this command from the console");
      }
      return true;
   }

   public HashMap<String, Location> getPlayerMap() {
      return playerMap;
   }

   public HashMap<Location, String> getBlockMap() {
      return blockMap;
   }

   public HashMap<String, String> getWorldMap() {
      return worldMap;
   }
}
