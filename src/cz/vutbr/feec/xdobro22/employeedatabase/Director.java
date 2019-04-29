package cz.vutbr.feec.xdobro22.employeedatabase;

public class Director extends Developer implements Administrative, Technical, Development {
    protected int administrativeHours;

    public Director(int id, String firstname, String surname) {
        super(id, firstname, surname);
        wage = 350;
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
        workload = administrativeHours = technicalHours = developmentHours = 0;
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.DIRECTOR;
    }
}

