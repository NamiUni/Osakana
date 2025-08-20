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
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.standard.DoburokuStandard;
import io.github.namiuni.doburoku.standard.argument.MiniMessageArgumentTransformer;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ResourceBundle(baseName = "translations/messages")
public interface MessageService {

    final class Provider implements com.google.inject.Provider<MessageService> {

        @Inject
        private Provider() {
        }

        @Override
        public MessageService get() {
            return DoburokuStandard.of(MessageService.class)
                    .argument(registry -> { }, MiniMessageArgumentTransformer.create())
                    .brew();
        }
    }
}
