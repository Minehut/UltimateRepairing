package com.songoda.repairplus;

import java.io.IOException;

import com.songoda.arconix.Arconix;
import com.songoda.repairplus.Events.*;
import com.songoda.repairplus.Handlers.CommandHandler;
import com.songoda.repairplus.Handlers.HologramHandler;
import com.songoda.repairplus.Handlers.ParticleHandler;
import com.songoda.repairplus.Handlers.RepairHandler;
import com.songoda.repairplus.Utils.ConfigWrapper;

import com.songoda.repairplus.Utils.Debugger;
import com.songoda.repairplus.Utils.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import com.songoda.repairplus.API.MCUpdate;

public final class RepairPlus extends JavaPlugin implements Listener {
    public static CommandSender console = Bukkit.getConsoleSender();

    public boolean v1_7 = Bukkit.getServer().getClass().getPackage().getName().contains("1_7");
    public boolean v1_8 = Bukkit.getServer().getClass().getPackage().getName().contains("1_8");

    public References references = null;
    private ConfigWrapper langFile = new ConfigWrapper(this, "", "lang.yml");

    public RepairHandler repair;
    public HologramHandler holo = null;
    public SettingsManager sm;

    public void onEnable() {
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
        console.sendMessage(Arconix.pl().format().formatText("&7RepairPlus " + this.getDescription().getVersion()  + " by &5Brianna <3!"));
        console.sendMessage(Arconix.pl().format().formatText("&7Action: &aEnabling&7..."));
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
        Bukkit.getPluginManager().registerEvents(this, this);

        setupConfig();

        langFile.createNewFile("Loading language file", "RepairPlus language file");
        loadLanguageFile();
        references = new References();


        repair = new RepairHandler();
        holo = new HologramHandler();
        sm = new SettingsManager();
        new ParticleHandler();

        try {
            MCUpdate update = new MCUpdate(this, true);
            Bukkit.getLogger().info(references.getPrefix() + "MCUpdate enabled and loaded");
        } catch (IOException e) {
            Bukkit.getLogger().info(references.getPrefix() + "Failed initialize MCUpdate");
        }

        this.getCommand("RPAnvil").setExecutor(new CommandHandler(this));
        this.getCommand("RepairPlus").setExecutor(new CommandHandler(this));

        getServer().getPluginManager().registerEvents(new BlockListeners(), this);
        getServer().getPluginManager().registerEvents(new InteractListeners(), this);
        getServer().getPluginManager().registerEvents(new InventoryListeners(), this);
    }

    public void onDisable() {
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
        console.sendMessage(Arconix.pl().format().formatText("&7RepairPlus " + this.getDescription().getVersion()  + " by &5Brianna <3!"));
        console.sendMessage(Arconix.pl().format().formatText("&7Action: &cDisabling&7..."));
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
        saveConfig();
    }

    private void setupConfig() {
        try {

            getConfig().addDefault("settings.Timeout", 200L);


            getConfig().addDefault("settings.XP-Cost-Equation", "{MaxDurability} - ({MaxDurability} - {Durability} / 40) + 1");
            getConfig().addDefault("settings.ECO-Cost-Equation", "{XPCost} * 5");
            getConfig().addDefault("settings.ITEM-Cost-Equation", "{XPCost} * 3");
            getConfig().addDefault("settings.Enchanted-Item-Multiplier", 2);

            getConfig().addDefault("settings.ECO-Icon", "DOUBLE_PLANT");
            getConfig().addDefault("settings.XP-Icon", "EXP_BOTTLE");
            getConfig().addDefault("settings.ITEM", "DIAMOND");

            getConfig().addDefault("Exit-Icon", "WOOD_DOOR");
            getConfig().addDefault("Buy-Icon", "EMERALD");
            getConfig().addDefault("Buy-Icon", "EMERALD");

            getConfig().addDefault("settings.Glass-Type", 7);
            getConfig().addDefault("settings.Rainbow-Glass", false);

            getConfig().addDefault("settings.Item-Match-Type", true);

            getConfig().addDefault("settings.Enable-Default-Anvil-Function", true);

            getConfig().addDefault("settings.Swap-Functions", false);

            getConfig().addDefault("settings.Perms-Only", false);
            getConfig().addDefault("settings.Debug-Mode", false);

            getConfig().options().copyDefaults(true);
            saveConfig();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void reload() {
        try {
            langFile.createNewFile("Loading language file", "RepairPlus language file");
            loadLanguageFile();
            references = new References();
            reloadConfig();
            saveConfig();
            holo.updateHolograms(true);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void loadLanguageFile() {
        try {
            Lang.setFile(langFile.getConfig());

            for (final Lang value : Lang.values()) {
                langFile.getConfig().addDefault(value.getPath(), value.getDefault());
            }

            langFile.getConfig().options().copyDefaults(true);
            langFile.saveConfig();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public static RepairPlus pl() {
        return (RepairPlus) Bukkit.getServer().getPluginManager().getPlugin("RepairPlus");
    }
}
