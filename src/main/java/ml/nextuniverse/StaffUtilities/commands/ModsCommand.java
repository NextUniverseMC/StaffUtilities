package ml.nextuniverse.StaffUtilities.commands;

import ml.nextuniverse.StaffUtilities.CenterText;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by TheDiamondPicks on 13/02/2017.
 */
public class ModsCommand extends Command {
    public ModsCommand() {
        super("staff","staff.show");
    }

    private static List<ProxiedPlayer> spectators = new ArrayList<>();
    private static List<ProxiedPlayer> vanished = new ArrayList<>();

    public static List<ProxiedPlayer> getSpectators() {
        return spectators;
    }

    public static List<ProxiedPlayer> getVanished() {
        return vanished;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.DARK_GRAY + "=====================================================");
            CenterText.sendCenteredMessage(commandSender, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Staff Online");
            ComponentBuilder owner = new ComponentBuilder("Owner: ").color(ChatColor.RED).bold(true);
            ComponentBuilder admin = new ComponentBuilder("Administrator: ").color(ChatColor.YELLOW).bold(true);
            ComponentBuilder mod = new ComponentBuilder("Moderator: ").color(ChatColor.DARK_GREEN).bold(true);
            ComponentBuilder helper = new ComponentBuilder("Helper: ").color(ChatColor.AQUA).bold(true);
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if (p.hasPermission("staff.owner")) {
                    String suffix = "";
                    if (getSpectators().contains(p)) suffix = " [S]";
                    else if (getVanished().contains(p)) suffix = " [V]";
                    else suffix = "";
                    owner.append(p.getName()).color(ChatColor.WHITE).bold(false);
                    owner.append(suffix + " ").color(ChatColor.GRAY);

                }
                else if (p.hasPermission("staff.admin")) {
                    String suffix = "";
                    if (getSpectators().contains(p)) suffix = " [S]";
                    else if (getVanished().contains(p)) suffix = " [V]";
                    else suffix = "";
                    admin.append(p.getName()).color(ChatColor.WHITE).bold(false);
                    admin.append(suffix + " ").color(ChatColor.GRAY);
                }
                else if (p.hasPermission("staff.mod")) {
                    String suffix = "";
                    if (getSpectators().contains(p)) suffix = " [S]";
                    else if (getVanished().contains(p)) suffix = " [V]";
                    else suffix = "";
                    mod.append(p.getName()).color(ChatColor.WHITE).bold(false);
                    mod.append(suffix + " ").color(ChatColor.GRAY);
                }
                else if (p.hasPermission("staff.helper")) {
                    String suffix = "";
                    if (getSpectators().contains(p)) suffix = " [S]";
                    else if (getVanished().contains(p)) suffix = " [V]";
                    else suffix = "";
                    helper.append(p.getName()).color(ChatColor.WHITE).bold(false);
                    helper.append(suffix + " ").color(ChatColor.GRAY);
                }
            }

            commandSender.sendMessage(owner.create());
            commandSender.sendMessage(admin.create());
            commandSender.sendMessage(mod.create());
            commandSender.sendMessage(helper.create());
            commandSender.sendMessage(ChatColor.DARK_GRAY + "=====================================================");
        }

    }
}
