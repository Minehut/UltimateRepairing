package com.songoda.repairplus.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.repairplus.RepairPlus;
import com.songoda.repairplus.Utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

/**
 * Created by songoda on 2/24/2017.
 */
public class ParticleHandler implements Listener {

    RepairPlus plugin = RepairPlus.pl();
    public boolean v1_7 = plugin.getServer().getClass().getPackage().getName().contains("1_7");
    public boolean v1_8 = plugin.getServer().getClass().getPackage().getName().contains("1_8");


    public ParticleHandler() {
        checkDefaults();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> applyParticles(), 5L, 10L);
    }

    @SuppressWarnings("all")
    public void applyParticles() {
        try {
            if (plugin.getConfig().getString("data.anvil") != null) {
                int amt = plugin.getConfig().getInt("data.particlesettings.ammount");
                String type = plugin.getConfig().getString("data.particlesettings.type");
                if (type == null) {
                    System.out.println("Critical error in your RepairPlus config. Please add a correct particle type or regenerate the config.");
                } else {
                    ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.anvil");
                    for (String loc : section.getKeys(false)) {
                        String str[] = loc.split(":");
                        String worldName = str[1].substring(0, str[1].length() - 1);
                        if (Bukkit.getServer().getWorld(worldName) != null) {
                            if (plugin.getConfig().getString("data.anvil." + loc + ".particles") != null) {
                                World w = Bukkit.getServer().getWorld(str[1].substring(0, str[1].length() - 1));
                                Location location = Arconix.pl().serialize().unserializeLocation(loc);
                                location.add(.5,0,.5);
                                if (v1_8 || v1_7) {
                                    w.spigot().playEffect(location, org.bukkit.Effect.valueOf(type), 1, 0, (float) 0.25, (float) 0.25, (float) 0.25, 1, amt, 100);
                                } else {
                                    w.spawnParticle(org.bukkit.Particle.valueOf(type), location, amt, 0.25, 0.25, 0.25);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void checkDefaults() {
        try {
            if (plugin.getConfig().getInt("data.particlesettings.ammount") == 0) {
                plugin.getConfig().set("data.particlesettings.ammount", 25);
                plugin.saveConfig();
            }
            if (plugin.getConfig().getString("data.particlesettings.type") == null) {
                if (v1_7 || v1_8) {
                    plugin.getConfig().set("data.particlesettings.type", "WITCH_MAGIC");
                } else {
                    plugin.getConfig().set("data.particlesettings.type", "SPELL_WITCH");
                }
                plugin.saveConfig();
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
