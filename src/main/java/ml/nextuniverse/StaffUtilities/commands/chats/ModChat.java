package ml.nextuniverse.StaffUtilities.commands.chats;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ModChat extends Command {
    public ModChat() {
        super("mc", "chat.mod");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s + " ");
        }
        sb.trimToSize();
        String message = sb.toString();
        ComponentBuilder chat = new ComponentBuilder("[").color(ChatColor.DARK_GREEN);
        chat.append(commandSender.getName()).color(ChatColor.DARK_GREEN).append("] ").color(ChatColor.DARK_GREEN);
        chat.append(message).color(ChatColor.DARK_GREEN);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.hasPermission("chat.mod")) {
                p.sendMessage(chat.create());
            }
        }
    }
}
