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
package com.github.namiuni.osakana.common.command;

import com.google.inject.Inject;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.NullMarked;
import java.util.Set;

@NullMarked
public final class OsakanaCommandManager {

    private final CommandManager<OsakanaCommandContext> cloudManager;
    private final Set<OsakanaCommand> commands;

    @Inject
    private OsakanaCommandManager(
        final CommandManager<OsakanaCommandContext> cloudManager,
        final Set<OsakanaCommand> commands
    ) {
        this.cloudManager = cloudManager;
        this.commands = commands;
    }

    public void registerCommands() {
        for (final OsakanaCommand command : this.commands) {
            this.cloudManager.command(command.create());
        }
    }
}
