package io.github.namiuni.osakana.minecraft.paper.listeners;

import com.google.inject.Inject;
import io.github.namiuni.osakana.translation.MessageService;
import io.github.namiuni.osakana.user.OsakanaUserServiceImpl;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ConnectionListener implements Listener {

    private final ComponentLogger logger;
    private final MessageService messageService;
    private final OsakanaUserServiceImpl userService;

    @Inject
    private ConnectionListener(
            final ComponentLogger logger,
            final MessageService messageService,
            final OsakanaUserServiceImpl userService
    ) {
        this.logger = logger;
        this.messageService = messageService;
        this.userService = userService;
    }

    @EventHandler
    private void onJoin(final AsyncPlayerPreLoginEvent event) {
        this.userService.handleUserLogin(event.getUniqueId(), event.getPlayerProfile().getName())
                .exceptionally(exception -> {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.messageService.kickUserDataLoadFailed());
                    this.logger.error(this.messageService.kickUserDataLoadFailed(), exception);
                    return null;
                })
                .join();
    }
}
