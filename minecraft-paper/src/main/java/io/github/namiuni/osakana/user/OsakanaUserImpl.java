package io.github.namiuni.osakana.user;

import io.github.namiuni.osakana.api.user.OsakanaUser;
import java.util.UUID;
import net.kyori.adventure.identity.Identity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record OsakanaUserImpl(UUID uuid, String name) implements OsakanaUser {

    @Override
    public Identity identity() {
        return Identity.identity(this.uuid);
    }
}
