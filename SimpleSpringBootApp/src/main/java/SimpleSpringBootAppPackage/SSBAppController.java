package SimpleSpringBootAppPackage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SSBAppController {
    @RequestMapping("/")
    @ResponseBody
    String home() {
        SSBApp.counter++;
        return "{ \"WebAppID\": " + SSBApp.serverID + ", \"counter\": " + SSBApp.counter + " }";
    }
}
