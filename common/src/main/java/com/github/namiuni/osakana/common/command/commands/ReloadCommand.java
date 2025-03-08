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
package com.github.namiuni.osakana.common.command.commands;

import com.github.namiuni.osakana.common.command.OsakanaCommand;
import com.github.namiuni.osakana.common.command.OsakanaCommandContext;
import com.github.namiuni.osakana.common.configuration.ConfigurationManager;
import com.github.namiuni.osakana.common.translation.TranslationService;
import com.github.namiuni.osakana.common.translation.TranslationManager;
import com.google.inject.Inject;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ReloadCommand implements OsakanaCommand {

    private final CommandManager<OsakanaCommandContext> commandManager;
    private final ConfigurationManager configManager;
    private final TranslationManager translationManager;
    private final TranslationService translationService;

    @Inject
    private ReloadCommand(
        final CommandManager<OsakanaCommandContext> commandManager,
        final ConfigurationManager configManager,
        final TranslationManager translationManager,
        final TranslationService translationService
    ) {
        this.commandManager = commandManager;
        this.configManager = configManager;
        this.translationManager = translationManager;
        this.translationService = translationService;
    }

    @Override
    public Command<OsakanaCommandContext> create() {
        return this.commandManager.commandBuilder("osakana")
            .permission("osakana.command.reload")
            .handler(context -> {
                this.configManager.loadConfigurations();
                this.translationManager.loadTranslations();
                this.translationService.configReloadSuccess(context.sender());
            })
            .build();
    }
}
