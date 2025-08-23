package io.github.namiuni.osakana.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.namiuni.osakana.api.user.OsakanaUser;
import io.github.namiuni.osakana.api.user.OsakanaUserService;
import io.github.namiuni.osakana.minecraft.paper.annotations.PluginName;
import io.github.namiuni.osakana.user.annotations.UserExecutor;
import io.github.namiuni.osakana.user.database.repository.UserRepository;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Singleton
@NullMarked
public final class OsakanaUserServiceImpl implements OsakanaUserService {

    public static final String USER_POOL = "%s UserServicePool-%s Thread #%%d";

    private final String pluginName;
    private final Cache<UUID, OsakanaUser> userCache;
    private final UserRepository userRepository;
    private final ExecutorService executorService;

    @Inject
    private OsakanaUserServiceImpl(
            final @PluginName String pluginName,
            final Cache<UUID, OsakanaUser> userCache,
            final UserRepository userRepository,
            final @UserExecutor ExecutorService executorService
    ) {
        this.pluginName = pluginName;
        this.userCache = userCache;
        this.userRepository = userRepository;

        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<@Nullable OsakanaUser> loadUser(final UUID uuid) {
        // First, attempt to retrieve synchronously from the cache.
        final OsakanaUser cachedUser = this.userCache.getIfPresent(uuid);
        if (cachedUser != null) {
            return CompletableFuture.completedFuture(cachedUser);
        }

        // If not in cache, retrieve asynchronously from DB.
        return CompletableFuture.supplyAsync(() -> {
                    this.setThreadName(uuid);
                    return this.userRepository
                            .findById(uuid)
                            .map(user -> {
                                this.userCache.put(uuid, user);
                                return user;
                            })
                            .orElse(null);
                },
                this.executorService);
    }

    public CompletableFuture<Void> saveUser(final OsakanaUserImpl user) {
        this.userCache.put(user.uuid(), user);
        return CompletableFuture.runAsync(() -> {
            this.setThreadName(user.uuid());
            this.userRepository.save(user);
        }, this.executorService);
    }

    public CompletableFuture<OsakanaUser> handleUserLogin(final UUID uuid, final String name) {
        return this.loadUser(uuid)
                .thenCompose(user -> {
                    if (user != null) {
                        if (!user.name().equals(name)) {
                            final OsakanaUserImpl updatedUser = new OsakanaUserImpl(user.uuid(), name);
                            return this.saveUser(updatedUser).thenApply(v -> updatedUser);
                        }
                        return CompletableFuture.completedFuture(user);
                    } else {
                        // Create and save a new User object
                        final OsakanaUserImpl newUser = new OsakanaUserImpl(uuid, name);
                        return this.saveUser(newUser).thenApply(v -> newUser);
                    }
                });
    }

    private void setThreadName(final UUID uuid) {
        Thread.currentThread().setName(USER_POOL.formatted(this.pluginName, uuid));
    }
}
