package online.meinkraft.customvillagertrades.util;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {

    private final String resourceURL;
    private final String resourceId;
    
    private final String currentVersion;
    private final String latestVersion;

    private final UpdateType updateType;

    private final boolean updateAvailable;

    public static enum UpdateType {
        RELEASE, 
        SNAPSHOT, 
        EXPERIMENTAL, 
        CURRENT, 
        UNKNOWN
    }

    public UpdateChecker(JavaPlugin plugin, String resourceId) {

        this.resourceId = resourceId;
        resourceURL = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;

        currentVersion = plugin.getDescription().getVersion();
        latestVersion = streamLatestVersion();

        if(latestVersion == null) {

            updateType = UpdateType.UNKNOWN;
            updateAvailable = false;

        }
        else {

            if(currentVersion.equals(latestVersion)) {
                updateType = UpdateType.CURRENT;
            }
            else if(latestVersion.toUpperCase().contains("SNAPSHOT")) {
                updateType = UpdateType.SNAPSHOT;
            }
            else if(latestVersion.toUpperCase().contains("EXPERIMENTAL")) {
                updateType = UpdateType.EXPERIMENTAL;
            }
            else {
                updateType = UpdateType.RELEASE;
            }

            String[] currentVersionNumberStrings = currentVersion.replaceAll("-(SNAPSHOT|RELEASE)", "").split("\\.");
            int[] currentVersionNumbers = new int[currentVersionNumberStrings.length];
            for(int index = 0; index < currentVersionNumberStrings.length; index++) {
                currentVersionNumbers[index] = Integer.parseInt(currentVersionNumberStrings[index]);
            }

            String[] latestVersionNumberStrings = latestVersion.replaceAll("-(SNAPSHOT|RELEASE)", "").split("\\.");
            int[] latestVersionNumbers = new int[latestVersionNumberStrings.length];
            for(int index = 0; index < latestVersionNumberStrings.length; index++) {
                latestVersionNumbers[index] = Integer.parseInt(latestVersionNumberStrings[index]);
            }

            if(compareVersionNumbers(currentVersionNumbers, latestVersionNumbers) == 1) {
                updateAvailable = true;
            }
            else {
                updateAvailable = false;
            }

        }
        
    }

    public int compareVersionNumbers(int[] a, int[] b) {
        int depth = Math.min(a.length, b.length);
        for(int index = 0; index < depth; index++) {
            if(b[index] > a[index]) return 1;
            if(b[index] < a[index]) return -1;
        }
        return 0;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + resourceId;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    private String streamLatestVersion() {
        try {

            URL url = new URL(resourceURL);
            URLConnection urlConnection = url.openConnection();

            return new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream())
            ).readLine();

        } catch (IOException exception) {
            return null;
        }
    }
    
}