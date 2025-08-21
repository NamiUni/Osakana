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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.namiuni.doburoku.standard.DoburokuStandard;
import io.github.namiuni.doburoku.standard.argument.MiniMessageArgumentTransformer;
import io.github.namiuni.osakana.config.PrimaryConfig;
import io.github.namiuni.osakana.integration.MiniPlaceholdersExpansion;
import io.github.namiuni.osakana.utility.OsakanaKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public final class TranslationModule extends AbstractModule {

    public TranslationModule() {
    }

    @Provides
    private List<ResourceBundle> bundles(final ResourceBundle.Control control) {
        final String baseName = MessageService.class.getAnnotation(io.github.namiuni.doburoku.annotation.annotations.ResourceBundle.class).baseName();
        return Locale.availableLocales()
                .map(locale -> ResourceBundle.getBundle(baseName, locale, control))
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Provides
    private Translator translator(
            final ComponentLogger logger,
            final PrimaryConfig primaryConfig,
            final OsakanaKey osakanaKey,
            final MiniMessage miniMessage,
            final List<ResourceBundle> bundles
    ) {
        final MiniMessageTranslationStore translationStore = MiniMessageTranslationStore.create(osakanaKey.create("message"), miniMessage);
        translationStore.defaultLocale(primaryConfig.defaultLocale());

        bundles.forEach(bundle -> translationStore.registerAll(bundle.getLocale(), bundle, false));
        final String localeNames = bundles.stream()
                .map(ResourceBundle::getLocale)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        logger.info("Loaded {} translations: [{}]", bundles.size(), localeNames);

        return translationStore;
    }

    @Override
    protected void configure() {
        this.bind(MiniMessage.class).toInstance(this.miniMessage());
        this.bind(MessageService.class).toInstance(this.messageService());
    }

    private MiniMessage miniMessage() {
        return MiniMessage.builder()
                .tags(TagResolver.standard())
                .tags(TagResolver.builder()
                        .tag("error", Tag.styling(builder -> builder.color(TextColor.color(0xFF4B00))))
                        .tag("warn", Tag.styling(builder -> builder.color(TextColor.color(0xF6AA00))))
                        .tag("info", Tag.styling(builder -> builder.color(TextColor.color(0x00B06B))))
                        .tag("debug", Tag.styling(builder -> builder.color(TextColor.color(0x00B06B))))
                        .build())
                .build();
    }

    private MessageService messageService() {
        return DoburokuStandard.of(MessageService.class)
                .argument(registry -> { }, MiniMessageArgumentTransformer.create())
                .result(registry -> registry
                        .plus(Message.class, (__, component) -> audience -> {
                            final List<ComponentLike> arguments = new ArrayList<>(component.arguments());
                            arguments.add(Argument.tagResolver(MiniPlaceholdersExpansion.placeholders()));
                            arguments.add(Argument.target(audience));

                            final TranslatableComponent result = Component.translatable(component.key(), arguments);

                            audience.sendMessage(result);
                        }))
                .brew();
    }
}
