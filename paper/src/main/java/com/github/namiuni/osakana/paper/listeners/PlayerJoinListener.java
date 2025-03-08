/*
 * Osakana
 *
 * Copyright (c) 2025. Namiu (Unitarou)
 *                     Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.namiuni.osakana.paper.listeners;

import com.github.namiuni.osakana.common.angler.AnglerService;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PlayerJoinListener implements Listener {

    private final AnglerService<Player> anglerService;

    @Inject
    private PlayerJoinListener(final AnglerService<Player> anglerService) {
        this.anglerService = anglerService;
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent event) {
        this.anglerService.load(event.getPlayer());
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent event) {
//    this.anglerManager.saveIfNeeded(event.getPlayer());
    }
}
