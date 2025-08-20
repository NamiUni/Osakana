/*
 * osakana
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors []
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
package io.github.namiuni.osakana.minecraft.paper.listeners;

import com.google.inject.Inject;
import io.papermc.paper.event.entity.FishHookStateChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class FishingHandler implements Listener {

    @Inject
    private FishingHandler() {
    }

    @EventHandler
    private void onPlayerFish(final PlayerFishEvent event) {

    }

    @EventHandler
    private void onFishHookStateChange(final FishHookStateChangeEvent event) {

    }
}
