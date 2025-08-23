package io.github.namiuni.osakana.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.github.namiuni.osakana.api.user.OsakanaUser;
import io.github.namiuni.osakana.configuration.configurations.DatabaseType;
import io.github.namiuni.osakana.spi.OsakanaLifecycleManager;
import io.github.namiuni.osakana.user.annotations.UserExecutor;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class UserServiceLifecycleManager implements OsakanaLifecycleManager {

    private final ComponentLogger logger;
    private final DatabaseType databaseType;
    private final Provider<Jdbi> jdbiProvider;
    private final Provider<HikariDataSource> dataSource;
    private final Provider<Flyway> flywayProvider;
    private final Cache<UUID, OsakanaUser> userCache;
    private final ExecutorService executorService;

    @Inject
    private UserServiceLifecycleManager(
            final ComponentLogger logger,
            final DatabaseType databaseType,
            final Provider<Jdbi> jdbi,
            final Provider<HikariDataSource> dataSource,
            final Provider<Flyway> flywayProvider,
            final Cache<UUID, OsakanaUser> userCache,
            final @UserExecutor ExecutorService executorService
    ) {
        this.logger = logger;
        this.databaseType = databaseType;
        this.jdbiProvider = jdbi;
        this.dataSource = dataSource;
        this.flywayProvider = flywayProvider;
        this.userCache = userCache;
        this.executorService = executorService;
    }

    @Override
    public void startup() {
        this.logger.info("Initializing user service...");

        // Migrate Flyway
        final Flyway flyway = this.flywayProvider.get();
        LogFactory.setLogCreator(OsakanaFlywayLog::new);
        flyway.repair();
        flyway.migrate();
        LogFactory.setLogCreator(null);

        // Initialize Jdbi
        final Jdbi jdbi = this.jdbiProvider.get();
        jdbi.installPlugin(new SqlObjectPlugin());
        switch (this.databaseType) {
            case H2 -> jdbi.installPlugin(new H2DatabasePlugin());
            case POSTGRESQL -> jdbi.installPlugin(new PostgresPlugin());
        }

        this.logger.info("User service initialization complete!");
    }

    @Override
    public void shutdown() {
        this.logger.info("Shutting down user service...");
        this.userCache.invalidateAll();
        this.dataSource.get().close();
        this.executorService.shutdown();
        try {
            if (!this.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                this.logger.warn("Executor did not terminate in the specified time.");
                this.executorService.shutdownNow();
            }
        } catch (final InterruptedException exception) {
            this.executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        this.logger.info("User service shutdown complete!");
    }

    private final class OsakanaFlywayLog implements Log {

        private final String format;

        private OsakanaFlywayLog(final Class<?> clazz) {
            this.format = "[%s] {}".formatted(clazz.getSimpleName());
        }

        @Override
        public boolean isDebugEnabled() {
            return true;
        }

        @Override
        public void debug(final String message) {
            UserServiceLifecycleManager.this.logger.debug(this.format, message);
        }

        @Override
        public void info(final String message) {
            UserServiceLifecycleManager.this.logger.info(this.format, message);
        }

        @Override
        public void warn(final String message) {
            UserServiceLifecycleManager.this.logger.warn(this.format, message);
        }

        @Override
        public void error(final String message) {
            UserServiceLifecycleManager.this.logger.error(this.format, message);
        }

        @Override
        public void error(final String message, final Exception exception) {
            UserServiceLifecycleManager.this.logger.error(this.format, message, exception);
        }

        @Override
        public void notice(final String message) {
            UserServiceLifecycleManager.this.logger.info(this.format, message);
        }
    }
}
