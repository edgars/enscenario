package br.com.edgarsilva.escenario;

import br.com.edgarsilva.escenario.legacy.greenbox.Generator;
import org.beryx.textio.TerminalProperties;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;


import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        TextIO textIO = TextIoFactory.getTextIO();
        TerminalProperties<?> props =  textIO.getTextTerminal().getProperties();

        props.setPromptColor("orange");

        textIO.getTextTerminal().println("\n" +
                "$$$$$$$$$$$$$$$$$$$$$$$$$\n" +
                "$$$$$$$$$$$$$$$$$$$$$$$$$\n" +
                "$$$$$'`$$$$$$$$$$$$$'`$$$\n" +
                "$$$$$$  $$$$$$$$$$$  $$$$    Escenario is a Microservices Generator for your data!\n" +
                "$$$$$$$  '$/ `/ `$' .$$$$    Powered by Ballerina + Docker\n" +
                "$$$$$$$$. i  i  /! .$$$$$    version beta-0.1\n" +
                "$$$$$$$$$.--'--'   $$$$$$\n" +
                "$$^^$$$$$'        J$$$$$$\n" +
                "$$$   ~\"\"   `.   .$$$$$$$\n" +
                "$$$$$e,      ;  .$$$$$$$$\n" +
                "$$$$$$$$$$$.'   $$$$$$$$$\n" +
                "$$$$$$$$$$$$.    $$$$$$$$\n" +
                "$$$$$$$$$$$$$     $byEAS$"

        );


        props.setPromptBold(true);
        props.setPromptUnderline(true);
        props.setPromptColor("cyan");

        props.setPromptUnderline(false);
        props.setPromptBold(false);
        props.setInputColor("blue");
        props.setInputItalic(true);


        String outputFolder =  textIO.newStringInputReader().read("Please, where would you like to generate the Ballerina Services?");


        String database = textIO.newStringInputReader()
                .withNumberedPossibleValues("mysql", "postgres", "h2")
                .read("Please, which is you database engine?");


        String host =  textIO.newStringInputReader().read("Please, enter with the database Host");
        //jdbc:mysql://localhost:3306/mybank_eascorp?useSSL=false


        Integer port =  textIO.newIntInputReader() .withMinVal(1).read("Please, specify the database port");

        String schema =  textIO.newStringInputReader().read("Please, enter with the database schema");

        String user =  textIO.newStringInputReader().read("Please, enter with the database USER");

        String pass = textIO.newStringInputReader()
                .withMinLength(3).withInputMasking(true)
                .read("Please, enter with the Password");


        Generator gen  = new Generator();
        gen.setDatabaseEngine(database);
        gen.setHost(host);
        gen.setPort(port);
        gen.setSchema(schema);
        gen.setUser(user);
        gen.setPass(pass);
        gen.setOutputFolder(outputFolder);

        List<String> tables = gen.getTables();

        tables.add("* - All Tables ");

        String table = textIO.newStringInputReader()
                .withNumberedPossibleValues(tables)
                .read("Select one of the tables for create the microservice");

        if (! table.startsWith("*")) {

            gen.generateSourcesFromTable(table);

        } else {

            tables.remove(tables.size()-1);

            for (String aTable:tables
                 ) {

                gen.generateSourcesFromTable(aTable);
            }
        }

        textIO.getTextTerminal().println("Microservice generated ! go to folder " + outputFolder + " and check the commands!");

    }

    public static String getPasswordConsole() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }

        char passwordArray[] = console.readPassword("Enter your secret password: ");
        return new String(passwordArray);

    }


}