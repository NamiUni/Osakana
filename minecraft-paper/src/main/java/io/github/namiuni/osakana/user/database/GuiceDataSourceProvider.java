package io.github.namiuni.osakana.user.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.namiuni.osakana.configuration.configurations.DatabaseType;
import io.github.namiuni.osakana.configuration.configurations.PrimaryConfig;
import io.github.namiuni.osakana.minecraft.paper.annotations.DataDirectory;
import io.github.namiuni.osakana.minecraft.paper.annotations.PluginName;
import java.nio.file.Path;
import java.util.concurrent.ThreadFactory;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GuiceDataSourceProvider implements Provider<HikariDataSource> {

    private final ComponentLogger logger;
    private final PrimaryConfig primaryConfig;
    private final DatabaseType databaseType;
    private final Path dataDirectory;
    private final String pluginName;

    @Inject
    private GuiceDataSourceProvider(
            final ComponentLogger logger,
            final PrimaryConfig primaryConfig,
            final DatabaseType databaseType,
            final @DataDirectory Path dataDirectory,
            final @PluginName String pluginName
    ) {
        this.logger = logger;
        this.primaryConfig = primaryConfig;
        this.databaseType = databaseType;
        this.dataDirectory = dataDirectory;
        this.pluginName = pluginName;
    }

    @Override
    public HikariDataSource get() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.url());
        hikariConfig.setDriverClassName(this.driverClassName());
        hikariConfig.setUsername(this.primaryConfig.database().username());
        hikariConfig.setPassword(this.primaryConfig.database().password());
        hikariConfig.setPoolName("Osakana-HikariPool");
        hikariConfig.setThreadFactory(this.threadFactory());

        return new HikariDataSource(hikariConfig);
    }

    private String url() {
        return this.databaseType == DatabaseType.H2
                ? this.primaryConfig.database().url().replace("{plugin_directory}", this.dataDirectory.toAbsolutePath().toString())
                : this.primaryConfig.database().url();
    }

    private String driverClassName() {
        return switch (this.databaseType) {
            case MYSQL -> com.mysql.jdbc.Driver.class.getName();
            case MARIA -> org.mariadb.jdbc.Driver.class.getName();
            case POSTGRESQL -> org.postgresql.Driver.class.getName();
            case H2 -> org.h2.Driver.class.getName();
        };
    }

    private ThreadFactory threadFactory() {
        return new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("%s %s Thread #%%d".formatted(this.pluginName, "HikariPool"))
                .setUncaughtExceptionHandler((thread, throwable) -> this.logger.warn("Uncaught exception on thread {}", thread.getName(), throwable))
                .build();
    }
}
