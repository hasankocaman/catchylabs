package driver;



/**
 * Browser tiplerini tanımlayan enum sınıfı.
 * OCP (Open/Closed Principle) prensibine uygun olarak,
 * yeni browser tipleri eklemek için bu enum'a yeni değerler eklenebilir.
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE,
    SAFARI
}