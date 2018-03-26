package br.com.edgarsilva.escenario.legacy.greenbox.metamodel;


/**
 * <p>Project : GreenBox </p>
 *
 * @author Edgar Silva
 * @version 1.0
 * @since 2002
 * <p>
 * The objective of this class is to read properties located in files,
 * to get some important informations to project.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class EnviromentProperties {
    private static EnviromentProperties instance;
    protected Properties properties = new Properties();


    private EnviromentProperties() {
        try {
            //intializing propeties
            properties.load(new FileInputStream("mapping.properties"));

        } catch (IOException e) {
            try {
                properties.load(EnviromentProperties.class.getResourceAsStream("/mapping/mapping.properties"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Pure Singleton
    public static EnviromentProperties getInstance() {
        return (instance == null) ? instance = new EnviromentProperties() : instance;
    }

    public String replaceDataType(String jdbcType) {
        return properties.getProperty(jdbcType);
    }

}