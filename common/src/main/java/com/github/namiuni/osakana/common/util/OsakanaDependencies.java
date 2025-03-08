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
package com.github.namiuni.osakana.common.util;

import java.nio.file.Path;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.jpenilla.gremlin.runtime.DependencyCache;
import xyz.jpenilla.gremlin.runtime.DependencyResolver;
import xyz.jpenilla.gremlin.runtime.DependencySet;

@NullMarked
public final class OsakanaDependencies {

    private OsakanaDependencies() {

    }

    public static Set<Path> resolve(final Path cacheDir) {
        final DependencySet dependencies = DependencySet.readFromClasspathResource(OsakanaDependencies.class.getClassLoader(), "osakana-dependencies.txt");
        final DependencyCache cache = new DependencyCache(cacheDir);
        final Logger logger = LoggerFactory.getLogger(OsakanaDependencies.class.getSimpleName());

        try (final DependencyResolver downloader = new DependencyResolver(logger)) {
            return downloader.resolve(dependencies, cache).jarFiles();
        } finally {
            cache.cleanup();
        }
    }
}
