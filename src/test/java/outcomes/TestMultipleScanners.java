package outcomes;

import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TestMultipleScanners extends BaseStageTest<String> {

    private static void in(String s) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (!line.equals(s)) {
            throw new NullPointerException();
        }
    }

    private static void out(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        out("1");
        out("2");
        in("3");
        out("5");
        in("4");
        out("6");
        in("7");
        in("8");
    }

    public TestMultipleScanners() {
        super(TestMultipleScanners.class);
    }

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
            new TestCase<String>()
                .addInput(out -> {
                    if (!out.equals("1\n2\n")) {
                        int x = 0/0;
                    }
                    return "3\n4";
                })
                .addInput(out -> {
                    if (!out.equals("5\n6\n")) {
                        int x = 0/0;
                    }
                    return "7\n8\n";
                })
        );
    }

    @Override
    public CheckResult check(String reply, String attach) {
        return CheckResult.TRUE;
    }
}
