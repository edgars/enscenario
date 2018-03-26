package br.com.edgarsilva.escenario.legacy.greenbox;

import br.com.edgarsilva.escenario.legacy.greenbox.metamodel.Field;
import br.com.edgarsilva.escenario.legacy.greenbox.util.JDBCConnectionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {



    Map<String, Object> model = new HashMap<String,Object>();

    private String databaseEngine;
    private String url;
    private String user;
    private String pass;
    private String realDriverClass;


    public String getDatabaseEngine() {
        return databaseEngine;
    }

    public void setDatabaseEngine(String databaseEngine) {



        this.databaseEngine = databaseEngine;


        if (databaseEngine.equalsIgnoreCase("mysql")) {

            realDriverClass="com.mysql.jdbc.Driver";
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public List<String> getTables() {

        return JDBCConnectionManager.getInstance().getTableNames(realDriverClass, getUrl(), getUser(), getPass());


    }

    public List<Field> getFields(String table) {

        return JDBCConnectionManager.getInstance().getFileds(JDBCConnectionManager.getConnection(), table );

    }
}
