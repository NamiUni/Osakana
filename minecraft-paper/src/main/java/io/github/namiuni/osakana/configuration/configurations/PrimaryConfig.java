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
package io.github.namiuni.osakana.configuration.configurations;

import io.github.namiuni.osakana.configuration.annotations.ConfigFile;
import io.github.namiuni.osakana.configuration.annotations.ConfigHeader;
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

            @Comment("""
                JDBC URL. e.g.:
                MySQL: jdbc:mysql://localhost:3306/osakana
                MariaDB: jdbc:mariadb://localhost:3306/osakana
                PostgreSQL: jdbc:postgresql://localhost:3306/osakana
                H2: jdbc:h2:{plugin_directory}/osakana-h2;MODE=MySQL
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
                    "jdbc:h2:{plugin_directory}/osakana-h2;MODE=MySQL",
                    "",
                    "")
    );
}
