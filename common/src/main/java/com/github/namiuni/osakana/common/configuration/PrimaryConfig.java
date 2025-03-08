/*
 * Osakana
 *
 * Copyright (c) 2025. Namiu (Unitarou)
 *                     Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.namiuni.osakana.common.configuration;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import java.nio.file.Path;

@NullMarked
@ConfigSerializable
public final class PrimaryConfig {

    @Comment("default/OsakanaPack")
    private @Nullable Path fishPack = null; // TODO: per world pack

    private RepositoryType repositoryType = RepositoryType.JSON;

    public @Nullable Path fishPack() {
        return this.fishPack;
    }

    public RepositoryType repositoryType() {
        return this.repositoryType;
    }

    public enum RepositoryType {
        JSON,
        PDC,
        MYSQL,
        PSQL,
        H2
    }
}
