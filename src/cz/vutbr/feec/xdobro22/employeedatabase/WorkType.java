package cz.vutbr.feec.xdobro22.employeedatabase;

public enum WorkType {
    ADMINISTRATIVE(Administrative.class), TECHNICAL(Technical.class), DEVELOPMENT(Development.class);
    private Class interfaceType;

    WorkType(Class interfaceType) {
        this.interfaceType = interfaceType;
    }

    Class getInterfaceType() {
        return interfaceType;
    }
}
