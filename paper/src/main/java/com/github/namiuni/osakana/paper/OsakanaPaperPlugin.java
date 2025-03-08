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

import com.google.inject.Inject;
import java.util.Set;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OsakanaPaperPlugin extends JavaPlugin {

    private final Set<Listener> listeners;

    @Inject
    private OsakanaPaperPlugin(final Set<Listener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void onLoad() {
        // TODO    OsakanaProvider.register();
    }

    @Override
    public void onEnable() {
        this.listeners.forEach(listener ->
            this.getServer().getPluginManager().registerEvents(listener, this));
    }
}
