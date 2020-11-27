package CounterAppForRedisPackage;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedisAppController {
    @RequestMapping("/")
    @ResponseBody
    String home() {
        RedisClient redisClient = RedisClient.create("redis://password@172.17.0.1:8085/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        if (syncCommands.exists("counter").intValue() == 0){
            syncCommands.set("counter", "0");
            RedisApp.counter = 0;
        }
        else {
            Long counterLong = syncCommands.incr("counter");
            RedisApp.counter = counterLong.intValue();
        }

        connection.close();
        redisClient.shutdown();

        RedisApp.counter++;
        return "{ \"RedisAppID\": " + RedisApp.serverID + ", \"counter\": " + RedisApp.counter + " }";
    }
}
