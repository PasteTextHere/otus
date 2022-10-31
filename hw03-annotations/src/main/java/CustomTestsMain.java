import annotations.After;
import annotations.Before;
import annotations.HWAnnotation;
import annotations.Test;

@HWAnnotation
public class CustomTestsMain {

    @Before
    public void runBeforeTest() {
        System.out.println("Before method");
    }

    @Before
    public void aaa() {
        System.out.println("next ll be exception");
        throw new RuntimeException();
    }

    @Test
    public void testMethod() {
        System.out.println("Into test method");
    }

    @Test
    public void testExceptionMethod() {
        throw new NullPointerException("Try NPE");
    }

    @After
    public void afterTestMethod() {
        System.out.println("After test method");
    }
}
