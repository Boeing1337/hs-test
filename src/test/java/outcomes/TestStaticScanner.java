package outcomes;

import org.hyperskill.hstest.v7.stage.BaseStageTest;
import org.hyperskill.hstest.v7.testcase.CheckResult;
import org.hyperskill.hstest.v7.testcase.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class UserClass {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(scanner.nextInt());
        System.out.println(scanner.nextInt());
    }
}

public class TestStaticScanner extends BaseStageTest<String> {

    public TestStaticScanner() {
        super(UserClass.class);
    }

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
            new TestCase<String>().setInput("123 435").setAttach("123\n435\n"),
            new TestCase<String>().setInput("1234 546").setAttach("1234\n546\n")
        );
    }

    @Override
    public CheckResult check(String reply, String attach) {
        return new CheckResult(reply.equals(attach));
    }
}