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
package io.github.namiuni.osakana.minecraft.paper.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import io.github.namiuni.osakana.config.PrimaryConfig;
import io.github.namiuni.osakana.minecraft.paper.OsakanaPaperPlugin;
import io.github.namiuni.osakana.minecraft.paper.module.annotations.DataDirectory;
import io.github.namiuni.osakana.minecraft.paper.module.annotations.PluginName;
import io.github.namiuni.osakana.translation.DynamicResourceBundleControl;
import io.github.namiuni.osakana.translation.MessageService;
import io.github.namiuni.osakana.translation.MiniMessageProvider;
import io.github.namiuni.osakana.translation.TranslatorProvider;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import java.nio.file.Path;
import java.util.ResourceBundle;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.Translator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class OsakanaPaperModule extends AbstractModule {

    private final PluginProviderContext context;

    public OsakanaPaperModule(final PluginProviderContext context) {
        this.context = context;
    }

    @Override
    protected void configure() {
        this.bind(Plugin.class).to(OsakanaPaperPlugin.class).in(Scopes.SINGLETON);
        this.bind(Path.class).annotatedWith(DataDirectory.class).toInstance(this.context.getDataDirectory());
        this.bind(String.class).annotatedWith(PluginName.class).toInstance(this.context.getConfiguration().getName());
        this.bind(ComponentLogger.class).toInstance(this.context.getLogger());

        this.bind(ResourceBundle.Control.class).to(DynamicResourceBundleControl.class).in(Scopes.SINGLETON);

        this.bind(MiniMessage.class).toProvider(MiniMessageProvider.class).in(Scopes.SINGLETON);
        this.bind(MessageService.class).toProvider(MessageService.Provider.class).in(Scopes.SINGLETON);
        this.bind(PrimaryConfig.class).toProvider(PrimaryConfig.Provider.class).in(Scopes.SINGLETON);
        this.bind(Translator.class).toProvider(TranslatorProvider.class).in(Scopes.SINGLETON);

        this.configureListeners();
    }

    private void configureListeners() {
        final Multibinder<Listener> listeners = Multibinder.newSetBinder(this.binder(), Listener.class);
        // listeners.addBinding().to(/* listener class */);
    }
}
