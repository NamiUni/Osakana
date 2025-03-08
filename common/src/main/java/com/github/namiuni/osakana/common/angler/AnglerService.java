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
import com.github.namiuni.osakana.common.angler.repository.CookieStore;
import com.github.namiuni.osakana.common.angler.repository.JsonUserRepository;
import com.github.namiuni.osakana.common.angler.repository.UserRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class AnglerService<T extends Audience & Identified> {

    private final ComponentLogger logger;
    private final CookieStore<T> cookieStore;
    private final UserRepository repository;

    private final Map<UUID, CompletableFuture<AnglerProperty>> cache;
    private final ThreadFactory threadFactory;

    @Inject
    private AnglerService(
        final ComponentLogger logger,
        final CookieStore<T> cookieStore,
        final UserRepository repository
    ) {
        this.logger = logger;
        this.cookieStore = cookieStore;
        this.repository = repository;

        this.cache = new ConcurrentHashMap<>();
        this.threadFactory = Thread.ofVirtual()
            .name("Osakana")
            .uncaughtExceptionHandler((thread, throwable) -> this.logger.warn("Uncaught exception on thread {}", thread.getName(), throwable))
            .factory();
    }

    /*
     * =============================================================
     * ======================== Load logic =========================
     * =============================================================
     */

    public CompletableFuture<Angler<T>> load(final T player) {
        return this.cache.computeIfAbsent(player.identity().uuid(), uuid -> this.loadOrCreate(player))
            .thenApply(property -> new AnglerImpl<>(player, property)); // wrapped
    }

    private CompletableFuture<AnglerProperty> loadOrCreate(final T player) {
        // Load from cookie or repository
        final CompletableFuture<AnglerProperty> anglerFuture = CompletableFuture.supplyAsync(() -> {

            // Retrieve from Cookies
            final Optional<AnglerProperty> cookieProperty = this.loadFromCookie(player);
            if (cookieProperty.isPresent()) {
                this.logger.debug("Retrieve from cookies: {}", cookieProperty.get());
                return cookieProperty.get();
            }

            // Load from repository or create
            final AnglerProperty repositoryProperty = cookieProperty
                .or(() -> loadFromRepository(player.identity().uuid()))
                .orElseGet(() -> this.createAnglerProperty(player.identity().uuid()));
            this.logger.debug("Load from repository: {}", repositoryProperty);

            // Store cookies
            this.cookieStore.storeAll(repositoryProperty);

            return repositoryProperty;
        }, Executors.newThreadPerTaskExecutor(this.threadFactory));

        // Remove cache if failed
        anglerFuture.exceptionally(ex -> {
            this.cache.remove(player.identity().uuid());
            throw new CompletionException(ex);
        });

        return anglerFuture;
    }

    private Optional<AnglerProperty> loadFromCookie(final T player) {
        return this.cookieStore.retrieveAll(player);
    }

    private Optional<AnglerProperty> loadFromRepository(final UUID uuid) {
        return switch (this.repository) {
            case JsonUserRepository json -> json.find(uuid);
            // TODO more
        };
    }

    private AnglerProperty createAnglerProperty(final UUID uuid) {
        return new AnglerProperty(uuid, true); // TODO コンフィグ
    }

    /*
     * =============================================================
     * ======================== Save logic =========================
     * =============================================================
     */

    public void save(final T player) {
        if (this.cache.containsKey(player.identity().uuid())) {
            this.cache.get(player.identity().uuid())
                .thenAccept(this.repository::save);
        }
    }
}
