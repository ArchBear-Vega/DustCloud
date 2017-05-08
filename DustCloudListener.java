package me.archbearvega.dustcloud;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class DustCloudListener implements Listener {
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event){
		
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
			if(bPlayer != null && bPlayer.canBend(CoreAbility.getAbility(DustCloud.NAME))){
				new DustCloud(event.getPlayer());
			}
		
	}
}
