package AppPackage;

import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ControllerVision {

    public static void runCommand(String commandLine){
        String str;
        Process proc;
        try{
            proc = Runtime.getRuntime().exec(commandLine);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while((str = bufferedReader.readLine()) != null){
                System.out.println(str);
            }
            proc.waitFor();
            System.out.println("exit code: " + proc.exitValue());
            proc.destroy();
        }
        catch (Exception ignored){

        }
    }

    public static String detectOnImage(File imageFile) {
        String request = "curl -k -v \"https://smarty.mail.ru/api/v1/objects/detect?oauth_provider=mcs&oauth_token=QzyzAKmwgCG9WHYMpx75J9oeaCwxHyXkENyxALqtghhVMH5ru\" " +
                "-F file_0=@" + imageFile.getPath() + " " +
                "-F meta=\"{\\\"mode\\\":[\\\"object\\\"],\\\"images\\\":[{\\\"name\\\":\\\"file_0\\\"}]}\"";
        //runCommand(request);
        String response = "";
        String str;
        Process proc;
        try{
            proc = Runtime.getRuntime().exec(request);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while((str = bufferedReader.readLine()) != null){
                response += str;
            }
            proc.waitFor();
            proc.destroy();
        }
        catch (Exception e){
            return "";
        }
        return response;
    }
}
