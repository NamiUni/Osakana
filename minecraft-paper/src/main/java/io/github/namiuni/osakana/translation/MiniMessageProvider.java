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
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MiniMessageProvider implements Provider<MiniMessage> {

    @Inject
    private MiniMessageProvider() {
    }

    @Override
    public MiniMessage get() {
        // JIS Z 9103 https://ja.wikipedia.org/wiki/JIS%E5%AE%89%E5%85%A8%E8%89%B2
        return MiniMessage.builder()
                .tags(TagResolver.standard())
                .tags(TagResolver.builder()
                        .resolver(Placeholder.styling("error", TextColor.color(Integer.parseInt("ff4b00", 16))))
                        .resolver(Placeholder.styling("warn", TextColor.color(Integer.parseInt("f2e700", 16))))
                        .resolver(Placeholder.styling("info", TextColor.color(Integer.parseInt("00b06b", 16))))
                        .resolver(Placeholder.styling("debug", TextColor.color(Integer.parseInt("1971ff", 16))))
                        .build())
                .build();
    }
}
