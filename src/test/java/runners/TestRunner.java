package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "reporting.TestListener",  // düzeltilmiş package yolu
                "pretty",
                "html:target/cucumber-reports",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "json:target/cucumber.json"
        },
        features = "src/test/resources/features",
        glue = "stepdefinitions",
        tags = "@smoke or @regression"
)
public class TestRunner {
}