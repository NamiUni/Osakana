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
package com.github.namiuni.osakana.common.translation;

import com.github.namiuni.osakana.common.DataDirectory;
import com.github.namiuni.osakana.common.PluginSource;
import com.github.namiuni.osakana.common.util.MoreFiles;
import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

// TODO crowdinから取得
@NullMarked
final class TranslationRepository {

    private final Path pluginSource;
    private final Path repositoryTranslationsDirectory;

    @Inject
    private TranslationRepository(
            final @PluginSource Path pluginSource,
            final @DataDirectory Path dataDirectory
    ) {
        this.pluginSource = pluginSource;
        final Path translationRepository = dataDirectory.resolve("translations");
        this.repositoryTranslationsDirectory = translationRepository.resolve("repository");
    }

    public void copyResourcesToDataDirectory() {
        try {
            MoreFiles.createDirectoriesIfNotExists(this.repositoryTranslationsDirectory);
            MoreFiles.walkAsDirectory(this.pluginSource, pathStream -> pathStream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().startsWith("/translations/messages_"))
                    .filter(path -> path.toString().endsWith(".properties"))
                    .forEach(this::copyFile));
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to copy resources", exception);
        }
    }

    private void copyFile(final Path sourceFile) {
        final Path targetPath = this.repositoryTranslationsDirectory.resolve(sourceFile.getFileName().toString());
        try (final InputStream inputStream = Files.newInputStream(sourceFile);
             final OutputStream outputStream = Files.newOutputStream(targetPath)) {
            inputStream.transferTo(outputStream);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to copy file: %s".formatted(sourceFile), exception);
        }
    }
}
