package cz.vutbr.feec.xdobro22.employeedatabase;

public enum EmployeeType {
    ASSISTANT("asistent"), TECHNICIAN("technik"), DEVELOPER("vývojár"), DIRECTOR("riaditeľ");

    private String s;

    EmployeeType(String s) {
        this.s = s;
    }

    public String getString() {
        return s;
    }
}
