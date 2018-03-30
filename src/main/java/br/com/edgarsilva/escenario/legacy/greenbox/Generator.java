package br.com.edgarsilva.escenario.legacy.greenbox;

import br.com.edgarsilva.escenario.legacy.greenbox.metamodel.Field;
import br.com.edgarsilva.escenario.legacy.greenbox.util.JDBCConnectionManager;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Generator {

    private String databaseEngine;
    private String url;
    private String user;
    private String pass;
    private String realDriverClass;
    private String host;
    private Integer port;
    private String schema;
    private String outputFolder;


    VelocityEngine ve = new VelocityEngine();
    VelocityContext context = new VelocityContext();
    Map<String, Object> model = new HashMap<String, Object>();

    public Generator() {

        Properties props = new Properties();
        // THIS PATH CAN BE HARDCODED BUT IDEALLY YOUD GET IT FROM A PROPERTIES FILE
        String path = "/home/edgar/wso2/workspace/2018/toys/enscenario/src/main/resources/templates";
        props.put("file.resource.loader.path", path);
        props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");

        ve.init(props);

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }


    public String getDatabaseEngine() {
        return databaseEngine;
    }

    public void setDatabaseEngine(String databaseEngine) {


        this.databaseEngine = databaseEngine;


        if (databaseEngine.equalsIgnoreCase("mysql")) {

            realDriverClass = "com.mysql.jdbc.Driver";
        }
    }

    public String getUrl() {

        if (databaseEngine.equalsIgnoreCase("mysql")) {
            this.url = "jdbc:" + databaseEngine + "://" + host + ":" + port.toString() + "/" + schema + "?useSSL=false";
        } else {

            this.url = "jdbc:" + databaseEngine + "://" + host + ":" + port.toString() + "/" + schema;

        }


        return url;
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

        context.put("host", getHost());
        context.put("port", getPort());
        context.put("database", getSchema());
        context.put("user", getUser());
        context.put("pass", getPass());

        return JDBCConnectionManager.getInstance().getTableNames(realDriverClass, getUrl(), getUser(), getPass());


    }

    public List<Field> getFields(String table) {

        return JDBCConnectionManager.getInstance().getFileds(JDBCConnectionManager.getConnection(), table);

    }


    public void generateSourcesFromTable(String table) {


        context.put("table", table);
        context.put("fields", JDBCConnectionManager.getInstance().getFileds(JDBCConnectionManager.getConnection(), table));
        context.put("service", table.substring(0, 1).toUpperCase() + table.substring(1));



        if (! outputFolder.endsWith("/")) outputFolder+="/";

        createDirs(outputFolder+ table + "dataservice/");
        createDirs(outputFolder+ table + "dataservice/db/");


        String ballerinaResourceSourceLocation = outputFolder+ table + "dataservice/"+  table+"DataService.bal" ;
        String ballerinaDBLocation = outputFolder+ table + "dataservice/db/"+  table+"DbUtil.bal" ;

        System.out.println("File: " + ballerinaResourceSourceLocation);

        File fileBallerinaResource = new File(ballerinaResourceSourceLocation);
        File fileBallerinaDBUtil = new File(ballerinaDBLocation);

        try {
            fileBallerinaResource.createNewFile();

            Template template = ve.getTemplate("ballerina-resource.vm");

            StringWriter writer = new StringWriter();
            template.merge(context, writer);


            FileUtils.writeStringToFile(fileBallerinaResource, writer.toString(), (String) null);


            fileBallerinaDBUtil.createNewFile();

            template = ve.getTemplate("ballerina-db-util.vm");
            writer = new StringWriter();
            template.merge(context,writer);

            FileUtils.writeStringToFile(fileBallerinaDBUtil,writer.toString(), (String) null);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Template template = ve.getTemplate("ballerina-resource.vm");

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

    }




    public boolean createDirs(String structure){
        return new File(structure).mkdirs();
    }

    public boolean createFile(String file) throws IOException{
        try {
            return new File(file).createNewFile();
        }
        catch (IOException ex) {
            throw new IOException("Error creating the file .... "+ ex.getMessage());
        }
    }

    public void writeStringFile(String s, String file, String content) {

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter( file));
            writer.write( content);

        }
        catch ( IOException e)
        {
        }
        finally
        {
            try
            {
                if ( writer != null)
                    writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
    }
}
