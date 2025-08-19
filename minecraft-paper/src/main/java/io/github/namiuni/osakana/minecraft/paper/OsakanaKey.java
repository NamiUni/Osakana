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
package io.github.namiuni.osakana.minecraft.paper;

import com.google.inject.Inject;
import io.github.namiuni.osakana.minecraft.paper.module.annotations.PluginName;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OsakanaKey {

    private final String namespace;

    @Inject
    private OsakanaKey(final @PluginName String pluginName) {
        this.namespace = pluginName.toLowerCase();
    }

    public Key create(final @Subst("key") String value) {
        return Key.key(this.namespace, value);
    }
}
