package cz.vutbr.feec.xdobro22.employeedatabase.tests;

import static org.junit.Assert.*;
import cz.vutbr.feec.xdobro22.employeedatabase.*;

public class FirmTest {
    Firm firm;

    @org.junit.Before
    public void setUpFirm() {
        firm = new Firm();
        firm.addEmployee(EmployeeType.ASSISTANT, 1, "A", "A");
        firm.addEmployee(EmployeeType.TECHNICIAN, 2, "B", "B");
        firm.addEmployee(EmployeeType.DEVELOPER, 3, "C", "C");
        firm.addEmployee(EmployeeType.DIRECTOR, 4, "D", "D");
    }


    @org.junit.Test
    public void testWorkAssigning() {
        firm.setMaxWorkload(200);
        firm.assignWork(WorkType.ADMINISTRATIVE,300);
        firm.assignWork(WorkType.TECHNICAL, 300);
        firm.assignWork(WorkType.DEVELOPMENT, 300);
        assertEquals(((Administrative)firm.getEmployee(1)).getAdministrativeHours(), 200);
        assertEquals(((Administrative)firm.getEmployee(2)).getAdministrativeHours(), 100);
        assertEquals(((Technical)firm.getEmployee(2)).getTechnicalHours(), 100);
        assertEquals(((Technical)firm.getEmployee(3)).getTechnicalHours(), 200);
        assertEquals(((Development)firm.getEmployee(3)).getDevelopmentHours(), 0);
        assertEquals(((Development)firm.getEmployee(4)).getDevelopmentHours(), 300);
    }

}