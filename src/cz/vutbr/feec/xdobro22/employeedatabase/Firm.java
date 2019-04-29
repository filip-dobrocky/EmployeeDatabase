package cz.vutbr.feec.xdobro22.employeedatabase;


import java.io.*;
import java.util.*;

public class Firm {
    private HashSet<Employee> employees;
    private boolean hasDirector;
    private int maxWorkload;

    private final int DEFAULT_MAX_WORKLOAD = 200;

    public Firm() {
        employees = new HashSet<>();
        hasDirector = false;
        maxWorkload = DEFAULT_MAX_WORKLOAD;
    }

    public Firm(File f) {
        employees = new HashSet<>();
        hasDirector = false;
        try {
            loadDatabase(f);
        } catch (FileNotFoundException e) {
            System.out.println("Súbor s databázou nebol nájdený");
            maxWorkload = DEFAULT_MAX_WORKLOAD;
        } catch (InvalidPropertiesFormatException e) {
            System.out.println("Súbor s databázou nemá správny formát");
            maxWorkload = DEFAULT_MAX_WORKLOAD;
        }
    }

    public void addEmployee(EmployeeType type, int id, String firstname, String surname) {
        Employee newEmployee = null;

        switch (type) {
            case ASSISTANT:
                newEmployee = new Assistant(id, firstname, surname);
                break;
            case TECHNICIAN:
                newEmployee = new Technician(id, firstname, surname);
                break;
            case DEVELOPER:
                newEmployee = new Developer(id, firstname, surname);
                break;
            case DIRECTOR:
                if (hasDirector) {
                    System.out.println("Firma môže mať len jedného riaditeľa");
                    return;
                }
                newEmployee = new Director(id, firstname, surname);
                hasDirector = true;
                break;
        }

        if (employees.add(newEmployee))
            System.out.println("Zamestnanec pridaný");
        else
            System.out.println("Zamestnanec s daným ID už existuje");
    }

    private void addEmployee(EmployeeType type, int id, String firstname, String surname, boolean ill,
                            int administrativeHours, int technicalHours, int developmentHours) {
        Employee newEmployee = null;

        switch (type) {
            case ASSISTANT:
                newEmployee = new Assistant(id, firstname, surname);
                ((Assistant)newEmployee).addAdministrativeHours(administrativeHours);
                break;
            case TECHNICIAN:
                newEmployee = new Technician(id, firstname, surname);
                ((Technician)newEmployee).addTechnicalHours(technicalHours);
                ((Technician)newEmployee).addAdministrativeHours(administrativeHours);
                break;
            case DEVELOPER:
                newEmployee = new Developer(id, firstname, surname);
                ((Developer)newEmployee).addDevelopmentHours(developmentHours);
                ((Developer)newEmployee).addTechnicalHours(technicalHours);
                break;
            case DIRECTOR:
                if (hasDirector) {
                    System.out.println("Firma môže mať len jedného riaditeľa");
                    return;
                }
                newEmployee = new Director(id, firstname, surname);
                ((Director)newEmployee).addAdministrativeHours(administrativeHours);
                ((Director)newEmployee).addTechnicalHours(technicalHours);
                ((Director)newEmployee).addDevelopmentHours(developmentHours);
                hasDirector = true;
                break;
        }

        newEmployee.setIll(ill);

        if (employees.add(newEmployee))
            System.out.println("Zamestnanec s daným ID už existuje");
    }
    
    public Employee getEmployee(int id) {
        Employee employee = null;
        for (Employee e : employees) {
            if (e.getId() == id) {
                employee = e;
                break;
            }
        }
        return employee;
    }

    public void removeEmployee(int id) {
        Employee employee = getEmployee(id);
        if (employee == null) {
            System.out.println("Zamestnanec neexistuje");
            return;
        }

        employees.remove(employee);

        if (employee.getType() == EmployeeType.DIRECTOR)
            hasDirector = false;

        reassignWork(employee);

        System.out.println("Zamestnanec prepustený");
    }

    public void makeIll(int id) {
        Employee employee = getEmployee(id);

        if (employee == null) {
            System.out.println("Zamestnanec neexistuje");
            return;
        }


        reassignWork(employee);

        employee.setIll(true);
        System.out.println("Zamestnanec ochorel");
    }

    public void heal(int id) {
        Employee employee = getEmployee(id);
        if (employee != null) {
            employee.setIll(false);
            System.out.println("Zamestnanec vyzdravel");
        } else {
            System.out.println("Zamestnanec neexistuje");
        }
    }

    public void assignWork(WorkType type, int hours) {
        List<Employee> employeeList = new ArrayList<>(employees);
        employeeList.sort(Comparator.comparingInt(Employee::getWage));

        for (Employee employee : employeeList) {
            if (type.getInterfaceType().isInstance(employee) && !employee.isIll()) {
                int hoursToAdd;
                if (employee.getWorkload() + hours <= maxWorkload || employee instanceof Director) {
                    hoursToAdd = hours;
                } else {
                    hoursToAdd = maxWorkload - employee.getWorkload();
                }

                switch (type) {
                    case ADMINISTRATIVE:
                        ((Administrative)employee).addAdministrativeHours(hoursToAdd);
                        break;
                    case TECHNICAL:
                        ((Technical)employee).addTechnicalHours(hoursToAdd);
                        break;
                    case DEVELOPMENT:
                        ((Developer)employee).addDevelopmentHours(hoursToAdd);
                        break;
                }

                if ((hours -= hoursToAdd) == 0)
                    break;
            }
        }

        if (hours > 0)
            System.out.println("Nedostatok zamestnancov. Zostalo " + hours + "h.");
    }

    public void cancelWork(WorkType type, int hours) {
        List<Employee> employeeList = new ArrayList<>(employees);
        employeeList.sort(Comparator.comparingInt(Employee::getWage).reversed());

        for (Employee employee : employeeList) {
            if (type.getInterfaceType().isInstance(employee)) {
                int hoursToRemove;
                if (employee.getWorkload() - hours >= 0)
                    hoursToRemove = hours;
                else
                    hoursToRemove = employee.getWorkload();

                switch (type) {
                    case ADMINISTRATIVE:
                        ((Administrative)employee).removeAdministrativeHours(hoursToRemove);
                        break;
                    case TECHNICAL:
                        ((Technical)employee).removeTechnicalHours(hoursToRemove);
                        break;
                    case DEVELOPMENT:
                        ((Developer)employee).removeDevelopmentHours(hoursToRemove);
                        break;
                }

                if ((hours -= hoursToRemove) == 0)
                    break;
            }
        }
    }

    public void activateEmployee(WorkType type, int id) {
        Employee employee = getEmployee(id);
        if (employee == null) {
            System.out.println("Zamestnanec neexistuje");
            return;
        }

        if (type.getInterfaceType().isInstance(employee)) {
            switch (type) {
                case ADMINISTRATIVE:
                    System.out.print("ID zamestnanca na výpis: ");
                    Scanner sc = new Scanner(System.in);
                    Employee employeeToPrint = null;
                    if (sc.hasNextInt())
                        employeeToPrint = getEmployee(sc.nextInt());
                    ((Administrative)employee).printEmployee(employeeToPrint);
                    break;
                case TECHNICAL:
                    int vowelCount = ((Technical)employee).getVowelCount();
                    System.out.format("Počet samohlások v mene zamestnanca %s je %d.%n", employee.getName(), vowelCount);
                    break;
                case DEVELOPMENT:
                    System.out.format("Meno zamestnanca %s je odzadu ", employee.getName());
                    ((Development)employee).printReversedName();
                    System.out.println();
                    break;
            }
        } else {
            System.out.println("Daný zamestnanec nemôže robiť zadanú prácu.");
        }
    }

    public void printEmployeeTypes() {
        int assistants = 0, technicians = 0, developers = 0;
        int assistantFreeHours = 0, technicianFreeHours = 0, developerFreeHours = 0;

        for (Employee employee : employees) {
            switch (employee.getType()) {
                case ASSISTANT:
                    assistants++;
                    assistantFreeHours += maxWorkload - employee.getWorkload();
                    break;
                case TECHNICIAN:
                    technicians++;
                    technicianFreeHours += maxWorkload - employee.getWorkload();
                    break;
                case DEVELOPER:
                    developers++;
                    developerFreeHours += maxWorkload - employee.getWorkload();
                    break;
            }
        }

        System.out.println("Asistentov je " + assistants + ", voľné úväzky: " + assistantFreeHours + "h.");
        System.out.println("Technikov je " + technicians + ", voľné úväzky: " + technicianFreeHours + "h.");
        System.out.println("Vývojárov je " + developers + ", voľné úväzky: " + developerFreeHours + "h.");
        System.out.println(hasDirector ? "Firma má riaditeľa." : "Firma nemá riaditeľa.");
        System.out.println("Maximálny úväzok je nastavený na " + maxWorkload + "h.");
    }


    public void printMonthlySpendings() {
        int spendings = 0;
        for (Employee employee: employees)
            spendings += employee.getMonthlySalary();
        System.out.println("Náklady na jeden mesiac činia " + spendings + "kč.");
    }

    public void printEmployees(boolean sortById) {
        List<Employee> employeeList = new ArrayList<>(employees);
        if (sortById)
            employeeList.sort(Comparator.comparingInt(Employee::getId));
        else
            employeeList.sort(Comparator.comparing(Employee::getSurname));
        for (Employee employee : employeeList) {
            StringBuilder hours = new StringBuilder();
            if (employee instanceof Administrative)
                hours.append(" Administratívna práca: ").append(((Administrative) employee).getAdministrativeHours()).append("h");
            if (employee instanceof Technical)
                hours.append(" Technická práca: ").append(((Technical) employee).getTechnicalHours()).append("h");
            if (employee instanceof Development)
                hours.append(" Vývojová práca: ").append(((Development) employee).getDevelopmentHours()).append("h");

            System.out.format("%d %s %s %s%s%n",
                    employee.getId(), employee.getSurname(), employee.getFirstname(), employee.getType().getString(), hours.toString());
        }
    }


    public int getMaxWorkload() {
        return maxWorkload;
    }

    public void setMaxWorkload(int maxWorkload) {
        this.maxWorkload = maxWorkload;
        reassignAllWork();
    }

    private void reassignWork(Employee employee) {
        if (employee instanceof Administrative)
            assignWork(WorkType.ADMINISTRATIVE, ((Administrative)employee).getAdministrativeHours());
        if (employee instanceof Technical)
            assignWork(WorkType.TECHNICAL, ((Technical)employee).getTechnicalHours());
        if (employee instanceof Development)
            assignWork(WorkType.DEVELOPMENT, ((Development)employee).getDevelopmentHours());
    }

    private void reassignAllWork() {
        int administrativeHours = 0, technicalHours = 0, developmentHours = 0;
        for (Employee employee : employees) {
            if (employee instanceof Administrative)
                administrativeHours += ((Administrative)employee).getAdministrativeHours();
            if (employee instanceof Technical)
                technicalHours += ((Technical)employee).getTechnicalHours();
            if (employee instanceof Development)
                developmentHours += ((Development)employee).getDevelopmentHours();

            employee.resetWorkload();
        }

        assignWork(WorkType.ADMINISTRATIVE, administrativeHours);
        assignWork(WorkType.TECHNICAL, technicalHours);
        assignWork(WorkType.DEVELOPMENT, developmentHours);
    }

    public void saveDatabase(File f) {
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(String.format("%d%n", maxWorkload));
            for (Employee employee : employees) {
                StringBuilder hours = new StringBuilder();
                if (employee instanceof Administrative)
                    hours.append(",adm,").append(((Administrative) employee).getAdministrativeHours());
                if (employee instanceof Technical)
                    hours.append(",tech,").append(((Technical) employee).getTechnicalHours());
                if (employee instanceof Development)
                    hours.append(",dev,").append(((Development) employee).getDevelopmentHours());

                fw.write(String.format("%s,%d,%s,%s,%b%s%n",
                        employee.getType().getString(), employee.getId(), employee.getFirstname(), employee.getSurname(), employee.isIll(), hours.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDatabase(File f) throws FileNotFoundException, InvalidPropertiesFormatException {
        Scanner sc = new Scanner(new FileInputStream(f));
        InvalidPropertiesFormatException invalidFileException = new InvalidPropertiesFormatException("Invalid database file format");

        try {
            maxWorkload = sc.nextInt();
            sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                String employeeType = lineScanner.next();
                int id = lineScanner.nextInt();
                String firstname = lineScanner.next();
                String surname = lineScanner.next();
                boolean ill = lineScanner.nextBoolean();
                int administrativeHours = 0, technicalHours = 0, developmentHours = 0;
                while (lineScanner.hasNext()) {
                    String workType = lineScanner.next();
                    switch (workType) {
                        case "adm":
                            administrativeHours = lineScanner.nextInt();
                            break;
                        case "tech":
                            technicalHours = lineScanner.nextInt();
                            break;
                        case "dev":
                            developmentHours = lineScanner.nextInt();
                            break;
                        default:
                            throw invalidFileException;
                    }
                }

                if (employeeType.equals(EmployeeType.ASSISTANT.getString()))
                    addEmployee(EmployeeType.ASSISTANT, id, firstname, surname, ill, administrativeHours, technicalHours, developmentHours);
                else if (employeeType.equals(EmployeeType.TECHNICIAN.getString()))
                    addEmployee(EmployeeType.TECHNICIAN, id, firstname, surname, ill, administrativeHours, technicalHours, developmentHours);
                else if (employeeType.equals(EmployeeType.DEVELOPER.getString()))
                    addEmployee(EmployeeType.DEVELOPER, id, firstname, surname, ill, administrativeHours, technicalHours, developmentHours);
                else if (employeeType.equals(EmployeeType.DIRECTOR.getString()))
                    addEmployee(EmployeeType.DIRECTOR, id, firstname, surname, ill, administrativeHours, technicalHours, developmentHours);
            }
        } catch (NoSuchElementException e) {
            throw invalidFileException;
        }
    }
}
