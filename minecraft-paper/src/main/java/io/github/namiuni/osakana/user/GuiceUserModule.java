package io.github.namiuni.osakana.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;
import io.github.namiuni.osakana.api.user.OsakanaUser;
import io.github.namiuni.osakana.configuration.configurations.DatabaseType;
import io.github.namiuni.osakana.configuration.configurations.PrimaryConfig;
import io.github.namiuni.osakana.user.annotations.UserExecutor;
import io.github.namiuni.osakana.user.database.GuiceDataSourceProvider;
import io.github.namiuni.osakana.user.database.repository.MySQLUserRepository;
import io.github.namiuni.osakana.user.database.repository.PostgresUserRepository;
import io.github.namiuni.osakana.user.database.repository.UserRepository;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public final class GuiceUserModule extends AbstractModule {

    @Provides
    @Singleton
    private DatabaseType databaseType(final PrimaryConfig primaryConfig) {
        final String[] scheme = primaryConfig.database().url().split(":");
        return switch (scheme[1]) {
            case "mysql" -> DatabaseType.MYSQL;
            case "mariadb" -> DatabaseType.MARIA;
            case "postgresql" -> DatabaseType.POSTGRESQL;
            case "h2" -> DatabaseType.H2;
            default -> throw new IllegalStateException("Unsupported database scheme: " + scheme[1]);
        };
    }

    @Provides
    @Singleton
    private Flyway flyway(final HikariDataSource dataSource, final DatabaseType databaseType) {
        final String location = switch (databaseType) {
            case H2 -> "queries/migrations/h2";
            case MYSQL, MARIA -> "queries/migrations/mysql";
            case POSTGRESQL -> "queries/migrations/postgresql";
        };

        return Flyway.configure(GuiceUserModule.class.getClassLoader())
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .dataSource(dataSource)
                .locations(location)
                .validateMigrationNaming(true)
                .validateOnMigrate(true)
                .load();
    }

    @Provides
    @Singleton
    private Jdbi jdbi(final HikariDataSource dataSource) {
        return Jdbi.create(dataSource);
    }

    @Provides
    @Singleton
    private Cache<UUID, OsakanaUser> userCache() {
        return Caffeine.newBuilder()
                .expireAfterAccess(Duration.of(1, ChronoUnit.HOURS)) // TODO: config
                .maximumSize(100) // TODO: config
                .build();
    }

    @Provides
    private UserRepository userRepository(final DatabaseType databaseType, final Jdbi jdbi) {
        return switch (databaseType) {
            case MYSQL, MARIA, H2 -> jdbi.onDemand(MySQLUserRepository.class);
            case POSTGRESQL -> jdbi.onDemand(PostgresUserRepository.class);
        };
    }

    @Override
    protected void configure() {
        this.bind(HikariDataSource.class).toProvider(GuiceDataSourceProvider.class).in(Scopes.SINGLETON);
        this.bind(ExecutorService.class).annotatedWith(UserExecutor.class).toInstance(Executors.newVirtualThreadPerTaskExecutor());
    }
}
