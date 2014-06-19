package net.catchpole.web.servlet;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import junit.framework.TestCase;
import net.catchpole.io.Arrays;
import net.catchpole.io.Files;
import net.catchpole.lang.Throw;
import net.catchpole.sql.Database;
import net.catchpole.sql.mapping.SchemaExport;
import net.catchpole.sql.meta.Table;
import net.catchpole.sql.query.Prospect;
import net.catchpole.web.server.JettyServer;
import org.junit.Test;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;

public class WebServiceServletTest {
    private static final Database database = AirlineDatabase.create();

    @Test
    public void testServer() throws Exception {
        Table table = database.getTable("reservation");

        Prospect prospect = new Prospect(table);
        prospect.addSelectColumn(table.getColumn("booking"));
        prospect.addSelectColumn(table.getColumn("flight"));
        prospect.addSelectColumn(table.getColumn("passenger"));
        prospect.addOrderColumn(table.getColumn("booking"));
        prospect.addSelectColumn(database.getTable("airline").getColumn("code"));

        SchemaExport schemaExport = new SchemaExport(prospect);

        JettyServer jettyServer = new JettyServer(9999, new WebServiceServlet(schemaExport.getValueModel()));
        try {
            URLConnection urlConnection = new URL("http://localhost:9999/").openConnection();
            TestCase.assertEquals(200, ((HttpURLConnection)urlConnection).getResponseCode());
            Arrays.spool(urlConnection.getInputStream(), System.out);
        } finally {
            jettyServer.stop();
        }
    }
}

class AirlineDatabase {
    private static int count = 0;

    public static Database create() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Database database = new Database(DriverManager.getConnection("jdbc:hsqldb:mem:Airline-" + count++, "sa", ""), "%");
            database.executeStatement(new String(Files.loadFile(new File("../atomicobjects-demo/src/main/resources/net/catchpole/sql/AirlinesTest.sql"))));
            database.reloadTables();
            return database;
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
