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
package com.github.namiuni.osakana.common.angler.repository;

import com.github.namiuni.osakana.api.angler.AnglerProperty;
import com.github.namiuni.osakana.common.DataDirectory;
import com.github.namiuni.osakana.common.angler.repository.serializer.UUIDSerializer;
import com.github.namiuni.osakana.common.util.MoreFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class JsonUserRepository implements UserRepository {

    private final Path usersDirectory;

    private final Gson gsonSerializer;

    @Inject
    private JsonUserRepository(
        final @DataDirectory Path dataDirectory,
        final UUIDSerializer uuidSerializer
    ) throws IOException {
        this.usersDirectory = dataDirectory.resolve("users");
        MoreFiles.createDirectoriesIfNotExists(this.usersDirectory);

        this.gsonSerializer = new GsonBuilder()
            .registerTypeAdapter(UUID.class, uuidSerializer)
            .setPrettyPrinting()
            .create();

        MoreFiles.createDirectoriesIfNotExists(this.usersDirectory);
    }

    @Override
    public Optional<AnglerProperty> find(final UUID uuid) {
        final Path anglerFile = this.anglerFile(uuid);

        // If the file does not exist, return empty
        if (!Files.exists(anglerFile)) {
            return Optional.empty();
        }

        try (final Reader reader = Files.newBufferedReader(anglerFile)) {
            final AnglerProperty property = this.gsonSerializer.fromJson(reader, AnglerProperty.class);
            Objects.requireNonNull(property, "Angler file found but empty: '%s'".formatted(anglerFile));

            return Optional.of(property);
        } catch (final IOException e) {
            throw new UncheckedIOException("Exception while reading to file: '%s'".formatted(anglerFile), e);
        }
    }

    @Override
    public void save(final AnglerProperty angler) {
        final Path anglerFile = this.anglerFile(angler.uuid());
        try (final Writer writer = Files.newBufferedWriter(anglerFile)) {
            this.gsonSerializer.toJson(angler, writer);
        } catch (final IOException e) {
            throw new UncheckedIOException("Exception while saving to file: '%s'".formatted(anglerFile), e);
        }
    }

    private Path anglerFile(final UUID id) {
        return this.usersDirectory.resolve(id + ".json");
    }
}
