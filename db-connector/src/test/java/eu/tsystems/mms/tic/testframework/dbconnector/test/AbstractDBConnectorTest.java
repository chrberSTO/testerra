/*
 * (C) Copyright T-Systems Multimedia Solutions GmbH 2018, ..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Peter Lehmann <p.lehmann@t-systems.com>
 *     pele <p.lehmann@t-systems.com>
 */
/* 
 * Created on 13.06.2012
 * 
 * Copyright(c) 2011 - 2012 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.dbconnector.test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

import eu.tsystems.mms.tic.testframework.dbconnector.test.connectors.TestTable;
import eu.tsystems.mms.tic.testframework.testing.FennecTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;

import eu.tsystems.mms.tic.testframework.dbconnector.DBConnector;
import eu.tsystems.mms.tic.testframework.dbconnector.query.InsertQuery;
import eu.tsystems.mms.tic.testframework.dbconnector.query.TruncateQuery;
import eu.tsystems.mms.tic.testframework.dbconnector.test.connectors.DBEntry;
import eu.tsystems.mms.tic.testframework.dbconnector.test.connectors.TableDefinitions;
import eu.tsystems.mms.tic.testframework.dbconnector.test.connectors.TestDBConnections;
import org.testng.annotations.BeforeSuite;

/**
 * Abstract class for DBConnector Tests that provides a DBConnector object and fills the test db with some data, which
 * will be removed after the tests again.
 * 
 * @author sepr
 * 
 */
public abstract class AbstractDBConnectorTest extends FennecTest {

    /** Logger for all DBConnector Tests. */
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractDBConnectorTest.class);

    /** DB Entry Oject to be used by any testmethod. */
    public static final DBEntry USER1 = new DBEntry(
            "user01", "max", "muster", 20, new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()));
    /** DB Entry Oject to be used by any testmethod. */
    public static final DBEntry USER2 = new DBEntry(
            "user02", "hans", "meier", 60, new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()));
    /** DB Entry Oject to be used by any testmethod. */
    public static final DBEntry USER3 = new DBEntry(
            "user03", "paul", "schulze", 112, new Date(System.currentTimeMillis()), new Time(
                    System.currentTimeMillis()));

    /** ThreadLocal DBConnector Object. */
    private static ThreadLocal<DBConnector<?>> dbConnector = new ThreadLocal<DBConnector<?>>();

    /**
     * Before method fills the testdb with some values.
     * 
     * @throws SQLException Error while querying the db.
     */
    @BeforeSuite(alwaysRun = true)
    public void setup() throws SQLException {
        final TableDefinitions table = TableDefinitions.TESTTABLE;

        final DBConnector<?> con = getDBConnector();

        String type = "varchar(255)";
        String values = Arrays.stream(table.getClass().getDeclaredFields()).map(field -> field.getName() + " " + type).collect(Collectors.joining(","));

        try {
            con.query("CREATE TABLE " + table.getTableName() + " (" + values + ");");
        } catch (SQLException e) {
            LOGGER.info("Table not created", e);
        }

        con.query(new InsertQuery<TableDefinitions>(
                table, new String[] { TestTable.getUser(), TestTable.getFirstname(),
                        TestTable.getLastname(), TestTable.getAge(),
                        TestTable.getDate(),
                        TestTable.getCreationTime() },
                new String[] { USER1.getUser(), USER1.getFirstname(), USER1.getLastname(),
                        USER1.getAge() + "", USER1.getDate().toString(), USER1.getCreationTime().toString() }));
        con.query(new InsertQuery<TableDefinitions>(
                table, new String[] { TestTable.getUser(), TestTable.getFirstname(),
                        TestTable.getLastname(), TestTable.getAge(),
                        TestTable.getDate(),
                        TestTable.getCreationTime() },
                new String[] { USER2.getUser(), USER2.getFirstname(), USER2.getLastname(),
                        USER2.getAge() + "", USER2.getDate().toString(), USER2.getCreationTime().toString() }));
        con.query(new InsertQuery<TableDefinitions>(
                table, new String[] { TestTable.getUser(), TestTable.getFirstname(),
                        TestTable.getLastname(), TestTable.getAge(),
                        TestTable.getDate(),
                        TestTable.getCreationTime() },
                new String[] { USER3.getUser(), USER3.getFirstname(), USER3.getLastname(),
                        USER3.getAge() + "", USER3.getDate().toString(), USER3.getCreationTime().toString() }));
    }

    /**
     * Getter for the encapsulated ThreadLocal DBConnector object.
     * 
     * @return DBConnector object for test db.
     */
    protected DBConnector<?> getDBConnector() {
        if (dbConnector.get() == null) {
            dbConnector.set(TestDBConnections.H2RESULTS.getDbConnection());
        }
        return dbConnector.get();
    }

    /**
     * After method, that empties the test db.
     * 
     * @throws SQLException Error while querying the db.
     */
    @AfterSuite(alwaysRun = true)
    public void tearDown() throws SQLException {
        final DBConnector<?> conn = getDBConnector();
        final TableDefinitions table = TableDefinitions.TESTTABLE;
        conn.query(new TruncateQuery<TableDefinitions>(table));
        dbConnector.remove();
    }

    /**
     * Sends log messages of the result to the logger.
     * 
     * @param resAfter ArrayList containing the result of a db query.
     */
    protected void printTableToLog(final List<HashMap<String, String>> resAfter) {
        int i = 0;
        for (final HashMap<String, String> map : resAfter) {
            if (i == 0) {
                LOGGER.debug(map.keySet().toString().toUpperCase());
            }
            LOGGER.debug(map.values().toString());
            i = 1;
        }
    }
}