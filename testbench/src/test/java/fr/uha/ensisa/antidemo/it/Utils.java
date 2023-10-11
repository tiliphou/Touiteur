package fr.uha.ensisa.antidemo.it;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Utils {
    public static final String CHROME_VERSION = "111"; // check https://chromedriver.chromium.org/downloads

    private static transient File ChromeDriver;
    public static File getChromeDriver() throws IOException, InterruptedException {
        if (ChromeDriver != null) return ChromeDriver;

        // Finding Chrome version
        String[][] commands = new String[][] {
            new String[]{"sh", "-c", "chrome --version"},
            new String[]{"sh", "-c", "chromium --version"},
            new String[]{"reg", "query", "\"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\"", "/v", "version"},
            new String[]{"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome", "--version"}
        };
        String version = CHROME_VERSION, foundVersion = null;
        int minVersion = -1;
        Pattern chromeVersionDetector = Pattern.compile("(?:Chrom\\w+\\s+|Version\\s*REG_SZ\\s*)([0-9]+)\\.0\\.[0-9]{4}\\.([0-9]{2,})", Pattern.CASE_INSENSITIVE);
        Matcher chromeVersionMatch;
        for (String[] cmd : commands) {
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                BufferedReader lr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = lr.readLine()) != null) {
                    if ((chromeVersionMatch = chromeVersionDetector.matcher(line)).find()) {
                        version = foundVersion = chromeVersionMatch.group(1);
                        minVersion = Integer.parseInt(chromeVersionMatch.group(2));
                        System.out.println("Found version " + version + " (" + minVersion + ')');
                        break;
                    }
                }
            } catch (IOException x) {}
        }

        // Detecting platform
        String os = System.getProperty("os.name");
        if (os != null) os = os.toLowerCase().trim();
        String arch = System.getProperty("os.arch");
        if (arch != null && arch.length() > 2) {
            arch = arch.substring(arch.length()-2);
        } else {
            arch = "64"; // Most common...
        }
        File driver = null;
        if (os.equals("linux")) {
            driver = new File("./chromedriver");
            arch = "64"; // no other version...
        } else if (os.startsWith("win")) {
            os = "win";
            arch = "32"; // no other version...
            driver = new File("./chromedriver.exe");
        } else if (os.startsWith("mac")) {
            // MacOS
            // chromedriver installed via homebrew
            os = "mac";
            if (!System.getProperty("os.arch").trim().toLowerCase().startsWith("x86")) arch += "_arm64";
            driver = new File("./chromedriver");
        } else {
            throw new IOException("Unsupported platform : " + System.getProperty("os.name") + ' ' + System.getProperty("os.arch"));
        }

        // Checking driver version
        int driverMinVersion = -1;
        if (driver.exists() && foundVersion != null && minVersion > 0) {
            try {
                Process p = Runtime.getRuntime().exec(driver.getAbsolutePath() + " --version");
                p.waitFor();
                BufferedReader lr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                String driverVersion;
                while ((line = lr.readLine()) != null) {
                    if ((chromeVersionMatch = chromeVersionDetector.matcher(line)).find()) {
                        driverVersion = chromeVersionMatch.group(1);
                        driverMinVersion = Integer.parseInt(chromeVersionMatch.group(2));
                        System.out.println("Found driver version " + version + " (" + driverMinVersion + ')');
                        if (!driverVersion.equals(foundVersion)) {
                            System.out.println("Version mismatch !");
                            driver.delete();
                        }
                        break;
                    }
                }
            } catch (IOException x) {}
        }

        // Downloading driver or upgrading it
        if (!driver.exists() || (minVersion > 0 && driverMinVersion > 0 && driverMinVersion < minVersion)) {
            // Getting latest driver version available for this major version of Chrome
            System.out.println("Checking latest version available for Chromedriver");
            String driverVersion = null;
            try (InputStream inStr = new URL("https://chromedriver.storage.googleapis.com").openStream()) {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inStr);
                NodeList downloadables = doc.getElementsByTagName("Key");
                
                Pattern driverFinder = Pattern.compile("^" + version + "\\.0\\.[0-9]{4}\\.([0-9]{2,})/chromedriver_" + os + arch + ".zip$", Pattern.CASE_INSENSITIVE);
                int availableMinVersion = driverMinVersion > 0 ? driverMinVersion : 0;
                Matcher m;
                for(int i = 0; i < downloadables.getLength(); i++) {
                    Node downloadable = downloadables.item(i);
                    String item = downloadable.getTextContent();
                    m = driverFinder.matcher(item);
                    if (m.matches()) {
                        int _minVersion = Integer.parseInt(m.group(1));
                        if (_minVersion > availableMinVersion && (minVersion <= 0 || _minVersion <= minVersion)) {
                            driverVersion = item;
                            availableMinVersion = _minVersion;
                        }
                    }
                }
            } catch (SAXException | ParserConfigurationException e) {
                System.err.println("Cannot determine latest Chromedriver version");
                e.printStackTrace();
            }

            if (driverVersion == null && !driver.exists()) throw new IOException("Cannot find driver URL for version " + version + " (on " + os + ' ' + arch + ')');
            
            if (driverVersion != null) {
                System.out.println("Downloading driver " + driverVersion);
                
                URL downloadUrl = new URL("https://chromedriver.storage.googleapis.com/" + driverVersion);
                // Downloading zip
                File driverZip = new File("./chromedriver.zip");
                try (InputStream inStr = downloadUrl.openStream()) {
                    Files.copy(inStr, driverZip.toPath());
                }
                // Extracting driver from zip
                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(driverZip));) {
                    ZipEntry ze;
                    while((ze = zis.getNextEntry()) != null) {
                        if (ze.getName().startsWith("chromedriver")) {
                            if (driver.exists()) driver.delete();
                            Files.copy(zis, driver.toPath());
                            driver.setExecutable(true);
                            break;
                        }
                    }
                }
                driverZip.delete();
            }
        }
        return ChromeDriver = driver;
    }
}
