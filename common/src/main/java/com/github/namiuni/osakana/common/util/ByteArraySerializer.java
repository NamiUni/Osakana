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
package com.github.namiuni.osakana.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ByteArraySerializer {

    private ByteArraySerializer() {

    }

    public static <T extends Serializable> byte[] serializeToBytes(final T serializable) {
        try (final ByteArrayOutputStream bas = new ByteArrayOutputStream();
             final ObjectOutputStream oos = new ObjectOutputStream(bas)) {
            oos.writeObject(serializable);
            return bas.toByteArray();
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to serialization from '%s'".formatted(serializable), e);
        }
    }

    public static <T extends Serializable> T deserializeFromBytes(final byte[] data, final Class<T> type) {
        try (final ByteArrayInputStream bis = new ByteArrayInputStream(data);
             final ObjectInputStream ois = new ObjectInputStream(bis)) {
            final Object obj = ois.readObject();
            return type.cast(obj);
        } catch (final IOException | ClassNotFoundException e) {
            if (e instanceof IOException io) {
                throw new UncheckedIOException("Failed to deserialization from '%s'", io);
            }
            throw new IllegalStateException("Unexpected value: %s".formatted(type), e);
        }
    }
}
