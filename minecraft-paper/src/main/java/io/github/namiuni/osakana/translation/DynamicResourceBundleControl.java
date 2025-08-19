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
import io.github.namiuni.osakana.minecraft.paper.module.annotations.DataDirectory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DynamicResourceBundleControl extends ResourceBundle.Control {

    private final Path dataDirectory;

    @Inject
    private DynamicResourceBundleControl(final @DataDirectory Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    public @Nullable ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IOException {
        final String resourceFileName = this.toResourceName(this.toBundleName(baseName, locale), "properties");

        final Path filePath = this.dataDirectory.resolve(resourceFileName);
        if (Files.exists(filePath)) {
            return new PropertyResourceBundle(Files.newBufferedReader(filePath, StandardCharsets.UTF_8));
        }

        try (InputStream inputStream = loader.getResourceAsStream(resourceFileName)) {
            if (inputStream == null) {
                return null;
            }

            final byte[] content = inputStream.readAllBytes();
            this.install(content, filePath);
            return new PropertyResourceBundle(new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8));
        }
    }

    private void install(final byte[] content, final Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content);
    }
}
