package net.D3GN.MiracleM4n.mAutoPaste;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class mAutoPaste extends JavaPlugin {

    //Console & Logger
    public static Logger Log = Logger.getLogger("Minecraft");
    ColouredConsoleSender console = null;

    //Strings
    String ThrownPaste;
    String PluginNamePaste;
    String CBVersionPaste;
    String PluginInfoPaste;
    String FinalPaste;

    public void onEnable() {
        // Default plugin data
        console = new ColouredConsoleSender((CraftServer) getServer());
        PluginDescriptionFile pdfFile = getDescription();

        Log.setFilter(new Filter() {
            public boolean isLoggable(LogRecord record) {
                if (record.getLevel().getName().contains("SEVERE")) {
                    try {
                        //Declare Vars
                        ThrownPaste = "";
                        PluginNamePaste = "";
                        CBVersionPaste = "";
                        PluginInfoPaste = "";
                        FinalPaste = "";
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

                        if (pluginTest != null) {
                            PluginInfoPaste = "Plugin Name: " + pluginTest.getDescription().getName() + '\n' + "Plugin Version: " + pluginTest.getDescription().getVersion() + '\n' + "Plugin Author: " + pluginTest.getDescription().getAuthors();
                        }

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

        console.sendMessage((pdfFile.getName()) +  " version " + pdfFile.getVersion() + " is enabled!");
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();

        console.sendMessage((pdfFile.getName()) +  " version " + pdfFile.getVersion() + " is disabled!");
    }
}
