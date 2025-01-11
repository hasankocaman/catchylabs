package reporting;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ExtentReports raporlama sistemini yöneten sınıf.
 * Singleton pattern kullanılarak tek bir instance üzerinden raporlama yapılır.
 */
public class ExtentReportManager {
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final Map<String, ExtentTest> testMap = new HashMap<>();
    private static final String REPORT_PATH = "test-output/reports/";
    private static final String REPORT_FILE_PREFIX = "TestReport_";
    private static final String REPORT_FILE_SUFFIX = ".html";

    private ExtentReportManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * ExtentReports instance'ını oluşturur veya mevcut instance'ı döndürür
     * @return ExtentReports instance
     */
    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    /**
     * Yeni bir ExtentReports instance'ı oluşturur
     */
    private static void createInstance() {
        // Report dosyası için timestamp oluştur
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportFileName = REPORT_FILE_PREFIX + timestamp + REPORT_FILE_SUFFIX;
        String reportFilePath = REPORT_PATH + reportFileName;

        // Report dizinini oluştur
        new File(REPORT_PATH).mkdirs();

        // ExtentSparkReporter'ı yapılandır
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
        configureReporter(sparkReporter);

        // ExtentReports'u oluştur ve yapılandır
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        setSystemInfo();

        logger.info("ExtentReports instance created with report file: {}", reportFilePath);
    }

    /**
     * ExtentSparkReporter'ı yapılandırır
     * @param sparkReporter yapılandırılacak reporter
     */
    private static void configureReporter(ExtentSparkReporter sparkReporter) {
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Test Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setJs(""); // Custom JavaScript eklenebilir
        sparkReporter.config().setCss(""); // Custom CSS eklenebilir
    }

    /**
     * Sistem bilgilerini rapora ekler
     */
    private static void setSystemInfo() {
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
        extentReports.setSystemInfo("Environment", System.getProperty("env", "test"));
        extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
    }

    /**
     * Yeni bir test oluşturur
     * @param testName test adı
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        testMap.put(testName, test);
        logger.info("Created test in extent report: {}", testName);
        return test;
    }

    /**
     * Yeni bir test oluşturur (açıklamalı)
     * @param testName test adı
     * @param description test açıklaması
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testMap.put(testName, test);
        logger.info("Created test in extent report: {} with description: {}", testName, description);
        return test;
    }

    /**
     * Mevcut testi getirir
     * @param testName test adı
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest getTest(String testName) {
        return testMap.get(testName);
    }

    /**
     * Test node'u oluşturur
     * @param parentTestName parent test adı
     * @param nodeName node adı
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest createNode(String parentTestName, String nodeName) {
        ExtentTest parentTest = getTest(parentTestName);
        ExtentTest node = parentTest.createNode(nodeName);
        testMap.put(nodeName, node);
        logger.info("Created node '{}' under test '{}'", nodeName, parentTestName);
        return node;
    }

    /**
     * Raporu kaydeder ve kapatır
     */
    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Extent report flushed and saved");
        }
    }

    /**
     * Test haritasını temizler
     */
    public static synchronized void clearTestMap() {
        testMap.clear();
        logger.info("Test map cleared");
    }
}