package ml.nextuniverse.StaffUtilities.commands.chats;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class DonatorChat extends Command {
    public DonatorChat() {
        super("dc", "chat.donator");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s + " ");
        }
        sb.trimToSize();
        String message = sb.toString();
        ComponentBuilder chat = new ComponentBuilder("Donator Chat [").color(ChatColor.BLUE);
        chat.append(commandSender.getName()).color(ChatColor.BLUE).append("] ").color(ChatColor.BLUE);
        chat.append(message).color(ChatColor.BLUE);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.hasPermission("chat.donator")) {
                p.sendMessage(chat.create());
            }
        }
    }
}
