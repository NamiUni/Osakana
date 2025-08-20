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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.github.namiuni.osakana.config.ConfigModule;
import io.github.namiuni.osakana.database.DatabaseModule;
import io.github.namiuni.osakana.minecraft.paper.commands.OsakanaCommand;
import io.github.namiuni.osakana.translation.TranslationModule;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Set;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class OsakanaPaperBootstrap implements PluginBootstrap {

    private @MonotonicNonNull Injector injector;

    @Override
    public void bootstrap(final BootstrapContext context) {
        this.injector = Guice.createInjector(
                new OsakanaPaperModule(context),
                new ConfigModule(),
                new TranslationModule(),
                new DatabaseModule()
        );

        final Translator translator = this.injector.getInstance(Translator.class);
        GlobalTranslator.translator().addSource(translator);

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Set<OsakanaCommand> commands = this.injector.getInstance(Key.get(new TypeLiteral<>() { }));
            commands.forEach(command -> event.registrar().register(command.create(), command.description(), command.aliases()));
        });
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return this.injector.getInstance(OsakanaPaperPlugin.class);
    }
}
