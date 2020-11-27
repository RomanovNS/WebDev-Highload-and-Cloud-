package CounterAppForRedisPackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RedisApp {
    public static int serverID = 0;
    public static int counter = 0;

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
