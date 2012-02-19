package littlegruz.saveme.listeners;

import littlegruz.saveme.SaveMeMain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SavePlayerListener implements Listener {
   private static SaveMeMain plugin;
   
   public SavePlayerListener(SaveMeMain instance){
      plugin = instance;
   }
   
   /* When a player dies, spawn them at their last checkpoint */
   @EventHandler
   public void onPlayerRespawn(PlayerRespawnEvent event){
      if(plugin.getWorldMap().containsKey(event.getPlayer().getWorld().getUID().toString())){
         event.setRespawnLocation(plugin.getPlayerMap().get(event.getPlayer().getName()));
         plugin.getServer().broadcastMessage("Respawned");
      }
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
         plugin.getServer().broadcastMessage("Join spawn");
      }
   }
}
