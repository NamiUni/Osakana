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
package io.github.namiuni.osakana.api.user;

import java.util.Locale;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

/**
 * The abstract user of this plugin.
 */
@NullMarked
public interface OsakanaUser extends Audience, Identified {

    /**
     * Gets the uuid of this user.
     *
     * @return the uuid
     */
    UUID uuid();

    /**
     * Gets the "friendly" name to display of this user.
     *
     * @return the display name
     */
    default Component displayName() {
        return this.getOrDefault(Identity.DISPLAY_NAME, Component.text("UnknownOsakanaUser"));
    }

    /**
     * Gets the name of this user.
     *
     * @return the name
     */
    default String name() {
        return this.getOrDefault(Identity.NAME, "UnknownOsakanaUser");
    }

    /**
     * Gets the locale of this user.
     *
     * @return the locale
     */
    default Locale locale() {
        return this.getOrDefault(Identity.LOCALE, Locale.ROOT);
    }
}
