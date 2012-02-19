package littlegruz.saveme.listeners;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.saveme.SaveMeMain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class SaveBlockListener implements Listener {
   private SaveMeMain plugin;
   
   public SaveBlockListener(SaveMeMain instance){
      plugin = instance;
   }
   
   @EventHandler
   public void onBlockPhysics(BlockPhysicsEvent event){
      if(plugin.getWorldMap().containsKey(event.getBlock().getWorld().getUID().toString())){
         if(event.getBlock().getType().compareTo(Material.STONE_PLATE) == 0
               || event.getBlock().getType().compareTo(Material.WOOD_PLATE) == 0){
            Location loc = event.getBlock().getLocation();

            /* Iterate through the player hashmap to find the player who stepped
               on the checkpoint plate*/
            Iterator<Map.Entry<String, Location>> it = plugin.getPlayerMap().entrySet().iterator();
            while(it.hasNext()){
               Entry<String, Location> player = it.next();
               if(plugin.getServer().getPlayer(player.getKey()) != null){
                  Location playerLoc = plugin.getServer().getPlayer(player.getKey()).getLocation();
                  if(loc.getBlockX() >= playerLoc.getBlockX() - 1 && loc.getBlockX() <= playerLoc.getBlockX() + 1
                        && loc.getBlockY() == playerLoc.getBlockY()
                        && loc.getBlockZ() >= playerLoc.getBlockZ() - 1 && loc.getBlockZ() <= playerLoc.getBlockZ() + 2
                        && !player.getValue().equals(loc)){
                     plugin.getServer().getPlayer(player.getKey()).sendMessage("Checkpoint set");
                     player.setValue(loc);
                  }
               }
            }
         }
      }
   }
   
   @EventHandler
   public void onBlockDamage(BlockDamageEvent event){
      if(plugin.getWorldMap().containsKey(event.getBlock().getWorld().getUID().toString())){
         if(event.getItemInHand().getType().compareTo(Material.CACTUS) == 0){
            if(plugin.getBlockMap().get(event.getBlock().getLocation()) == null){
               plugin.getBlockMap().put(event.getBlock().getLocation(), "Stringy");
               event.getPlayer().sendMessage("Checkpoint saved");
            }
            else{
               plugin.getBlockMap().remove(event.getBlock().getLocation());
               event.getPlayer().sendMessage("Checkpoint removed");
            }
         }
      }
   }
}
