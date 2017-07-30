package ml.nextuniverse.StaffUtilities.commands;

import ml.nextuniverse.StaffUtilities.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends Command{
    public ReportCommand() {
        super("report");
    }

    ScheduledTask task;
    ScheduledTask task2;

    List<String> cantReport = new ArrayList<>();

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            if (strings.length >= 2) {
                boolean isValid = false;
                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                    if (p.getName().equalsIgnoreCase(strings[0])) isValid = true;
                }

                    if (isValid) {
                        if (!cantReport.contains(commandSender.getName())) {
                            if (strings[0].equals(commandSender.getName())) {
                                commandSender.sendMessage(ChatColor.RED + "You cannot report yourself!");
                                return;
                            }

                            StringBuilder sb = new StringBuilder();
                            int i = 0;
                            for (String s : strings) {
                                if (i != 0) {
                                    sb.append(s);
                                }
                                i++;
                            }
                            final String reason = sb.toString();
                            final UUID id = UUID.randomUUID();
                            final String sender = commandSender.getName();
                            Main.getReportReporter().put(id, sender);
                            Main.getReportPlayers().put(id, strings[0]);
                            Main.getReportReason().put(id, reason);
                            ComponentBuilder cb = new ComponentBuilder("[Reports] ").color(ChatColor.DARK_GREEN);
                            cb.append(strings[0]).color(ChatColor.WHITE).bold(false).append(" has been reported. ").color(ChatColor.GREEN);
                            HoverEvent hv = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reported Player: ").color(ChatColor.GREEN).append(strings[0] + "\n").color(ChatColor.WHITE).append("Reason: ").color(ChatColor.GREEN).append(reason + "\n").color(ChatColor.WHITE).append("Reported By: ").color(ChatColor.GREEN).append(sender + "\n").color(ChatColor.WHITE).append("Click to claim").color(ChatColor.DARK_GREEN).create());
                            ClickEvent cv = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report whyareyoureadingthislol " + id);
                            cb.append("[More Information]").color(ChatColor.DARK_GREEN).event(hv).event(cv);
                            BaseComponent[] report = cb.create();
                            commandSender.sendMessage(ChatColor.GREEN + "Your report has been submitted.");
                            int staffs = 0;
                            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                                if (p.hasPermission("reports.show")) {
                                    p.sendMessage(report);
                                    staffs++;
                                }

                            }
                            if (staffs == 0) {
                                Jedis jedis = new Jedis("localhost");
                                jedis.publish("StaffUtils", "DiscordReport;" + strings[0] + ";" + reason + ";" + commandSender.getName());
                            }
                            cantReport.add(sender);
                            task = ProxyServer.getInstance().getScheduler().schedule(Main.getPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    if (!Main.getReportClaimed().containsKey(id)) {
                                        Jedis jedis = new Jedis("localhost");
                                        jedis.publish("StaffUtils", "DiscordReport;" + strings[0] + ";" + reason + ";" + commandSender.getName());
                                    }
                                    cantReport.remove(sender);
                                    task.cancel();

                                }
                            }, 10, TimeUnit.MINUTES);
                            task2 = ProxyServer.getInstance().getScheduler().schedule(Main.getPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    Main.getReportClaimed().remove(id);
                                    Main.getReportPlayers().remove(id);
                                    Main.getReportReason().remove(id);
                                    Main.getReportReporter().remove(id);

                                }
                            }, 60, TimeUnit.MINUTES);
                        } else {
                            commandSender.sendMessage(ChatColor.RED + "You have recently reported and so cannot report again for around 10 minutes.");
                        }

                    } else if (strings[0].equalsIgnoreCase("whyareyoureadingthislol") && commandSender.hasPermission("reports.show")) {
                        if (strings.length == 2) {
                            UUID id = UUID.fromString(strings[1]);
                            if (Main.getReportReason().containsKey(id) && !Main.getReportClaimed().containsKey(id)) {
                                String reason = Main.getReportReason().get(id);
                                String sender = Main.getReportReporter().get(id);
                                String target = Main.getReportPlayers().get(id);
                                String claimed = commandSender.getName();
                                Main.getReportClaimed().put(id, claimed);
                                ComponentBuilder cb = new ComponentBuilder("[Reports] ").color(ChatColor.DARK_GREEN);
                                cb.append(claimed).color(ChatColor.WHITE).bold(false).append(" has claimed the report on ").color(ChatColor.GREEN);
                                HoverEvent hv = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reported Player: ").color(ChatColor.GREEN).append(target + "\n").color(ChatColor.WHITE).append("Reason: ").color(ChatColor.GREEN).append(reason + "\n").color(ChatColor.WHITE).append("Reported By: ").color(ChatColor.GREEN).append(sender).color(ChatColor.WHITE).create());
                                try {
                                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sender);
                                    if (p.isConnected())
                                        p.sendMessage(ChatColor.GREEN + "A staff member is on their way to solve your report. Please have any evidence ready.");
                                } catch (Exception e) {

                                }
                                cb.append(target).color(ChatColor.DARK_GREEN).event(hv);
                                commandSender.sendMessage(cb.create());
                            } else if (Main.getReportClaimed().containsKey(id)) {
                                String claimed = Main.getReportClaimed().get(id);
                                ComponentBuilder cb = new ComponentBuilder("[Reports] ").color(ChatColor.DARK_GREEN).bold(false);
                                cb.append(claimed).color(ChatColor.WHITE).bold(false).append(" is already handling that report!").color(ChatColor.GREEN);
                                commandSender.sendMessage(cb.create());
                            } else {
                                commandSender.sendMessage(ChatColor.DARK_RED + "[Reports]" + ChatColor.RED + "The UUID provided does not match a open report. Perhaps it has expired? (Reports expire after 60 minutes)");
                            }
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Player must be online!");
                    }

            }
            else {
                commandSender.sendMessage(ChatColor.DARK_GRAY + "=====================================================");
                commandSender.sendMessage(ChatColor.GREEN + "/report sends a message to all online staff. The command is used to report" +
                        " players for breaking the rules." + ChatColor.RED + "\nPlease do not use this command to get help or to try and attract a staff members " +
                        "attention. Abusing this command is" + ChatColor.BOLD + "bannable" + ChatColor.RED + "!" + ChatColor.GREEN + "\nSyntax: " +
                        ChatColor.WHITE + "/report <player> <reason>");
                commandSender.sendMessage(ChatColor.DARK_GRAY + "=====================================================");

            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "You seriously thought the console could do that?");
        }
    }
}
