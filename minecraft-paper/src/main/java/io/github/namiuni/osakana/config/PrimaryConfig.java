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
package io.github.namiuni.osakana.config;

import io.github.namiuni.osakana.config.annotations.ConfigFile;
import io.github.namiuni.osakana.config.annotations.ConfigHeader;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@NullMarked
@ConfigFile("config.conf")
@ConfigHeader("""
        This is the primary configuration file for Osakana.
        Some options may impact gameplay, so use
        with caution, and make sure you know what each option does before configuring.
        """)
@ConfigSerializable
public record PrimaryConfig(
        @Comment("The default locale for plugin messages.")
        Locale defaultLocale,

        @Comment("Configures the database connection.")
        DatabaseSettings database
) {

    @ConfigSerializable
    public record DatabaseSettings(

            @Comment("The storage type for saving plugin information.")
            StorageType storageType,

            @Comment("""
                JDBC URL. Suggested defaults for each DB:
                MySQL: jdbc:mysql://host:3306/DB
                MariaDB: jdbc:mariadb://host:3306/DB
                PostgreSQL: jdbc:postgresql://host:5432/database
                """)
            String url,

            @Comment("The connection username.")
            String username,

            @Comment("The connection password.")
            String password
    ) {
    }

    public static final PrimaryConfig DEFAULT = new PrimaryConfig(
            Locale.US,
            new DatabaseSettings(
                    StorageType.MYSQL,
                    "jdbc:mysql://localhost:3306/osakana",
                    "username",
                    "password")
    );
}
