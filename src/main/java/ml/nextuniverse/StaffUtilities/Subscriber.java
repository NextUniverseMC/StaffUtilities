package ml.nextuniverse.StaffUtilities;

import ml.nextuniverse.StaffUtilities.commands.ModsCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.JedisPubSub;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


public class Subscriber extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals("StaffUtils")) {
            String[] args = message.split(";");
            if (args[0].equals("VanishStart")) {
                ModsCommand.getVanished().add(ProxyServer.getInstance().getPlayer(args[1]));
            }
            else if (args[0].equals("VanishEnd")) {
                ModsCommand.getVanished().remove(ProxyServer.getInstance().getPlayer(args[1]));
            }
            else if (args[0].equals("SpectateStart")) {
                if (!ModsCommand.getSpectators().contains(ProxyServer.getInstance().getPlayer(args[1])))
                    ModsCommand.getSpectators().add(ProxyServer.getInstance().getPlayer(args[1]));
            }
            else if (args[0].equals("SpectateEnd")) {
                ModsCommand.getSpectators().remove(ProxyServer.getInstance().getPlayer(args[1]));
            }
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}

