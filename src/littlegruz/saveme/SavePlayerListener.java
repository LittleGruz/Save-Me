package littlegruz.saveme;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SavePlayerListener implements Listener {
   private static SaveMe plugin;
   
   public SavePlayerListener(SaveMe instance){
      plugin = instance;
   }
   
   /* When a player dies, spawn them at their last checkpoint */
   @EventHandler
   public void onPlayerRespawn(PlayerRespawnEvent event){
      plugin.getServer().broadcastMessage("Respawned");
   }
   
   /* When a player joins, spawn them at their last checkpoint */
   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event){
      plugin.getServer().broadcastMessage("Respawned");
   }
}
