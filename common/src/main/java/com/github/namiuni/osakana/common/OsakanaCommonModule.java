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
package com.github.namiuni.osakana.common;

import com.github.namiuni.osakana.common.angler.repository.JsonUserRepository;
import com.github.namiuni.osakana.common.angler.repository.UserRepository;
import com.github.namiuni.osakana.common.command.OsakanaCommand;
import com.github.namiuni.osakana.common.command.commands.ReloadCommand;
import com.github.namiuni.osakana.common.configuration.ConfigurationManager;
import com.github.namiuni.osakana.common.translation.TranslationService;
import com.github.namiuni.osakana.common.translation.TranslationServiceProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OsakanaCommonModule extends AbstractModule {

    private final ComponentLogger logger;

    public OsakanaCommonModule() {
        this.logger = ComponentLogger.logger("Osakana");
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public UserRepository userRepository(
        final ConfigurationManager configManager,
        final Provider<JsonUserRepository> json
    ) {
        return switch (configManager.primaryConfig().repositoryType()) {
            default -> json.get();
        };
    }

    @Override
    protected void configure() {
        this.bind(ComponentLogger.class).toInstance(this.logger);
        this.bind(TranslationService.class).toProvider(TranslationServiceProvider.class).in(Scopes.SINGLETON);

        this.configureCommands();
    }

    private void configureCommands() {
        final Multibinder<OsakanaCommand> commandBinder = Multibinder.newSetBinder(this.binder(), OsakanaCommand.class);
        commandBinder.addBinding().to(ReloadCommand.class).in(Scopes.SINGLETON);
    }
}
