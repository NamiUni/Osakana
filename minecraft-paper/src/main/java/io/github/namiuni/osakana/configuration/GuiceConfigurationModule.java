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
package io.github.namiuni.osakana.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.namiuni.osakana.configuration.annotations.ConfigFile;
import io.github.namiuni.osakana.configuration.annotations.ConfigHeader;
import io.github.namiuni.osakana.configuration.configurations.PrimaryConfig;
import io.github.namiuni.osakana.configuration.serializers.LocaleSerializer;
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
@SuppressWarnings("unused")
public final class GuiceConfigurationModule extends AbstractModule {

    public GuiceConfigurationModule() {
    }

    @Provides
    @Singleton
    private PrimaryConfig primaryConfig(final ConfigurationNode node) throws ConfigurateException {
        return node.get(PrimaryConfig.class, PrimaryConfig.DEFAULT);
    }

    @Provides
    @Singleton
    private ConfigurationNode node(final ConfigurationLoader<?> loader) throws ConfigurateException {
        return loader.load();
    }

    @Provides
    @Singleton
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

    @Override
    protected void configure() {
        this.bind(ConfigurationTransformation.Versioned.class).toInstance(this.version());
    }

    private ConfigurationTransformation.Versioned version() {
        return ConfigurationTransformation.versionedBuilder()
                .addVersion(0, ConfigurationTransformation.builder().build())
                .build();
    }
}
