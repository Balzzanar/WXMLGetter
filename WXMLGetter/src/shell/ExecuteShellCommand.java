package shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by wades on 1/9/14.
 */
public class ExecuteShellCommand {

    private String output;

    public ExecuteShellCommand(String command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.output = output.toString();
    }

    public String getResult() {
        return this.output;
    }
}
