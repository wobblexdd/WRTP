package me.klouse.krtp.listener;

import me.klouse.krtp.manager.CountdownManager;
import me.klouse.krtp.service.RtpService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public final class PlayerKickListener implements Listener {

    private final RtpService rtpService;
    private final CountdownManager countdownManager;

    public PlayerKickListener(RtpService rtpService, CountdownManager countdownManager) {
        this.rtpService = rtpService;
        this.countdownManager = countdownManager;
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        countdownManager.cancel(event.getPlayer().getUniqueId(), false, null);
        rtpService.clearSearchState(event.getPlayer().getUniqueId());
    }
}
