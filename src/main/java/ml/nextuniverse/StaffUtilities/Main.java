package ml.nextuniverse.StaffUtilities;

import ml.nextuniverse.StaffUtilities.commands.*;
import ml.nextuniverse.StaffUtilities.commands.ModsCommand;
import ml.nextuniverse.StaffUtilities.commands.chats.*;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by TheDiamondPicks on 13/02/2017.
 */
public class Main extends Plugin{
    private static HashMap<UUID, String> reportReason = new HashMap<>();
    private static HashMap<UUID, String> reportPlayers = new HashMap<>();
    private static HashMap<UUID, String> reportReporter = new HashMap<>();
    private static HashMap<UUID, String> reportClaimed = new HashMap<>();

    public static HashMap<UUID, String> getReportClaimed() {
        return reportClaimed;
    }

    public static HashMap<UUID, String> getReportReason() {
        return reportReason;
    }

    public static HashMap<UUID, String> getReportPlayers() {
        return reportPlayers;
    }

    public static HashMap<UUID, String> getReportReporter() {
        return reportReporter;
    }

    JedisPoolConfig poolConfig;
    JedisPool jedisPool;
    Jedis subscriberJedis;
    Subscriber subscriber;

    private static Plugin plugin;


    @Override
    public void onEnable() {
        plugin = this;

        poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
        subscriberJedis = jedisPool.getResource();
        subscriber = new Subscriber();

        new Thread(new Runnable() {
            public void run() {
                try {
                    getLogger().info("Subscribing to \"StaffUtils\". This thread will be blocked.");
                    subscriberJedis.subscribe(subscriber, "StaffUtils");
                    getLogger().info("Subscription ended.");
                } catch (Exception e) {
                    getLogger().severe("Subscribing failed." + e);
                }
            }
        }).start();

        getProxy().getPluginManager().registerCommand(this, new ModsCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportCommand());
        getProxy().getPluginManager().registerCommand(this, new AdminChat());
        getProxy().getPluginManager().registerCommand(this, new ModChat());
        getProxy().getPluginManager().registerCommand(this, new HelperChat());
        getProxy().getPluginManager().registerCommand(this, new DonatorChat());
    }

    @Override
    public void onDisable() {
        plugin = null;
    }
    public static Plugin getPlugin() { return plugin; }
}
