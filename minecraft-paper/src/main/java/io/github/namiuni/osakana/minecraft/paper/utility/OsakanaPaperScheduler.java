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
package io.github.namiuni.osakana.minecraft.paper.utility;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class OsakanaPaperScheduler {

    private final Provider<Plugin> plugin;

    @Inject
    private OsakanaPaperScheduler(final Provider<Plugin> plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTask(final Runnable task) {
        return Bukkit.getScheduler().runTask(this.plugin.get(), task);
    }

    public BukkitTask runTaskLater(final Runnable task, final long delay) {
        return Bukkit.getScheduler().runTaskLater(this.plugin.get(), task, delay);
    }
}
