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
package io.github.namiuni.osakana.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.namiuni.osakana.config.annotations.ConfigFile;
import io.github.namiuni.osakana.config.annotations.ConfigHeader;
import io.github.namiuni.osakana.config.serializers.LocaleSerializer;
import io.github.namiuni.osakana.minecraft.paper.annotations.DataDirectory;
import java.nio.file.Path;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@NullMarked
public final class ConfigModule extends AbstractModule {

    public ConfigModule() {
    }

    @Provides
    @Singleton
    private PrimaryConfig primaryConfig(final @DataDirectory Path dataDirectory, final LocaleSerializer localeSerializer) throws ConfigurateException {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(PrimaryConfig.class.getAnnotation(ConfigHeader.class).value())
                        .serializers(builder -> builder.register(Locale.class, localeSerializer)))
                .path(dataDirectory.resolve(PrimaryConfig.class.getAnnotation(ConfigFile.class).value()))
                .build();

        final ConfigurationNode node = loader.load();
        final PrimaryConfig config = node.get(PrimaryConfig.class, PrimaryConfig.DEFAULT);

        final ConfigurationNode updateNode = this.updateNode(node);
        loader.save(updateNode);
        return config;
    }

    @Provides
    private ConfigurationLoader<?> loader(final @DataDirectory Path dataDirectory, final LocaleSerializer localeSerializer) {
        return HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(PrimaryConfig.class.getAnnotation(ConfigHeader.class).value())
                        .serializers(builder -> builder.register(Locale.class, localeSerializer)))
                .path(dataDirectory.resolve(PrimaryConfig.class.getAnnotation(ConfigFile.class).value()))
                .build();
    }

    private <N extends ConfigurationNode> N updateNode(final N node) throws ConfigurateException {
        if (!node.virtual()) {
            final ConfigurationTransformation.Versioned trans = this.version();
            trans.apply(node);
        }
        return node;
    }

    private ConfigurationTransformation.Versioned version() {
        return ConfigurationTransformation.versionedBuilder()
                .addVersion(0, ConfigurationTransformation.builder().build())
                .build();
    }
}
