/*
 * osakana
 *
 * Copyright (c) 2025. Namiu (うにたろう)
 *                     Contributors []
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
package io.github.namiuni.osakana.translation;

import io.github.namiuni.doburoku.annotation.Locales;
import io.github.namiuni.doburoku.annotation.annotations.Key;
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.annotation.annotations.Value;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ResourceBundle(baseName = "translations/messages")
public interface MessageService {

    @Key("osakana.kick.user_data.load.failed")
    @Value(locale = Locales.EN_US, content = "Failed to load user data.")
    @Value(locale = Locales.JA_JP, content = "ユーザーデータの読み込みに失敗しました。")
    Component kickUserDataLoadFailed();
}
