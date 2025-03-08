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

import com.github.namiuni.osakana.common.DataDirectory;
import com.github.namiuni.osakana.common.OsakanaCommonModule;
import com.github.namiuni.osakana.common.PluginSource;
import com.github.namiuni.osakana.common.angler.repository.CookieStore;
import com.github.namiuni.osakana.common.command.OsakanaCommand;
import com.github.namiuni.osakana.common.command.OsakanaCommandContext;
import com.github.namiuni.osakana.common.command.commands.ReloadCommand;
import com.github.namiuni.osakana.common.datapack.DataPackManager;
import com.github.namiuni.osakana.paper.angler.PaperCookieStore;
import com.github.namiuni.osakana.paper.command.PaperCommandContext;
import com.github.namiuni.osakana.paper.datapack.PaperDataPackManager;
import com.github.namiuni.osakana.paper.listeners.FishingListener;
import com.github.namiuni.osakana.paper.listeners.PlayerJoinListener;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import java.nio.file.Path;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class OsakanaPaperModule extends AbstractModule {

    private final BootstrapContext bootstrapContext;

    public OsakanaPaperModule(final BootstrapContext bootstrapContext) {
        this.bootstrapContext = bootstrapContext;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public CommandManager<OsakanaCommandContext> cloudCommandManager() {
        final SenderMapper<CommandSourceStack, OsakanaCommandContext> mapper = SenderMapper.create(PaperCommandContext::new, context -> ((PaperCommandContext) context).sourceStack());
        return PaperCommandManager.builder(mapper)
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildBootstrapped(this.bootstrapContext);
    }

    @Override
    protected void configure() {
        this.install(new OsakanaCommonModule());
        this.bind(Path.class).annotatedWith(DataDirectory.class).toInstance(this.bootstrapContext.getDataDirectory());
        this.bind(Path.class).annotatedWith(PluginSource.class).toInstance(this.bootstrapContext.getPluginSource());
        this.bind(Key.get(new TypeLiteral<LifecycleEventManager<BootstrapContext>>() {})).toInstance(this.bootstrapContext.getLifecycleManager());
        this.bind(DataPackManager.class).to(PaperDataPackManager.class).in(Scopes.SINGLETON);
        this.bind(Key.get(new TypeLiteral<CookieStore<Player>>() {})).to(PaperCookieStore.class).in(Scopes.SINGLETON);
//        this.bind(AnglerService.class).to(new TypeLiteral<AnglerService<Player>>() {}).in(Scopes.SINGLETON);

        this.configureCommands();
        this.configureListeners();

        this.bind(JavaPlugin.class).to(OsakanaPaperPlugin.class).in(Scopes.SINGLETON);
    }

    private void configureCommands() {
        final Multibinder<OsakanaCommand> commandBinder = Multibinder.newSetBinder(this.binder(), OsakanaCommand.class);
        commandBinder.addBinding().to(ReloadCommand.class).in(Scopes.SINGLETON);
    }

    private void configureListeners() {
        final Multibinder<Listener> listenerBinder = Multibinder.newSetBinder(this.binder(), Listener.class);
        listenerBinder.addBinding().to(FishingListener.class).in(Scopes.SINGLETON);
        listenerBinder.addBinding().to(PlayerJoinListener.class).in(Scopes.SINGLETON);
    }
}
