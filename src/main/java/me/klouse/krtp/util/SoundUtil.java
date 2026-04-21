package me.klouse.krtp.util;

import me.klouse.krtp.model.ConfiguredSound;
import org.bukkit.entity.Player;

public final class SoundUtil {

    private SoundUtil() {
    }

    public static void play(Player player, ConfiguredSound sound) {
        if (player == null || sound == null || !sound.enabled()) {
            return;
        }
        player.playSound(player.getLocation(), sound.sound(), sound.volume(), sound.pitch());
    }
}
