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
package com.github.namiuni.osakana.common.configuration;

import java.io.Serial;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;

@NullMarked
@SuppressWarnings("unused")
public final class UncheckedConfigurateException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2214743499979182898L;

    public UncheckedConfigurateException() {
        super();
    }

    public UncheckedConfigurateException(final String message) {
        super(message);
    }

    public UncheckedConfigurateException(final ConfigurateException cause) {
        super(cause);
    }

    public UncheckedConfigurateException(final String message, final ConfigurateException cause) {
        super(message, cause);
    }

    public UncheckedConfigurateException(final String message, final ConfigurateException cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
