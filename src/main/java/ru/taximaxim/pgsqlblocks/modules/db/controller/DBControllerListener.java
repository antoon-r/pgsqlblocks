/*
 * ========================LICENSE_START=================================
 * pgSqlBlocks
 * %
 * Copyright (C) 2017 "Technology" LLC
 * %
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package ru.taximaxim.pgsqlblocks.modules.db.controller;

import ru.taximaxim.pgsqlblocks.modules.db.model.DBStatus;

import java.sql.SQLException;

public interface DBControllerListener {

    //сигнал о том, что у контроллера изменился статус (fe перерисовать таблицу)
    void dbControllerStatusChanged(DBController controller, DBStatus newStatus);

    //сигнал, что контроллер был подключен и с ним можно работать
    void dbControllerDidConnect(DBController controller);

    //подключение
    void dbControllerWillConnect(DBController controller);

    void dbControllerConnectionFailed(DBController controller, SQLException exception);

    void dbControllerDisconnectFailed(DBController controller, boolean forcedByUser, SQLException exception);

    //сигнал об отключении
    void dbControllerDidDisconnect(DBController controller, boolean forcedByUser);

    //сигнал, о том, что началось обновление
    void dbControllerWillUpdateProcesses(DBController controller);

    //процессы уже обновились и их нужно отрисовать
    void dbControllerProcessesUpdated(DBController controller);

    void dbControllerBlockedChanged(DBController controller);

    void dbControllerProcessesFilterChanged(DBController controller);

    //журнал блокировок изменился
    void dbControllerBlocksJournalChanged(DBController controller);

}
