package io.github.namiuni.osakana.configuration;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.github.namiuni.osakana.configuration.annotations.ConfigFile;
import io.github.namiuni.osakana.configuration.configurations.PrimaryConfig;
import io.github.namiuni.osakana.spi.OsakanaLifecycleManager;
import java.io.UncheckedIOException;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@NullMarked
public final class ConfigurationLifecycleManager implements OsakanaLifecycleManager {

    private final ComponentLogger logger;
    private final Provider<PrimaryConfig> primaryConfigProvider;
    private final ConfigurationLoader<?> loader;
    private final ConfigurationNode node;
    private final ConfigurationTransformation.Versioned version;

    @Inject
    private ConfigurationLifecycleManager(
            final ComponentLogger logger,
            final Provider<PrimaryConfig> primaryConfigProvider,
            final ConfigurationLoader<?> loader,
            final ConfigurationNode node,
            final ConfigurationTransformation.Versioned version
    ) {
        this.logger = logger;
        this.primaryConfigProvider = primaryConfigProvider;
        this.loader = loader;
        this.node = node;
        this.version = version;
    }

    @Override
    public void startup() {
        try {

            // Load primary configuration
            this.primaryConfigProvider.get();
            this.logger.info("Loaded configuration: {}", PrimaryConfig.class.getAnnotation(ConfigFile.class).value());

            // Update node
            if (!this.node.virtual()) {
                final int startVersion = this.version.version(this.node);
                this.version.apply(this.node);
                final int endVersion = this.version.version(this.node);
                this.logger.info("Updated config schema from {} to {}", startVersion, endVersion);
            }

            // Save node
            this.loader.save(this.node);

        } catch (final ConfigurateException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @Override
    public void shutdown() {
        // none
    }
}
