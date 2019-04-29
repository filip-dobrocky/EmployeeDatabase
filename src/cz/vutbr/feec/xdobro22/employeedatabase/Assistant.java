package cz.vutbr.feec.xdobro22.employeedatabase;

public class Assistant extends Employee implements Administrative {
    protected int administrativeHours;

    public Assistant(int id, String firstname, String surname) {
        super(id, firstname, surname);
        wage = 150;
        administrativeHours = 0;
    }

    @Override
    public void printEmployee(Employee employee) {
        Actions.printEmployee(employee);
    }

    @Override
    public int getAdministrativeHours() {
        return administrativeHours;
    }

    @Override
    public void addAdministrativeHours(int administrativeHours) {
        this.administrativeHours += administrativeHours;
        this.workload += administrativeHours;
    }

    @Override
    public void removeAdministrativeHours(int administrativeHours) {
        this.administrativeHours -= administrativeHours;
        this.workload -= administrativeHours;
    }

    @Override
    public void resetWorkload() {
        workload = administrativeHours = 0;
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.ASSISTANT;
    }
}
