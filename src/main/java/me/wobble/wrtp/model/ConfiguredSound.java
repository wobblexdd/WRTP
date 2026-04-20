package me.wobble.wrtp.model;

import org.bukkit.Sound;

public record ConfiguredSound(Sound sound, float volume, float pitch) {

    public static ConfiguredSound disabled() {
        return new ConfiguredSound(null, 1.0F, 1.0F);
    }

    public boolean enabled() {
        return sound != null;
    }
}
