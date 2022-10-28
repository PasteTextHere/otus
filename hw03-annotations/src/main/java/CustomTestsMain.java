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

    @Test
    public void testMethod() {
        System.out.println("Into test method");
    }

    @After
    public void afterTestMethod() {
        System.out.println("After test method");
    }
}
