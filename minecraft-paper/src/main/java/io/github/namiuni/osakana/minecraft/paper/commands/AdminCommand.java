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
package io.github.namiuni.osakana.minecraft.paper.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.namiuni.osakana.minecraft.paper.OsakanaPermissions;
import io.github.namiuni.osakana.minecraft.paper.annotations.PluginName;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AdminCommand implements OsakanaCommand {

    private final String pluginName;

    @Inject
    private AdminCommand(final @PluginName String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal(this.pluginName.toLowerCase())
                .requires(stack -> stack.getSender().hasPermission(OsakanaPermissions.COMMAND_ADMIN))
                .build();
    }

    @Override
    public String description() {
        return "Osakana provided admin commands.";
    }
}
