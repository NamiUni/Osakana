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
package com.github.namiuni.osakana.paper.angler;

import com.github.namiuni.osakana.api.angler.AnglerProperty;
import com.github.namiuni.osakana.common.angler.repository.CookieStore;
import com.github.namiuni.osakana.common.util.ByteArraySerializer;
import com.github.namiuni.osakana.paper.util.OsakanaKey;
import com.google.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PaperCookieStore implements CookieStore<Player> {

    private static final String KEY_PREFIX = "angler.";
    private static final NamespacedKey TEST_BOOL_KEY = OsakanaKey.create(KEY_PREFIX + "test_bool");

    @Inject
    private PaperCookieStore() {

    }

    @Override
    public Optional<AnglerProperty> retrieveAll(final Player player) {
        final Map<NamespacedKey, CompletableFuture<? extends @Nullable Serializable>> futureCookies =
            Map.of(
                TEST_BOOL_KEY, this.retrieve(player, TEST_BOOL_KEY, Boolean.class)
            ); // TODO more..

        return CompletableFuture.allOf(futureCookies.values().toArray(CompletableFuture[]::new))
            .thenApplyAsync($$ -> {

                // Try to retrieve AnglerProperty from cookies
                final Map<NamespacedKey, Serializable> cookies = new HashMap<>();
                for (final var cookie : futureCookies.entrySet()) {
                    final var value = cookie.getValue().join();
                    if (value == null) {
                        return Optional.<AnglerProperty>empty(); // Return if any value is null
                    }
                    cookies.put(cookie.getKey(), value);
                }

                // Parse AnglerProperty
                final AnglerProperty property = new AnglerProperty(
                    player.getUniqueId(),
                    ((Boolean) cookies.get(TEST_BOOL_KEY))); //fixme

                return Optional.of(property);
            })
            .join();
    }

    private CompletableFuture<@Nullable Serializable> retrieve(final Player player, final NamespacedKey key, final Class<? extends Serializable> type) {
        // Serialize a bytearray into a Serializable object and return it.
        return player.retrieveCookie(key)
            .thenApply(bytes -> bytes == null
                ? null
                : ByteArraySerializer.deserializeFromBytes(bytes, type));
    }

    @Override
    public void storeAll(final AnglerProperty property) {
        Map.of(
            TEST_BOOL_KEY, property.testBool()
            // TODO more
        ).forEach((key, value) -> this.store(Objects.requireNonNull(Bukkit.getPlayer(property.uuid())), key, value));  //FIXME
    }

    private void store(final Player player, final NamespacedKey key, final Serializable value) {
        final byte[] serialized = ByteArraySerializer.serializeToBytes(value);
        player.storeCookie(key, serialized);
    }
}
