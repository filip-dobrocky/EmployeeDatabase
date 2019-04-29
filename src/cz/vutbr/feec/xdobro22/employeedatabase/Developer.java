package cz.vutbr.feec.xdobro22.employeedatabase;

public class Developer extends Employee implements Technical, Development {
    protected int technicalHours;
    protected int developmentHours;

    public Developer(int id, String firstname, String surname) {
        super(id, firstname, surname);
        wage = 250;
        technicalHours = developmentHours = 0;
    }

    @Override
    public int getVowelCount() {
        return Actions.getVowelCount(getName());
    }

    @Override
    public void printReversedName() {
        Actions.printReversed(getName());
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
    public int getDevelopmentHours() {
        return developmentHours;
    }

    @Override
    public void addDevelopmentHours(int developmentHours) {
        this.developmentHours += developmentHours;
        this.workload += developmentHours;
    }

    @Override
    public void removeDevelopmentHours(int developmentHours) {
        this.developmentHours -= developmentHours;
        this.workload -= developmentHours;
    }

    @Override
    public void resetWorkload() {
        workload = developmentHours = technicalHours = 0;
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.DEVELOPER;
    }
}
