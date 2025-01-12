package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "reporting.TestListener",
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "rerun:target/failed_scenarios.txt"
        },
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"}, // hooks paketi eklendi
        tags = "@negative",
        monochrome = true,
        dryRun = false,
        publish = true // Cucumber raporlarını online olarak görüntüleme seçeneği
)
public class TestRunner {
        /*
         * Bu sınıf test koşumlarını yönetir.
         *
         * Plugin açıklamaları:
         * - "reporting.TestListener": Özel test listener'ı
         * - "pretty": Konsolda okunabilir çıktı
         * - "html:target/cucumber-reports/cucumber.html": HTML rapor
         * - "json:target/cucumber-reports/cucumber.json": JSON rapor
         * - "junit:target/cucumber-reports/cucumber.xml": XML rapor
         * - "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:": Extent rapor
         * - "rerun:target/failed_scenarios.txt": Başarısız senaryoları kaydetme
         */
}