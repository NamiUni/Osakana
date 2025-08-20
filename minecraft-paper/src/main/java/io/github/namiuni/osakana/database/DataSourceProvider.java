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
package io.github.namiuni.osakana.database;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.namiuni.osakana.config.PrimaryConfig;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DataSourceProvider implements Provider<DataSource> {

    private final PrimaryConfig primaryConfig;

    @Inject
    private DataSourceProvider(final PrimaryConfig primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    @Override
    public DataSource get() {
        final HikariConfig config = new HikariConfig();
        final PrimaryConfig.DatabaseSettings databaseSettings = this.primaryConfig.database();
        config.setJdbcUrl(databaseSettings.url());
        config.setUsername(databaseSettings.username());
        config.setPassword(databaseSettings.password());

        final DataSource dataSource = new HikariDataSource(config);

        Flyway.configure(DataSourceProvider.class.getClassLoader())
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .dataSource(dataSource)
                // TODO: locations()
                .validateMigrationNaming(true)
                .validateOnMigrate(true)
                .load();

        return new HikariDataSource(config);
    }
}
