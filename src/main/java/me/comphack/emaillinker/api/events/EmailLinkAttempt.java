package me.comphack.emaillinker.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EmailLinkAttempt extends Event {

     private final Player player;
     private final String email;
     private final boolean success;

    public EmailLinkAttempt(Player player, String email, boolean success) {
        this.player = player;
        this.email = email;
        this.success = success;
    }

    public Player getPlayer() {
        return player;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuccess() {
        return success;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
