package io.github.namiuni.osakana.user.database.repository;

import io.github.namiuni.osakana.user.OsakanaUserImpl;
import io.github.namiuni.osakana.user.database.OsakanaUserMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RegisterRowMapper(OsakanaUserMapper.class)
public interface MySQLUserRepository extends UserRepository {

    @Override
    @SqlUpdate("""
            INSERT INTO osakana_users (id, name)
            VALUES (:uuid, :name)
            ON DUPLICATE KEY UPDATE name = :name
            """)
    void save(@BindMethods OsakanaUserImpl user);
}
