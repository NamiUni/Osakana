package io.github.namiuni.osakana.user.database.repository;

import io.github.namiuni.osakana.user.OsakanaUserImpl;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MySQLUserRepository extends UserRepository {

    @Override
    @SqlUpdate("""
            INSERT INTO osakana_users (id, name)
            VALUES (:id, :name)
            ON DUPLICATE KEY UPDATE name = :name
            """)
    void save(OsakanaUserImpl user);
}
