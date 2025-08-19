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
package io.github.namiuni.osakana.translation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.github.namiuni.osakana.config.PrimaryConfig;
import io.github.namiuni.osakana.minecraft.paper.OsakanaKey;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TranslatorProvider implements Provider<Translator> {

    private final ComponentLogger logger;
    private final PrimaryConfig primaryConfig;
    private final MiniMessage miniMessage;
    private final ResourceBundle.Control control;

    private final Key key;

    @Inject
    private TranslatorProvider(
            final ComponentLogger logger,
            final PrimaryConfig primaryConfig,
            final OsakanaKey osakanaKey,
            final MiniMessage miniMessage,
            final ResourceBundle.Control control
    ) {
        this.logger = logger;
        this.primaryConfig = primaryConfig;
        this.miniMessage = miniMessage;
        this.control = control;

        this.key = osakanaKey.create("messages");
    }

    @Override
    public Translator get() {
        final MiniMessageTranslationStore translationStore = MiniMessageTranslationStore.create(this.key, this.miniMessage);
        translationStore.defaultLocale(this.primaryConfig.defaultLocale());

        final List<ResourceBundle> bundles = Locale.availableLocales()
                .map(this::resourceBundle)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        bundles.forEach(bundle -> translationStore.registerAll(bundle.getLocale(), bundle, false));

        final String localeNames = bundles.stream()
                .map(ResourceBundle::getLocale)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        this.logger.info("Loaded {} translations: [{}]", bundles.size(), localeNames);

        return translationStore;
    }

    private @Nullable ResourceBundle resourceBundle(final Locale locale) {
        return ResourceBundle.getBundle("translations/%s".formatted(this.key.value()), locale, this.control);
    }
}
