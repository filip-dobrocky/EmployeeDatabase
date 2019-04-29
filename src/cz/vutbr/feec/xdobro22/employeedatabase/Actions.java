package cz.vutbr.feec.xdobro22.employeedatabase;

public class Actions {
    public static int getVowelCount(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++)
            if ("aeiouyýíéúäô".indexOf(Character.toLowerCase(s.charAt(i))) >= 0)
                n++;
        return n;
    }

    public static void printReversed(String s) {
        System.out.print(new StringBuilder(s).reverse().toString());
    }

    public static void printEmployee(Employee employee) {
        if (employee != null)
            System.out.format("Zamestnanec: %s\tVyťaženie: %dh/m\tMzda: %dKč/h\tJe indisponovaný: %s%n",
                    employee.getName(), employee.getWorkload(), employee.getWage(), employee.isIll() ? "áno" : "nie");
        else
            System.out.println("Zamestnanec neexistuje.");
    }
}
