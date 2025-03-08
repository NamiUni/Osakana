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

import com.github.namiuni.osakana.api.angler.Angler;
import com.github.namiuni.osakana.common.angler.AnglerService;
import com.github.namiuni.osakana.paper.fishing.PaperFishingContext;
import com.github.namiuni.osakana.paper.fishing.PaperFishingHandler;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class FishingListener implements Listener {

    private final AnglerService<Player> anglerService;
    private final PaperFishingHandler fishingHandler;

    @Inject
    private FishingListener(
        final AnglerService<Player> anglerService,
        final PaperFishingHandler fishingHandler
    ) {
        this.anglerService = anglerService;
        this.fishingHandler = fishingHandler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onFish(final PlayerFishEvent event) {
        final Angler<Player> paperAngler = this.anglerService.load(event.getPlayer()).join();
        this.fishingHandler.handle(new PaperFishingContext(paperAngler, event));
    }
}
