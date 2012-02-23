package in.mDev.MiracleM4n.mAutoPaste;

import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class mAutoPaste extends JavaPlugin {
    // Default plugin data
    PluginDescriptionFile pdfFile;

    // Logger
    public static Logger Log = Logger.getLogger("Minecraft");

    public void onEnable() {
        // Default plugin data
        pdfFile = getDescription();

        Log.setFilter(new Filter() {
            @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
            public boolean isLoggable(LogRecord record) {
                if (record.getLevel().getName().contains("SEVERE")) {
                    try {
                        //Declare Vars
                        String PluginInfoPaste = "";

                        String ThrownPaste;
                        String PluginNamePaste;
                        String CBVersionPaste;
                        String FinalPaste;

                        //Parse Vars
                        Throwable errorThrown = record.getThrown();
                        StringWriter sw = new StringWriter();
                        errorThrown.printStackTrace(new PrintWriter(sw));
                        String stackTrace = sw.toString();

                        String[] splitMessage = record.getMessage().split(" ");

                        ThrownPaste = "Error:" + '\n' + record.getMessage() + '\n' + stackTrace;
                        PluginNamePaste = splitMessage[splitMessage.length - 1];
                        CBVersionPaste = getServer().getName() + " Version: " + '\n' + getServer().getVersion();

                        Plugin pluginTest = getServer().getPluginManager().getPlugin(PluginNamePaste);

                        if (pluginTest != null)
                            PluginInfoPaste = "Plugin Name: " + pluginTest.getDescription().getName() + '\n' + "Plugin Version: " + pluginTest.getDescription().getVersion() + '\n' + "Plugin Author: " + pluginTest.getDescription().getAuthors();

                        FinalPaste = CBVersionPaste + '\n' + '\n' + PluginInfoPaste + '\n' + '\n' + ThrownPaste;

                        // Construct data
                        String data = URLEncoder.encode("api_option", "UTF-8") + "=" + URLEncoder.encode("paste", "UTF-8");
                        data += "&" + URLEncoder.encode("api_dev_key", "UTF-8") + "=" + URLEncoder.encode("122b126dd610384b8c30947e706cdd63", "UTF-8");
                        data += "&" + URLEncoder.encode("api_paste_code", "UTF-8") + "=" + URLEncoder.encode(FinalPaste, "UTF-8");

                        // Send data
                        URL url = new URL("http://pastebin.com/api/api_post.php");
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setRequestMethod("POST");

                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        wr.write(data);
                        wr.flush();

                        // Get the response
                        InputStreamReader ird = new InputStreamReader(conn.getInputStream());
                        BufferedReader rd = new BufferedReader(ird);

                        //Parse Response
                        Log.log(Level.WARNING, "A Severe Error Has Occured: " + record.getMessage());
                        Log.log(Level.WARNING, "Please Give This To The Author Of This Plugin: " + rd.readLine());

                        //Close Streams
                        wr.close();
                        rd.close();

                    } catch (Exception e) {
                        System.out.println("Error Pastebinning Error." + '\n' + e);
                        return true;
                    }
                    return false;
                }
                return true;
            }
        });

        System.out.println("[" + (pdfFile.getName()) + "]" + " version " + pdfFile.getVersion() + " is enabled!");
    }

    public void onDisable() {
        System.out.println("[" + (pdfFile.getName()) + "]" + " version " + pdfFile.getVersion() + " is disabled!");
    }
}
