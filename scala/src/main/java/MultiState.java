public class MultiState {

    public MultiState(String name, String name2, String name3) {
    }
    public MultiState(String name, String name2, Integer name3) {
    }

    public MultiState(String name, String name2) {
        this(name, name2, "null");
    }

    public MultiState(String name) {
        this(name, "null", "null");
    }

}
