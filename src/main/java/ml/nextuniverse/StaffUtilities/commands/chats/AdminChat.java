package ml.nextuniverse.StaffUtilities.commands.chats;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AdminChat extends Command {
    public AdminChat() {
        super("ac", "chat.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s + " ");
        }
        sb.trimToSize();
        String message = sb.toString();
        ComponentBuilder chat = new ComponentBuilder("[").color(ChatColor.YELLOW);
        chat.append(commandSender.getName()).color(ChatColor.YELLOW).append("] ").color(ChatColor.GOLD);
        chat.append(message).color(ChatColor.YELLOW);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.hasPermission("chat.admin")) {
                p.sendMessage(chat.create());
            }
        }
    }
}
