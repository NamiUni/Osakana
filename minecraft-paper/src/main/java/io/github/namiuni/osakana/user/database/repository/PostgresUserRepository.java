package io.github.namiuni.osakana.user.database.repository;


import io.github.namiuni.osakana.user.OsakanaUserImpl;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PostgresUserRepository extends UserRepository {

    @Override
    @SqlUpdate("""
            INSERT INTO osakana_users (id, name)
            VALUES (:id, :name)
            ON CONFLICT (id) DO UPDATE SET name = :name
            """)
    void save(@BindBean OsakanaUserImpl user);
}
