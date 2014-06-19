package net.catchpole.sql;

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

import net.catchpole.dom.ModelDocument;
import net.catchpole.dom.ModelDocumentBuilder;
import net.catchpole.dom.builder.XsdBuilder;
import net.catchpole.dom.transform.XmlTransform;
import net.catchpole.model.Model;
import net.catchpole.sql.data.Row;
import net.catchpole.sql.mapping.ExcavationExport;
import net.catchpole.sql.mapping.SchemaExport;
import net.catchpole.sql.meta.Table;
import net.catchpole.sql.query.Excavation;
import net.catchpole.sql.query.Prospect;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;
import org.junit.Test;

public class CatchpoleAirlinesTest {
    public final static Database database = AirlineDatabase.create();
    private final Trace trace = Core.getTrace();

    @Test
    public void testDatabase_UpdateValue() throws Exception {
        Core.getTrace().info(database);

        Table table = database.getTable("schedule");
        Core.getTrace().info(table);

        Prospect prospect = new Prospect(table);
        prospect.addOrderColumn(table.getColumn("flight"));

        trace.info(database.getFamily().getSelectBuilder().createStatement(prospect));
        trace.info(database.getFamily().getInsertBuilder().createStatement(prospect));
        trace.info(database.getFamily().getUpdateBuilder().createStatement(prospect));

        Excavation excavation = new Excavation(prospect);

        Core.getTrace().info(excavation.getRowSet());

        Row row = excavation.getRowSet().iterator().next();
        // change status to LANDED
        row.set(7, "LANDED");

        excavation.update();

        Core.getTrace().info(excavation.getRowSet());
    }

    @Test
    public void testDatabase_ModelExtract() throws Exception {
        ModelDocumentBuilder modelDocumentBuilder = new ModelDocumentBuilder(new XsdBuilder());
        ModelDocument modelDocument = modelDocumentBuilder.newDocument(database);
        Core.getTrace().info(new XmlTransform().getXML(modelDocument));
    }

    @Test
    public void testDatabase_SchemaExport() throws Exception {
        SchemaExport schemaExport = new SchemaExport(database);
        printModel(schemaExport.getValueModel());
    }

    @Test
    public void testDatabase_ExcavationExport() throws Exception {
        Table table = database.getTable("reservation");

        Prospect prospect = new Prospect(table);
        prospect.addSelectColumn(table.getColumn("booking"));
        prospect.addSelectColumn(table.getColumn("flight"));
        prospect.addSelectColumn(table.getColumn("passenger"));
        prospect.addOrderColumn(table.getColumn("booking"));
        prospect.addSelectColumn(database.getTable("airline").getColumn("code"));


        SchemaExport schemaExport = new SchemaExport(prospect);
        printModel(schemaExport.getValueModel());

        Excavation excavation = new Excavation(prospect);

        ExcavationExport excavationExport = new ExcavationExport(excavation);
        printModel(excavationExport.getValueModel());
    }

    private void printModel(Model model) throws Exception {
        ModelDocumentBuilder modelDocumentBuilder = new ModelDocumentBuilder(new XsdBuilder());
        ModelDocument modelDocument = modelDocumentBuilder.newDocument(model);
        Core.getTrace().info(new XmlTransform().getXML(modelDocument));
    }

    public void testDatabase_Join_1Level() throws Exception {
        Table table = database.getTable("reservation");

        Prospect prospect = new Prospect(table);
        prospect.addSelectColumn(table.getColumn("booking"));
        prospect.addSelectColumn(table.getColumn("flight"));
        prospect.addSelectColumn(table.getColumn("passenger"));

        prospect.addOrderColumn(table.getColumn("booking"));

        prospect.addSelectColumn(database.getTable("airline").getColumn("code"));

        Excavation excavation = new Excavation(prospect);

        Core.getTrace().info(excavation.getRowSet());
    }
}

