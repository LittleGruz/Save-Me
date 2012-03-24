package littlegruz.saveme.listeners;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.saveme.SaveMeMain;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SavePlayerListener implements Listener {
   private static SaveMeMain plugin;
   
   public SavePlayerListener(SaveMeMain instance){
      plugin = instance;
   }
   
   /* When a player dies, spawn them at their last checkpoint */
   @EventHandler
   public void onPlayerRespawn(PlayerRespawnEvent event){
      if(plugin.getWorldMap().containsKey(event.getPlayer().getWorld().getUID().toString()))
         event.setRespawnLocation(plugin.getPlayerMap().get(event.getPlayer().getName()));
   }
   
   /* When a player joins, spawn them at their last checkpoint if they exist
    * in the HashMap */
   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event){
      if(plugin.getWorldMap().containsKey(event.getPlayer().getWorld().getUID().toString())){
         if(plugin.getPlayerMap().get(event.getPlayer().getName()) == null){
            String name = event.getPlayer().getName();
            plugin.getPlayerMap().put(name, plugin.getServer().getWorld(plugin.getServer().getPlayer(name).getWorld().getUID()).getSpawnLocation());
         }
         else
            event.getPlayer().teleport(plugin.getPlayerMap().get(event.getPlayer().getName()));
      }
   }
   
   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event){
      if(plugin.getWorldMap().containsKey(event.getPlayer().getWorld().getUID().toString())){
         Block block = event.getPlayer().getLocation().getBlock();
            
         /* Check if the plate is set to change the spawn */
         if(plugin.getBlockMap().containsKey(block.getLocation())){

            /* Iterate through the player hashmap to find the player who stepped
               on the checkpoint plate*/
            Iterator<Map.Entry<String, Location>> it = plugin.getPlayerMap().entrySet().iterator();
            while(it.hasNext()){
               Entry<String, Location> player = it.next();
               
               /* Check that the player is still in the server */
               if(plugin.getServer().getPlayer(player.getKey()) != null
                     && !player.getValue().equals(block.getLocation())){
                  plugin.getServer().getPlayer(player.getKey()).sendMessage("Checkpoint set");
                  player.setValue(block.getLocation());
               }
            }
         }
      }
   }
}
