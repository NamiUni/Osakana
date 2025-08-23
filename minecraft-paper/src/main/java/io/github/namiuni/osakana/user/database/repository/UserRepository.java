package io.github.namiuni.osakana.user.database.repository;

import io.github.namiuni.osakana.user.OsakanaUserImpl;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface UserRepository {

    @SqlQuery("SELECT * FROM osakana_users WHERE id = :id")
    @RegisterBeanMapper(OsakanaUserImpl.class)
    Optional<OsakanaUserImpl> findById(@Bind("id") UUID uuid);

    void save(@BindBean OsakanaUserImpl user);
}
