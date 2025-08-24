package io.github.namiuni.osakana.user.database;

import io.github.namiuni.osakana.user.OsakanaUserImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OsakanaUserMapper implements RowMapper<OsakanaUserImpl> {

    @Override
    public OsakanaUserImpl map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        final UUID id = UUID.fromString(rs.getString("id"));
        final String name = rs.getString("name");
        return new OsakanaUserImpl(id, name);
    }
}
