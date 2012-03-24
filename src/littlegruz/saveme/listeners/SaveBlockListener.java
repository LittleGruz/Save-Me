package littlegruz.saveme.listeners;

import littlegruz.saveme.SaveMeMain;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class SaveBlockListener implements Listener {
   private SaveMeMain plugin;
   
   public SaveBlockListener(SaveMeMain instance){
      plugin = instance;
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
