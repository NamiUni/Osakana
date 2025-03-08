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
package com.github.namiuni.osakana.api;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class OsakanaProvider {

    private static @Nullable Osakana osakana;

    private OsakanaProvider() {

    }

    @ApiStatus.Internal
    public static void register(final Osakana osakana) {
        OsakanaProvider.osakana = osakana;
    }

    public static Osakana osakana() {
        if (OsakanaProvider.osakana == null) {
            throw new NullPointerException("Osakana not Initialized");
        } else {
            return OsakanaProvider.osakana;
        }
    }
}
