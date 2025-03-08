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
package com.github.namiuni.osakana.paper.datapack;

import com.github.namiuni.osakana.common.configuration.ConfigurationManager;
import com.github.namiuni.osakana.common.datapack.DataPack;
import com.github.namiuni.osakana.common.datapack.DataPackManager;
import com.google.inject.Inject;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.io.IOException;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public final class PaperDataPackManager extends DataPackManager {

    private final LifecycleEventManager<BootstrapContext> eventManager;

    @Inject
    private PaperDataPackManager(
        final ComponentLogger logger,
        final ConfigurationManager configManager,
        final LifecycleEventManager<BootstrapContext> eventManager
    ) {
        super(logger, configManager);
        this.eventManager = eventManager;
    }

    @Override
    public void discoverDataPack() {
        this.eventManager.registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY, event -> {
            final DataPack dataPack = this.loadDataPack();
            if (dataPack != null) {
                try {
                    event.registrar().discoverPack(dataPack.path(), dataPack.id());
                    this.logger.info("Loaded data pack: {}", dataPack.id());
                } catch (final IOException exception) {
                    this.logger.warn("Failed to load data pack: '{}'", dataPack.path(), exception);
                }
            }
        });
    }
}
