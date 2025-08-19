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

import io.github.namiuni.osakana.integration.MiniPlaceholdersExpansion;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MessageService {

    private static final Function<Audience, TagResolver> MINI_PLACEHOLDERS_TAG = MiniPlaceholdersExpansion::getAudiencePlaceholders;
    private static final Function<TranslatableComponent, Message> MESSAGE_FACTORY = component -> audience -> {
        final List<ComponentLike> argumentsList = new ArrayList<>(component.arguments());
        argumentsList.add(Argument.tagResolver(MINI_PLACEHOLDERS_TAG.apply(audience)));

        final ComponentLike[] arguments = argumentsList.toArray(ComponentLike[]::new);
        final TranslatableComponent result = Component.translatable(component.key(), arguments);

        audience.sendMessage(result);
    };

    private MessageService() {
    }
}
