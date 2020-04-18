/*
 * Created by DamianosDW.
 * https://damianosdw.ovh
 */

package ovh.damianosdw.cmp.modules;

import ovh.damianosdw.cmp.utils.AppUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigFileModule
{
    private static final String filePath = "config.txt";

    public static void prepareConfigFile()
    {
        BufferedWriter bufferedWriter = null;
        try {
            if(Files.notExists(Paths.get(filePath)))
            {
                bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
                String options = prepareOption("sqlServerAddress", "127.0.0.1") +
                        prepareOption("sqlServerPort", "3306") +
                        prepareOption("sqlServerLogin", "test") +
                        prepareOption("sqlServerPassword", "test") +
                        prepareOption("debugMode", "false");
                bufferedWriter.write(options);
                AppUtils.showInformationAlertAndWait("Uzupełnij dane w pliku konfiguracyjnym i uruchom ponownie aplikację.");
                AppUtils.stopApp();
            }
        } catch(IOException e) {
            e.printStackTrace();
            closeBufferedWriter(bufferedWriter);
        }
    }

    private static String prepareOption(String optionName, String optionValue)
    {
        return optionName + ": " + optionValue + "\n";
    }

    public static String getOptionValue(String option)
    {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                if(line.contains(option))
                    return line.substring(line.indexOf(":") + 1).trim();
            }
        } catch(IOException e) {
            e.printStackTrace();
            closeBufferedReader(bufferedReader);
        }
        return "";
    }

    public static void saveOptionValue(String option, String value)
    {
        BufferedWriter bufferedWriter = null;
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                if(line.contains(option))
                    sb.append(option).append(": ").append(value);
                else
                    sb.append(line);
                sb.append("\n");
            }
            closeBufferedReader(bufferedReader);
            bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(sb.toString());
        } catch(IOException e) {
            e.printStackTrace();
            closeBufferedWriter(bufferedWriter);
        }
    }

    private static void closeBufferedWriter(BufferedWriter bufferedWriter)
    {
        try {
            if(bufferedWriter != null)
                bufferedWriter.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeBufferedReader(BufferedReader bufferedReader)
    {
        try {
            if(bufferedReader != null)
                bufferedReader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
