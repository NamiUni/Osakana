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
package com.github.namiuni.osakana.paper;

import com.github.namiuni.osakana.common.util.OsakanaDependencies;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.gremlin.runtime.platformsupport.PaperClasspathAppender;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@NullMarked
public final class OsakanaPaperLoader implements PluginLoader {

    @Override
    public void classloader(final PluginClasspathBuilder classpathBuilder) {
        new PaperClasspathAppender(classpathBuilder).append(
            OsakanaDependencies.resolve(classpathBuilder.getContext().getDataDirectory().resolve("libraries"))
        );
    }
}
