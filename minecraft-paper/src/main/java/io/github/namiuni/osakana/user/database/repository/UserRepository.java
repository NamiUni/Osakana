package io.github.namiuni.osakana.user.database.repository;

import io.github.namiuni.osakana.user.OsakanaUserImpl;
import io.github.namiuni.osakana.user.database.OsakanaUserMapper;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RegisterRowMapper(OsakanaUserMapper.class)
public interface UserRepository {

    @SqlQuery("SELECT id, name FROM osakana_users WHERE id = :uuid")
    Optional<OsakanaUserImpl> findById(@Bind UUID uuid);

    void save(@BindMethods OsakanaUserImpl user);
}
