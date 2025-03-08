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
package com.github.namiuni.osakana.common.angler;

import com.github.namiuni.osakana.api.angler.Angler;
import com.github.namiuni.osakana.api.angler.AnglerProperty;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AnglerImpl<T extends Audience> implements Angler<T> {

    private final T player;
    private final AnglerProperty property;

    public AnglerImpl(final T player, final AnglerProperty property) {
        this.player = player;
        this.property = property;
    }

    @Override
    public @NotNull T player() {
        return this.player;
    }

    @Override
    public UUID uuid() {
        return this.property.uuid();
    }

    @Override
    public boolean testBool() {
        return this.property.testBool();
    }

    @Override
    public @NotNull Audience audience() {
        return player;
    }
}
