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
package com.github.namiuni.osakana.common.configuration;

import com.github.namiuni.osakana.common.DataDirectory;
import com.github.namiuni.osakana.common.configuration.serializers.OsakanaSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.file.Path;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

@Singleton
@NullMarked
public final class ConfigurationManager {

    private static final String PRIMARY_CONFIG_FILE_NAME = "config.conf";

    private final Path dataDirectory;
    private final ComponentLogger logger;
    private final OsakanaSerializer osakanaSerializer;

    private @MonotonicNonNull PrimaryConfig primaryConfig = null;

    @Inject
    private ConfigurationManager(
        final @DataDirectory Path dataDirectory,
        final ComponentLogger logger,
        final OsakanaSerializer osakanaSerializer
    ) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
        this.osakanaSerializer = osakanaSerializer;
    }

    public void loadConfigurations() {
        this.logger.info("Loading configurations...");
        try {
            this.primaryConfig = this.load(PrimaryConfig.class, PRIMARY_CONFIG_FILE_NAME);
        } catch (final ConfigurateException exception) {
            throw new UncheckedConfigurateException("Unable to load configurations", exception);
        }
        this.logger.info("Successfully loaded configurations: {}", PRIMARY_CONFIG_FILE_NAME);
    }

    public PrimaryConfig primaryConfig() {
        return this.primaryConfig;
    }

    public ConfigurationLoader<CommentedConfigurationNode> configurationLoader(final Path file) {
        final ConfigurateComponentSerializer kyoriSerializer = ConfigurateComponentSerializer.configurate();

        return HoconConfigurationLoader.builder()
            .prettyPrinting(true)
            .defaultOptions(options -> options
                .shouldCopyDefaults(true)
                .serializers(serializerBuilder -> serializerBuilder
                    .registerAll(kyoriSerializer.serializers())
                    .registerAll(this.osakanaSerializer.serializers()))
            )
            .path(file)
            .build();
    }

    public <T> T load(final Class<T> clazz, final String fileName) throws ConfigurateException {
        final Path file = this.dataDirectory.resolve(fileName);
        final ConfigurationLoader<CommentedConfigurationNode> loader = this.configurationLoader(file);

        final CommentedConfigurationNode node = loader.load();
        final T config = node.get(clazz);
        if (config == null) {
            throw new ConfigurateException(node, "Failed to deserialize " + clazz.getName() + " from node");
        }

        node.set(clazz, config);
        loader.save(node);

        return config;
    }
}
