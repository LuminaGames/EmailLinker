package me.comphack.emaillinker.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EmailLinkEvent extends Event {

    private final Player player;
    private final String email;
    private static final HandlerList HANDLERS_LIST = new HandlerList();


    public EmailLinkEvent(Player player, String email) {
        this.player = player;
        this.email = email;
    }

    public Player getPlayer() {
        return player;
    }

    public String getEmail() {
        return email;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}

