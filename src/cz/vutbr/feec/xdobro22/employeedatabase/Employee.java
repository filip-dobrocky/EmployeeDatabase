package cz.vutbr.feec.xdobro22.employeedatabase;

public abstract class Employee implements Comparable<Employee> {
    protected int id;
    protected int wage;
    protected int workload;
    protected String firstname;
    protected String surname;
    protected boolean ill;

    public Employee(int id, String firstname, String surname) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        workload = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public String getName() {
        return firstname + " " + surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isIll() {
        return ill;
    }

    public void setIll(boolean ill) {
        if (ill)
            resetWorkload();
        this.ill = ill;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public int getMonthlySalary() {
        return workload == 0 ? 500 : workload * wage;
    }

    @Override
    public int compareTo(Employee e) {
        return Integer.compare(this.wage, e.wage);
    }

    @Override
    public int hashCode() {
        return id*17;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Employee)
            return this.id == ((Employee)obj).id;
        return false;
    }

    public abstract void resetWorkload();

    public abstract EmployeeType getType();
}
