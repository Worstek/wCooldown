package com.worstek;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class wCooldown extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().addDefault("format-seconds", "s");
        getConfig().addDefault("format-minutes", "m");
        getConfig().addDefault("format-hours", "h");
        getConfig().addDefault("format-days", "d");
        getConfig().options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("wcooldown").setExecutor(this);
        getCommand("cooldown").setExecutor(this);
        getCommand("wcooldown").setTabCompleter(this);
        getCommand("cooldown").setTabCompleter(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().substring(1).split(" ")[0].toLowerCase();
        ConfigurationSection section = getConfig().getConfigurationSection("cooldowns");
        if (section == null) return;

        Player player = event.getPlayer();

        if (player.hasPermission("wcooldown.admin")) return;

        for (String key : section.getKeys(false)) {
            if (section.getStringList(key + ".commands").contains(cmd)) {
                if (player.hasPermission("wcooldown.bypass.*") || player.hasPermission("wcooldown.bypass." + key)) return;

                ConfigurationSection groups = section.getConfigurationSection(key + ".groups");
                long cooldownSeconds = 60;
                boolean found = false;

                if (groups != null) {
                    for (String group : groups.getKeys(false)) {
                        if (group.equalsIgnoreCase("default")) continue;
                        if (player.hasPermission("wcooldown.group." + group)) {
                            cooldownSeconds = parseTime(groups.getString(group, "60s"));
                            found = true;
                            break;
                        }
                    }
                    if (!found && groups.contains("default")) {
                        cooldownSeconds = parseTime(groups.getString("default", "60s"));
                    }
                }

                UUID uuid = player.getUniqueId();
                long now = System.currentTimeMillis();
                cooldowns.putIfAbsent(uuid, new HashMap<>());

                long lastUsed = cooldowns.get(uuid).getOrDefault(key, 0L);
                long newEndTime = now + (cooldownSeconds * 1000);

                if (now >= lastUsed || newEndTime < lastUsed) {
                    cooldowns.get(uuid).put(key, newEndTime);
                    return;
                }

                event.setCancelled(true);
                long totalRemaining = (lastUsed - now) / 1000;

                long d = totalRemaining / 86400;
                long h = (totalRemaining % 86400) / 3600;
                long m = (totalRemaining % 3600) / 60;
                long s = totalRemaining % 60;

                StringBuilder sb = new StringBuilder();
                String fs = getConfig().getString("format-seconds", "s");
                String fm = getConfig().getString("format-minutes", "m");
                String fh = getConfig().getString("format-hours", "h");
                String fd = getConfig().getString("format-days", "d");

                if (d > 0) sb.append(d).append(fd).append(" ");
                if (h > 0) sb.append(h).append(fh).append(" ");
                if (m > 0) sb.append(m).append(fm).append(" ");
                if (s > 0 || sb.length() == 0) sb.append(s).append(fs);

                String fmt = sb.toString().trim();

                for (String line : section.getStringList(key + ".message")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', line
                            .replace("%cooldown%", fmt)
                            .replace("%cooldown_days%", String.valueOf(totalRemaining / 86400))
                            .replace("%cooldown_hours%", String.valueOf(totalRemaining / 3600))
                            .replace("%cooldown_minutes%", String.valueOf(totalRemaining / 60))
                            .replace("%cooldown_seconds%", String.valueOf(totalRemaining))
                            .replace("%command%", cmd)));
                }
                break;
            }
        }
    }

    private long parseTime(String time) {
        try {
            String unit = time.substring(time.length() - 1).toLowerCase();
            long value = Long.parseLong(time.substring(0, time.length() - 1));
            switch (unit) {
                case "s": return value;
                case "m": return value * 60;
                case "h": return value * 3600;
                case "d": return value * 86400;
                default: return value;
            }
        } catch (Exception e) { return 60; }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("wcooldown.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§x§7§4§A§C§F§6● &fConfiguration was reloaded"));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return (args.length == 1) ? Collections.singletonList("reload") : Collections.emptyList();
    }
}