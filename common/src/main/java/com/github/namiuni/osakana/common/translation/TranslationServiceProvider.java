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
package com.github.namiuni.osakana.common.translation;

import com.github.namiuni.osakana.common.translation.placeholders.ComponentPlaceholderResolver;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.Moonshine;
import net.kyori.moonshine.exception.scan.UnscannableMethodException;
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy;
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslationServiceProvider implements Provider<TranslationService> {

    private final TranslationReceiverResolver translationReceiverResolver;
    private final TranslationManager translationManager;
    private final TranslationRenderer translationRenderer;
    private final ComponentPlaceholderResolver componentPlaceholderResolver;

    @Inject
    private TranslationServiceProvider(
        final TranslationReceiverResolver translationReceiverResolver,
        final TranslationManager translationManager,
        final TranslationRenderer translationRenderer,
        final ComponentPlaceholderResolver componentPlaceholderResolver
    ) {
        this.translationReceiverResolver = translationReceiverResolver;
        this.translationManager = translationManager;
        this.translationRenderer = translationRenderer;
        this.componentPlaceholderResolver = componentPlaceholderResolver;
    }

    @Override
    public TranslationService get() {
        try {
            return Moonshine.<TranslationService, Audience>builder(new TypeToken<>() {})
                .receiverLocatorResolver(this.translationReceiverResolver, 0)
                .sourced(this.translationManager)
                .rendered(this.translationRenderer)
                .sent(Audience::sendMessage)
                .resolvingWithStrategy(new StandardPlaceholderResolverStrategy<>(new StandardSupertypeThenInterfaceSupertypeStrategy(false)))
                .weightedPlaceholderResolver(Component.class, this.componentPlaceholderResolver, 0)
                .create(this.getClass().getClassLoader());
        } catch (final UnscannableMethodException exception) {
            throw new IllegalStateException("Failed to create MessageService instance", exception);
        }
    }
}
