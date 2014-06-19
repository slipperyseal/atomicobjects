package net.catchpole.sql.connection;

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

import net.catchpole.lang.Throw;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionSource implements ConnectionSource {
    private String jndiName;
    private DataSource dataSource;

    public DataSourceConnectionSource(String jndiName) {
        this.jndiName = jndiName;

        try {
            // use reflection to avoid imposing J2EE imports at compiler time
            Class clazz = Class.forName("javax.naming.InitialContext");
            Object initialContext = clazz.newInstance();
            Method method = clazz.getMethod("lookup", String.class);
            this.dataSource = (javax.sql.DataSource) method.invoke(initialContext, jndiName);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("JNDI InitialContext class not available", cnfe);
        } catch (InvocationTargetException ite) {
            // unwrap any exception from initialContext.lookup()
            throw Throw.unchecked(ite.getCause());
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public String toString() {
        return this.getClass().getName() + ' ' + this.jndiName;
    }
}
