package cz.vutbr.feec.xdobro22.employeedatabase;

public interface Administrative {
    void printEmployee(Employee employee);
    int getAdministrativeHours();
    void addAdministrativeHours(int administrativeHours);
    void removeAdministrativeHours(int administrativeHours);
}
