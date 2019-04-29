package cz.vutbr.feec.xdobro22.employeedatabase;

public class Technician extends Assistant implements Administrative, Technical {
    protected int technicalHours;

    public Technician(int id, String firstname, String surname) {
        super(id, firstname, surname);
        wage = 200;
        technicalHours = 0;
    }

    @Override
    public int getVowelCount() {
        return Actions.getVowelCount(getName());
    }

    @Override
    public int getTechnicalHours() {
        return technicalHours;
    }

    @Override
    public void addTechnicalHours(int technicalHours) {
        this.technicalHours += technicalHours;
        this.workload += technicalHours;
    }

    @Override
    public void removeTechnicalHours(int technicalHours) {
        this.technicalHours -= technicalHours;
        this.workload -= technicalHours;
    }

    @Override
    public void resetWorkload() {
        workload = administrativeHours = technicalHours = 0;
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.TECHNICIAN;
    }
}
