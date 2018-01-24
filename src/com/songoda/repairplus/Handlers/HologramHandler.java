package com.songoda.repairplus.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.repairplus.Lang;
import com.songoda.repairplus.RepairPlus;
import com.songoda.repairplus.Utils.Debugger;
import com.songoda.repairplus.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
public class HologramHandler {

    RepairPlus plugin = RepairPlus.pl();
    public boolean v1_7 = plugin.getServer().getClass().getPackage().getName().contains("1_7");


    public HologramHandler() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> updateHolograms(false), 5000L, 5000L);
        updateHolograms(true);
    }

    @SuppressWarnings("all")
    public void updateHolograms(boolean full) {
        try {
            if (plugin.getConfig().getString("data.anvil") != null && v1_7 == false) {
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.anvil");
                for (String loc : section.getKeys(false)) {
                    String str[] = loc.split(":");
                    String worldName = str[1].substring(0, str[1].length() - 1);
                    if (Bukkit.getServer().getWorld(worldName) != null) {

                        double multi = .15;
                        Location location = Arconix.pl().serialize().unserializeLocation(loc);
                        location.add(.5,1.10,.5);
                        Block b = location.getBlock();

                        //Compatibility
                        boolean yes = false;
                        double radius = 1D;
                        List<Entity> near = location.getWorld().getEntities();
                        for (Entity e : near) {
                            if (e.getLocation().distance(location) <= radius) {
                                if (EntityType.ARMOR_STAND == e.getType()) {
                                    if (full == false) {
                                        yes = true;
                                    }
                                    if (plugin.getConfig().getBoolean("data.anvil." + loc + ".holo") != true || full) {
                                        e.remove();
                                    }
                                }
                            }
                        }
                        //End Compatibility

                        remove(location);
                        if (plugin.getConfig().getBoolean("data.anvil." + loc + ".holo") != false) {
                            if (yes == false) {
                                List<String> lines = new ArrayList<String>();

                                if (!plugin.getConfig().getBoolean("settings.Enable-Default-Anvil-Function")) {
                                    lines.add(Arconix.pl().format().formatText(Lang.ONECLICK.getConfigValue()));
                                } else if (plugin.getConfig().getBoolean("settings.Swap-Functions")) {
                                    lines.add(Arconix.pl().format().formatText(Lang.SWAPCLICK.getConfigValue()));
                                } else {
                                    lines.add(Arconix.pl().format().formatText(Lang.CLICK.getConfigValue()));
                                }

                                lines.add(Arconix.pl().format().formatText(Lang.TOREPAIR.getConfigValue()));
                                Arconix.pl().packetLibrary.getHologramManager().spawnHolograms(location, lines);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void remove(Location location) {
        Location loco = location.clone();
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco);
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
    }
}
