package Domain;

public class States {

    public static final String NEW = "Ny";
    public static final String READ = "Läst";
    public static final String SAVED = "Sparad";

    public static String[] getStates() {
        return new String[]{NEW, READ, SAVED};
    }
}
