package outcomes;

import org.hyperskill.hstest.dev.stage.BaseStageTest;
import org.hyperskill.hstest.dev.testcase.CheckResult;
import org.hyperskill.hstest.dev.testcase.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

public class FatalErrorGeneratingTestsWithAssertion extends BaseStageTest {

    public static void main(String[] args) {
        System.out.println("Hello World");
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public FatalErrorGeneratingTestsWithAssertion() {
        super(FatalErrorGeneratingTestsWithAssertion.class);
    }

    @Before
    public void before() {
        exception.expect(AssertionError.class);
        exception.expectMessage("Fatal error during testing, please send the report to Hyperskill team.");
    }

    @Override
    public List<TestCase> generate() {
        assert false;
        return Arrays.asList(
                new TestCase()
        );
    }

    @Override
    public CheckResult check(String reply, Object clue) {
        return CheckResult.TRUE;
    }
}