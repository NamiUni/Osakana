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

import com.google.inject.Inject;
import io.github.namiuni.osakana.config.annotations.ConfigFile;
import io.github.namiuni.osakana.config.annotations.ConfigHeader;
import io.github.namiuni.osakana.config.serializers.LocaleSerializer;
import io.github.namiuni.osakana.minecraft.paper.module.annotations.DataDirectory;
import java.nio.file.Path;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@NullMarked
@ConfigFile("config.conf")
@ConfigHeader("""
        This is the primary configuration file for Osakana.
        Some options may impact gameplay, so use
        with caution, and make sure you know what each option does before configuring.
        """)
@ConfigSerializable
public record PrimaryConfig(
        @Comment("The default locale for plugin messages.") Locale defaultLocale
) {

    public static final PrimaryConfig DEFAULT = new PrimaryConfig(Locale.US);

    public static final class Provider implements com.google.inject.Provider<PrimaryConfig> {

        private final PrimaryConfig primaryConfig;

        @Inject
        private Provider(final @DataDirectory Path dataDirectory, final LocaleSerializer localeSerializer) throws ConfigurateException {
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
            this.primaryConfig = config;
        }

        @Override
        public PrimaryConfig get() {
            return this.primaryConfig;
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
}
