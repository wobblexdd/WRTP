package me.klouse.krtp;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import me.klouse.krtp.command.RtpCommand;
import me.klouse.krtp.gui.RtpGui;
import me.klouse.krtp.listener.GuiListener;
import me.klouse.krtp.listener.PlayerKickListener;
import me.klouse.krtp.listener.PlayerMoveListener;
import me.klouse.krtp.listener.PlayerQuitListener;
import me.klouse.krtp.manager.ConfigurationManager;
import me.klouse.krtp.manager.CooldownManager;
import me.klouse.krtp.manager.CountdownManager;
import me.klouse.krtp.service.RtpService;
import me.klouse.krtp.service.SafeLocationFinder;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class KRTP extends JavaPlugin {

    private ExecutorService searchExecutor;
    private ConfigurationManager configurationManager;
    private CountdownManager countdownManager;
    private RtpService rtpService;

    @Override
    public void onEnable() {
        this.searchExecutor = Executors.newFixedThreadPool(
                Math.max(2, Runtime.getRuntime().availableProcessors() / 2),
                new SearchThreadFactory()
        );

        this.configurationManager = new ConfigurationManager(this);
        CooldownManager cooldownManager = new CooldownManager();
        this.countdownManager = new CountdownManager(this, configurationManager);
        SafeLocationFinder safeLocationFinder = new SafeLocationFinder(this, configurationManager, searchExecutor);
        this.rtpService = new RtpService(this, configurationManager, cooldownManager, countdownManager, safeLocationFinder);
        RtpGui rtpGui = new RtpGui(configurationManager, rtpService);

        PluginCommand command = Objects.requireNonNull(getCommand("rtp"), "rtp command missing from plugin.yml");
        RtpCommand rtpCommand = new RtpCommand(configurationManager, rtpGui);
        command.setExecutor(rtpCommand);
        command.setTabCompleter(rtpCommand);

        getServer().getPluginManager().registerEvents(new GuiListener(rtpGui), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(countdownManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(rtpService, countdownManager), this);
        getServer().getPluginManager().registerEvents(new PlayerKickListener(rtpService, countdownManager), this);
    }

    @Override
    public void onDisable() {
        if (rtpService != null) {
            rtpService.shutdown();
        }
        if (countdownManager != null) {
            countdownManager.cancelAll(false);
        }
        if (searchExecutor != null) {
            searchExecutor.shutdownNow();
        }
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    private static final class SearchThreadFactory implements ThreadFactory {
        private int counter = 1;

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "KRTP-Search-" + counter++);
            thread.setDaemon(true);
            return thread;
        }
    }
}
