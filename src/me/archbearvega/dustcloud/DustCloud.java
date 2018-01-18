package me.archbearvega.dustcloud;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.ParticleEffect.BlockData;






public class DustCloud extends EarthAbility implements AddonAbility {
	
	public static final String NAME = "DustCloud";
	
	private int radius = 5;
	private long duration = 4500;
	private long cooldown = 7000;
	private int potDuration = 2000;
	private int minEarthBlocksRequired = 6;
	
	private Location playerLocation;

	
	
	public DustCloud(Player player) {
		super(player);
		if(!bPlayer.canBend(this)){
			remove();
		}
		cooldown = ConfigManager.defaultConfig.get().getLong("AddonAbilities.ArchBear_Vega."+NAME+".Cooldown");
		duration = ConfigManager.defaultConfig.get().getLong("AddonAbilities.ArchBear_Vega."+NAME+".Duration");
		radius = ConfigManager.defaultConfig.get().getInt("AddonAbilities.ArchBear_Vega."+NAME+".Radius");
		potDuration = ConfigManager.defaultConfig.get().getInt("AddonAbilities.ArchBear_Vega."+NAME+".potDuration");
		minEarthBlocksRequired = ConfigManager.defaultConfig.get().getInt("AddonAbilities.ArchBear_Vega."+NAME+".minSourceAmount");
		
		playerLocation = player.getLocation();
		start();
		
	}

	@Override
	public long getCooldown() {
		
		return cooldown;
	}

	@Override
	public Location getLocation() {
		
		return null;
	}

	@Override
	public String getName() {
		
		return NAME;
	}

	@Override
	public boolean isHarmlessAbility() {
		
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		
		return true;
	}

	@Override
	public void progress() {
		Block b = player.getLocation().subtract(0, 1, 0).getBlock();
		
		if(EarthAbility.isEarthbendable(player, b)){
			
			Location center = playerLocation;
			Material m;
			List<Block> validSources = getBlocksInRadius(center);
				
				if(validSources.size() >= minEarthBlocksRequired){
					if(center.subtract(0,1,0).getBlock().getType()== Material.SAND){
						m = Material.SAND;
					}
					else if (EarthAbility.isEarthbendable(player, center.subtract(0, 1, 0).getBlock())){
						m = center.subtract(0,1,0).getBlock().getType();
					}
					else{
						m = Material.DIRT;
					}
			
					doEffect(player, m, center);
					
					for (Entity e : GeneralMethods.getEntitiesAroundPoint(center, radius)) {
		    
						if (e instanceof LivingEntity && e.getEntityId() != player.getEntityId()) { 
							LivingEntity entity = (LivingEntity) e; 
							entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ((potDuration/(1000))*20), 2));
							entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, ((potDuration/(1000))*20), 2));
						}
				
						if(e instanceof LivingEntity && e.getEntityId() == player.getEntityId()){
							player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,((5000/(1000))*20), 4));
						}
			
					}
					
					if(this.getStartTime() + duration < System.currentTimeMillis()){
						bPlayer.addCooldown(this);
						remove();
					}
		
				}
		}
	}
	public List<Block> getBlocksInRadius(Location center){
		Block block = null;
		List<Block> blocks = new ArrayList<Block>();
		int radius = 10;
		for (int x = -radius; x < radius; x++) {
		    for (int y = -radius; y < radius; y++) {
		        for (int z = -radius; z < radius; z++) {
		            if (x * x + y * y + z * z <= radius * radius) { 
		            	if(EarthAbility.isEarthbendable(player, center.clone().add(x, y, z).getBlock())) {
		            	block = center.clone().add(x, y, z).getBlock();
		            	blocks.add(block);
		            	return blocks;
		            	}
		            }
		        }
		    }
		}
		return blocks;
	}
	public void doEffect(Player player, Material m, Location center) {
		if (player.isSneaking()) {
			ParticleEffect.BLOCK_DUST.display(new BlockData(m, (byte) 0), radius, 3, radius, 0.1F, 40, center, 50);
			if (m == Material.SAND || m == Material.GRAVEL){
				ParticleEffect.FALLING_DUST.display(new BlockData(m, (byte) 0), radius, 3, radius, 0.1F, 40, center, 50);
			}
		}
	}

	@Override
	public String getAuthor() {
		
		return "ArchBear_Vega";
	}

	@Override
	public String getVersion() {
		
		return "1.1.0";
	}
	
	@Override
	public String getDescription(){
		return "By shaking the ground back and forth, earthbenders can create dust clouds of various sizes to provide cover.";
		
	}
	@Override
	public String getInstructions(){
		return "Sneak while standing on a sufficient source of earth.";
		
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new DustCloudListener(), ProjectKorra.plugin);
		
		ConfigManager.defaultConfig.get().addDefault("AddonAbilities.ArchBear_Vega."+NAME+".Cooldown", cooldown);
		ConfigManager.defaultConfig.get().addDefault("AddonAbilities.ArchBear_Vega."+NAME+".Duration", duration);
		ConfigManager.defaultConfig.get().addDefault("AddonAbilities.ArchBear_Vega."+NAME+".potDuration", potDuration);
		ConfigManager.defaultConfig.get().addDefault("AddonAbilities.ArchBear_Vega."+NAME+".Radius", radius);
		ConfigManager.defaultConfig.get().addDefault("AddonAbilities.ArchBear_Vega."+NAME+".minSourceAmount", minEarthBlocksRequired);
		
		
		
		ConfigManager.defaultConfig.save();
	
	}

	@Override
	public void stop() {
		
		
	}

}
