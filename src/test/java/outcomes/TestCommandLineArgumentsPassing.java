package outcomes;

import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.Arrays;
import java.util.List;

public class TestCommandLineArgumentsPassing extends BaseStageTest<String> {

    public static void main(String[] args) {
        System.out.println(args.length);
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    public TestCommandLineArgumentsPassing() {
        super(TestCommandLineArgumentsPassing.class);
    }

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
            new TestCase<String>()
                .addArguments("-in", "123", "-out", "234")
                .setAttach("4\n-in\n123\n-out\n234\n"),

            new TestCase<String>()
                .addArguments("-in", "435")
                .addArguments("-out", "567", "789")
                .setAttach("5\n-in\n435\n-out\n567\n789\n")
        );
    }

    @Override
    public CheckResult check(String reply, String attach) {
        return new CheckResult(reply.equals(attach));
    }
}
