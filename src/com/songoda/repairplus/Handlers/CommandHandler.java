package com.songoda.repairplus.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.repairplus.References;
import com.songoda.repairplus.RepairPlus;
import com.songoda.repairplus.Utils.Debugger;
import com.songoda.repairplus.Utils.Methods;
import com.songoda.repairplus.Utils.SettingsManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by songoda on 2/25/2017.
 */
public class CommandHandler implements CommandExecutor {

    private final RepairPlus plugin;

    public CommandHandler(final RepairPlus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (cmd.getName().equalsIgnoreCase("RPAnvil")) {
                Player player = (Player) sender;
                if (player.hasPermission("repairplus.rpanvil")) {
                    plugin.repair.initRepair(player, player.getLocation());
                }
            } else if (cmd.getName().equalsIgnoreCase("repairplus")) {
                if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                    sender.sendMessage("");
                    sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&7" + plugin.getDescription().getVersion() + " Created by &5&l&oBrianna"));
                    sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aRP help &7Displays this page."));
                    sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aRP reload &7Reload the Configuration and Language files."));
                    sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aRP holo &7Toggle a hologram for the anvil you are looking at."));
                    sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aRP particles &7Toggle particles for the anvil you are looking at."));
                    sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aRP inf &7Toggle unbreaking for the anvil you are looking at."));
                    sender.sendMessage("");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("repairplus.admin")) {
                        plugin.reload();
                        sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Configuration and Language files reloaded."));
                    }
                } else if (args[0].equalsIgnoreCase("holo")) {
                    if (plugin.v1_7) {
                        sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&7Holograms are not currently supported on 1.7... SORRY!"));
                    } else {
                        Player player = (Player) sender;
                        if (player.hasPermission("repairplus.admin")) {
                            Block b = player.getTargetBlock((Set<Material>) null, 200);
                            if (b.getType() == Material.ANVIL) {
                                String loc = Arconix.pl().serialize().serializeLocation(b);
                                if (plugin.getConfig().getString("data.anvil." + loc + ".active") == null) {
                                    plugin.getConfig().set("data.anvil." + loc + ".active", true);
                                }
                                if (plugin.getConfig().getString("data.anvil." + loc + ".holo") == null) {
                                    plugin.getConfig().set("data.anvil." + loc + ".holo", true);
                                    player.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&aHolograms &9enabled &afor this anvil."));
                                } else {
                                    plugin.getConfig().set("data.anvil." + loc + ".holo", null);
                                    player.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&aHolograms &9disabled &afor this anvil."));
                                }
                                plugin.holo.updateHolograms(true);
                                plugin.saveConfig();
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("particles")) {
                    Player player = (Player) sender;
                    if (player.hasPermission("repairplus.admin")) {
                        Block b = player.getTargetBlock((Set<Material>) null, 200);
                        if (b.getType() == Material.ANVIL) {
                            String loc = Arconix.pl().serialize().serializeLocation(b);
                            if (plugin.getConfig().getString("data.anvil." + loc + ".active") == null) {
                                plugin.getConfig().set("data.anvil." + loc + ".active", true);
                            }
                            if (plugin.getConfig().getString("data.anvil." + loc + ".particles") == null) {
                                plugin.getConfig().set("data.anvil." + loc + ".particles", true);
                                player.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&aParticles &9enabled &afor this anvil."));
                            } else {
                                plugin.getConfig().set("data.anvil." + loc + ".particles", null);
                                player.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&aParticles &9disabled &afor this anvil."));
                            }
                            plugin.saveConfig();
                        }
                    }
                } else if (args[0].equalsIgnoreCase("settings")) {
                    if (sender.hasPermission("repairPlus.admin")) {
                        Player p = (Player) sender;
                        SettingsManager.openEditor(p);
                    }
                } else if (args[0].equalsIgnoreCase("inf") || args[0].equalsIgnoreCase("infinity")) {
                    Player player = (Player) sender;
                    if (player.hasPermission("repairplus.admin")) {
                        Block b = player.getTargetBlock((Set<Material>) null, 200);
                        if (b.getType() == Material.ANVIL) {
                            String loc = Arconix.pl().serialize().serializeLocation(b);
                            if (plugin.getConfig().getString("data.anvil." + loc + ".active") == null) {
                                plugin.getConfig().set("data.anvil." + loc + ".active", true);
                            }
                            if (plugin.getConfig().getString("data.anvil." + loc + ".inf") == null) {
                                plugin.getConfig().set("data.anvil." + loc + ".inf", true);
                                player.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&aInfinity &9enabled &afor this anvil."));
                            } else {
                                plugin.getConfig().set("data.anvil." + loc + ".inf", null);
                                player.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&aInfinity &9disabled &afor this anvil."));
                            }
                            plugin.saveConfig();
                        }
                    }
                } else {
                    sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Invalid argument.."));
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
        return true;
    }
}
