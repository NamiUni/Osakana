package io.github.namiuni.osakana.minecraft.paper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.namiuni.osakana.configuration.ConfigurationLifecycleManager;
import io.github.namiuni.osakana.spi.OsakanaLifecycleManager;
import io.github.namiuni.osakana.translation.TranslationLifecycleManager;
import io.github.namiuni.osakana.user.UserServiceLifecycleManager;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class OsakanaServiceLifecycleManager implements OsakanaLifecycleManager {

    private final Provider<ConfigurationLifecycleManager> configuration;
    private final Provider<TranslationLifecycleManager> translation;
    private final Provider<UserServiceLifecycleManager> userService;

    @Inject
    private OsakanaServiceLifecycleManager(
            final Provider<ConfigurationLifecycleManager> configuration,
            final Provider<TranslationLifecycleManager> translation,
            final Provider<UserServiceLifecycleManager> userService
    ) {
        this.configuration = configuration;
        this.translation = translation;
        this.userService = userService;
    }

    @Override
    public void startup() {
        this.configuration.get().startup();
        this.translation.get().startup();
        this.userService.get().startup();
    }

    @Override
    public void shutdown() {
        this.userService.get().shutdown();
        this.translation.get().shutdown();
        this.configuration.get().shutdown();
    }
}
