package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportManager {
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final Map<String, ExtentTest> testMap = new HashMap<>();
    private static final String HTML_REPORT_PATH = "test-output/HtmlReport/";
    private static final String PDF_REPORT_PATH = "test-output/PdfReport/";
    private static final String REPORT_FILE_PREFIX = "TestReport_";
    private static final String HTML_SUFFIX = ".html";
    private static final String PDF_SUFFIX = ".pdf";

    private ExtentReportManager() {
        // Private constructor
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    private static void createInstance() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // HTML rapor için dosya yolu
        String htmlReportFileName = REPORT_FILE_PREFIX + timestamp + HTML_SUFFIX;
        String htmlReportFilePath = HTML_REPORT_PATH + htmlReportFileName;

        // Dizinleri oluştur
        new File(HTML_REPORT_PATH).mkdirs();
        new File(PDF_REPORT_PATH).mkdirs();

        // HTML Reporter'ı yapılandır
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(htmlReportFilePath);
        configureHtmlReporter(sparkReporter);

        // ExtentReports'u oluştur ve reporter'ı ekle
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        setSystemInfo();

        logger.info("ExtentReports instance created with HTML report: {}", htmlReportFilePath);
    }

    private static void configureHtmlReporter(ExtentSparkReporter sparkReporter) {
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Test Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.viewConfigurer()
                .viewOrder()
                .as(new ViewName[] {
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.CATEGORY,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION
                })
                .apply();
    }

    private static void setSystemInfo() {
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
        extentReports.setSystemInfo("Environment", System.getProperty("env", "test"));
        extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
        extentReports.setSystemInfo("Test Type", "End to End");
    }

    public static synchronized ExtentTest createTest(String testName) {
        if (testName == null || testName.trim().isEmpty()) {
            testName = "Unnamed Test-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        }
        ExtentTest test = getInstance().createTest(testName);
        testMap.put(testName, test);
        logger.info("Created test in extent report: {}", testName);
        return test;
    }

    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testMap.put(testName, test);
        logger.info("Created test in extent report: {} with description: {}", testName, description);
        return test;
    }

    public static synchronized ExtentTest getTest(String testName) {
        return testMap.get(testName);
    }

    public static synchronized ExtentTest createNode(String parentTestName, String nodeName) {
        ExtentTest parentTest = getTest(parentTestName);
        ExtentTest node = parentTest.createNode(nodeName);
        testMap.put(nodeName, node);
        logger.info("Created node '{}' under test '{}'", nodeName, parentTestName);
        return node;
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Reports have been generated successfully");
        }
    }

    public static synchronized void clearTestMap() {
        testMap.clear();
        logger.info("Test map cleared");
    }
}