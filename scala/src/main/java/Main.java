
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        Pattern compile = Pattern.compile("(\\d{8})");
        Matcher matcher = compile.matcher("20202020");
        System.out.println(matcher.find());
    }
}
