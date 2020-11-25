package CounterAppForRedisPackage;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class RedisApp {

    public static int serverID = 0;
    public static int counter = 0;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        counter++;
        return "{ \"WebAppID\": " + serverID + ", \"counter\": " + counter + " }";
    }

    public static void main(String[] args) {
        int arg_id = 0;
        while (arg_id < args.length - 1) {
            if (args[arg_id].equals("-serverid")) {
                serverID = Integer.parseInt(args[arg_id + 1]);
            }
            arg_id++;
        }

        SpringApplication.run(RedisApp.class, args);
    }
}
