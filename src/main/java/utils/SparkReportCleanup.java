package utils;


import java.io.File;
import io.cucumber.java.Before;

public class SparkReportCleanup {

    @Before
    public void cleanupSparkReports() {
        // Spark.html raporlarını sil
        deleteSparkReports();
    }

    private void deleteSparkReports() {
        File reportsDir = new File("test-output/test-reports");
        if (reportsDir.exists()) {
            for (File file : reportsDir.listFiles()) {
                file.delete();
                System.out.println("Deleted report: " + file.getName());
            }
        }
    }
}