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

import com.github.namiuni.osakana.common.angler.AnglerService;
import com.github.namiuni.osakana.common.command.OsakanaCommandManager;
import com.github.namiuni.osakana.common.configuration.ConfigurationManager;
import com.github.namiuni.osakana.common.datapack.DataPackManager;
import com.github.namiuni.osakana.common.translation.TranslationManager;
import com.google.inject.Injector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ResourceInitializer {

    private ResourceInitializer() {

    }

    public static void initialize(final Injector injector) {
        injector.getInstance(ConfigurationManager.class).loadConfigurations();
        injector.getInstance(TranslationManager.class).loadTranslations();
//        injector.getInstance(AnglerService.class);
        injector.getInstance(DataPackManager.class).discoverDataPack();
        injector.getInstance(OsakanaCommandManager.class).registerCommands();
    }
}
