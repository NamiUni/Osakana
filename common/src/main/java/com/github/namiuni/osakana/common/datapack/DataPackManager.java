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
package com.github.namiuni.osakana.common.datapack;

import com.github.namiuni.osakana.common.configuration.ConfigurationManager;
import java.nio.file.Files;
import java.nio.file.Path;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class DataPackManager {

    protected final ComponentLogger logger;
    protected final ConfigurationManager configManager;

    protected DataPackManager(
        final ComponentLogger logger,
        final ConfigurationManager configManager
    ) {
        this.logger = logger;
        this.configManager = configManager;
    }

    public abstract void discoverDataPack();

    protected @Nullable DataPack loadDataPack() {
        final Path packPath = this.configManager.primaryConfig().fishPack();

        if (packPath == null) {
            return null;
        }

        if (Files.notExists(packPath)) {
            this.logger.warn("Invalid value for data pack path: {}", packPath);
            return null;
        }

        return new DataPack(packPath, DataPackManager.extractPackName(packPath));
    }

    private static String extractPackName(final Path packPath) {
        final String fileName = packPath.getFileName().toString();
        if (fileName.endsWith(".zip")) {
            return fileName.substring(0, fileName.length() - ".zip".length());
        }

        return fileName;
    }
}
