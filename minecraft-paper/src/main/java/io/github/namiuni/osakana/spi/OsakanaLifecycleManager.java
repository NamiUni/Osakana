package io.github.namiuni.osakana.spi;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface OsakanaLifecycleManager {

    void startup();

    void shutdown();
}
