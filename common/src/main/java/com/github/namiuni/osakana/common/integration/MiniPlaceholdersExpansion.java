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
package com.github.namiuni.osakana.common.integration;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import java.util.Objects;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MiniPlaceholdersExpansion {

    private static byte miniPlaceholdersLoaded = -1;

    private final ComponentLogger logger;

    @NullMarked
    private MiniPlaceholdersExpansion(final ComponentLogger logger) {
        this.logger = logger;
    }

    public static boolean miniPlaceholdersLoaded() {
        if (miniPlaceholdersLoaded == -1) {
            try {
                final String name = MiniPlaceholders.class.getName();
                Objects.requireNonNull(name);
                miniPlaceholdersLoaded = 1;
            } catch (final NoClassDefFoundError error) {
                miniPlaceholdersLoaded = 0;
            }
        }
        return miniPlaceholdersLoaded == 1;
    }

    public void registerExpansion() {
        if (miniPlaceholdersLoaded()) {
            final var expansion = Expansion.builder("osakana")
//                    .audiencePlaceholder("lastfishtekina", (audience, queue, ctx) -> )
                .build();
            expansion.register();
            this.logger.info("Register extensions to MiniPlaceholders");
        } else {
            this.logger.warn("MiniPlaceholders is not installed. Skip placeholder registration.");
        }
    }
}
