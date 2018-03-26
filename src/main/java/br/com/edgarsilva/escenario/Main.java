package br.com.edgarsilva.escenario;

import br.com.edgarsilva.escenario.legacy.greenbox.Generator;
import org.beryx.textio.TerminalProperties;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;


import java.io.Console;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // create a scanner so we can read the command-line input

        System.out.print("\n" +
                " ___ ___ _______ ___  ___ _____(_)__ \n" +
                "/ -_|_-</ __/ -_) _ \\/ _ `/ __/ / _ \\\n" +
                "\\__/___/\\__/\\__/_//_/\\_,_/_/ /_/\\___/\n" +
                "                                     "


        );

        System.out.println("\nEscenario is a MicroService data generator for Ballerina");

        TextIO textIO = TextIoFactory.getTextIO();

        TerminalProperties<?> props =  textIO.getTextTerminal().getProperties();

        props.setPromptBold(true);
        props.setPromptUnderline(true);
        props.setPromptColor("cyan");

        props.setPromptUnderline(false);
        props.setPromptBold(false);
        props.setInputColor("blue");
        props.setInputItalic(true);

        String database = textIO.newStringInputReader()
                .withNumberedPossibleValues("mysql", "postgres", "h2")
                .read("Please, which is you database engine?");

        Scanner scanner = new Scanner(System.in);


        // prompt for their age
        System.out.print("Database URL: ");
        //jdbc:mysql://localhost:3306/mybank_eascorp

        // get the age as an int
        String url = scanner.next();

        System.out.print("User : ");

        String user = scanner.next();

        System.out.print("Password: ");

        String pass = getPasswordConsole();

        Generator gen  = new Generator();
        gen.setDatabaseEngine(database);
        gen.setUrl(url);
        gen.setUser(user);
        gen.setPass(pass);

       System.out.println("Tables found in that connection :  "+ url + ":  " + gen.getTables());

        System.out.print("Select which table you want to generate the Ballerina Service, or * for all tables : ");

        String table = scanner.next();

        System.out.println("Fields : " + gen.getFields(table));

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