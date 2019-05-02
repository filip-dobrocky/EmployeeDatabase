package cz.vutbr.feec.xdobro22.employeedatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static IllegalArgumentException invalidInputException = new IllegalArgumentException("Nesprávny vstup");

    public static void main(String[] args) {
        Firm firm;
        if (args.length == 1)
            firm = new Firm(new File("./" + args[0]));
        else
            firm = new Firm();

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        boolean correctInput = true;

        System.out.print("Zadajte príkaz (h - pomoc, q - ukončiť):\n> ");
        while (running) {
            String command = sc.next().toLowerCase();

            try {
                switch (command) {
                    case "q":
                        running = false;
                        break;
                    case "h":
                        printHelp();
                        break;
                    case "hire":
                        addEmployee(firm, sc);
                        break;
                    case "assign":
                        changeWork(true, firm, sc);
                        break;
                    case "cancel":
                        changeWork(false, firm, sc);
                        break;
                    case "fire":
                        removeEmployee(firm, sc);
                        break;
                    case "ill":
                        makeIll(firm, sc);
                        break;
                    case "heal":
                        heal(firm, sc);
                        break;
                    case "max":
                        setMaxWorkload(firm, sc);
                        break;
                    case "do":
                        activateEmployee(firm, sc);
                        break;
                    case "print":
                        printAction(firm, sc);
                        break;
                    case "save":
                        saveDatabase(firm, sc);
                        break;
                    case "load":
                        firm = loadDatabase(sc);
                        break;
                    default:
                        throw invalidInputException;
                }
                correctInput = true;
                if (running)
                    System.out.print("> ");
            } catch (IllegalArgumentException e) {
                if (correctInput) {
                    System.out.println(e.getMessage());
                    System.out.print("> ");
                }
                correctInput = false;
            }
        }
    }

    private static void addEmployee(Firm firm, Scanner sc) throws IllegalArgumentException {
        String typeString, firstname, surname;
        int id;
        EmployeeType type;

        typeString = sc.next().toLowerCase();
        switch (typeString) {
            case "ass":
                type = EmployeeType.ASSISTANT;
                break;
            case "tech":
                type = EmployeeType.TECHNICIAN;
                break;
            case "dev":
                type = EmployeeType.DEVELOPER;
                break;
            case "dir":
                type = EmployeeType.DIRECTOR;
                break;
            default:
                throw invalidInputException;
        }

        firstname = sc.next();
        surname = sc.next();

        if (sc.hasNextInt())
            id = sc.nextInt();
        else
            throw invalidInputException;

        firm.addEmployee(type, id, firstname, surname);
    }

    private static void removeEmployee(Firm firm, Scanner sc) throws IllegalArgumentException {
        int id;
        if (sc.hasNextInt())
            id = sc.nextInt();
        else
            throw invalidInputException;
        firm.removeEmployee(id);
    }

    private static void makeIll(Firm firm, Scanner sc) throws IllegalArgumentException {
        int id;
        if (sc.hasNextInt())
            id = sc.nextInt();
        else
            throw invalidInputException;
        firm.makeIll(id);
    }

    private static void heal(Firm firm, Scanner sc) throws IllegalArgumentException {
        int id;
        if (sc.hasNextInt())
            id = sc.nextInt();
        else
            throw invalidInputException;
        firm.heal(id);
    }

    private static void setMaxWorkload(Firm firm, Scanner sc) throws IllegalArgumentException {
        int workload;
        if (sc.hasNextInt())
            workload = sc.nextInt();
        else
            throw invalidInputException;
        firm.setMaxWorkload(workload);
        System.out.println("Maximálny úväzok nastavený na " + workload + "h");
    }

    private static void activateEmployee(Firm firm, Scanner sc) throws IllegalArgumentException {
        int id;
        WorkType type = getWorkType(sc.next().toLowerCase());

        if (sc.hasNextInt())
            id = sc.nextInt();
        else
            throw invalidInputException;

        firm.activateEmployee(type, id);
    }

    private static void changeWork(boolean assign, Firm firm, Scanner sc) throws IllegalArgumentException {
        int hours;
        WorkType type = getWorkType(sc.next().toLowerCase());

        if (sc.hasNextInt())
            hours = sc.nextInt();
        else
            throw invalidInputException;

        if (assign)
            firm.assignWork(type, hours);
        else
            firm.cancelWork(type, hours);
    }

    private static WorkType getWorkType(String typeString) throws IllegalArgumentException {
        switch (typeString) {
            case "adm":
                return WorkType.ADMINISTRATIVE;
            case "tech":
                return WorkType.TECHNICAL;
            case "dev":
                return WorkType.DEVELOPMENT;
            default:
                throw invalidInputException;
        }
    }

    private static void printAction(Firm firm, Scanner sc) throws IllegalArgumentException {
        String action = sc.next().toLowerCase();

        switch (action) {
            case "emps":
                System.out.println("Zoradiť podľa ID/priezviska (i/p): ");
                String sortBy = sc.next();
                switch (sortBy) {
                    case "i":
                        firm.printEmployees(true);
                        break;
                    case "p":
                        firm.printEmployees(false);
                        break;
                    default:
                        throw invalidInputException;
                }
                break;
            case "spend":
                firm.printMonthlySpendings();
                break;
            case "pos":
                firm.printEmployeeTypes();
                break;
            default:
                throw invalidInputException;
        }
    }

    private static void printHelp() {
        File helpFile = new File("./manual.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(helpFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Chyba pri čítaní súboru manual.txt");
        }
    }

    private static void saveDatabase(Firm firm, Scanner sc) {
        String name = sc.next();
        firm.saveDatabase(new File("./" + name + ".csv"));
    }

    private static Firm loadDatabase(Scanner sc) {
        String name = sc.next();
        return new Firm(new File("./" + name + ".csv"));
    }
}
