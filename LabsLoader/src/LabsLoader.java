import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class LabsLoader {

    public static List<Process> processes = new LinkedList<Process>();

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
        catch (Exception e){}
    }
    public static void runCommandInBackground(String commandLine){
        String str;
        Process proc;
        try{
            proc = Runtime.getRuntime().exec(commandLine);

        }
        catch (Exception e){}
    }

    public static class Docker{
        public static void runContainer(String imageName, String containerName, String args, boolean background, boolean removeAfterExit, int[][] portPairsHostContainer){
            String commandLine = "docker run --name=" + containerName + " ";
            for (int[] portsPair : portPairsHostContainer) {
                commandLine += "-p " + portsPair[0] + ":" + portsPair[1] + " ";
            }
            if (background) commandLine += "-d ";
            if (removeAfterExit) commandLine += "--rm ";
            commandLine += imageName + " " + args;
            runCommand(commandLine);
        }
        public static void killContainer(String containerName){
            runCommand("docker kill " + containerName);
        }
        public static void containersList(){
            runCommand("docker ps");
        }
        public static void containersListAll(){
            runCommand("docker ps --all");
        }
        public static void removeContainer(String containerName){
            runCommand("docker rm " + containerName);
        }
        public static void buildImage(String dockerFile, String imageName){
            String commandLine = "docker build -t " + imageName + " . -f " + dockerFile;
            runCommand(commandLine);
        }
        public static void removeImage(String imageName){
            runCommand("docker rmi " + imageName);
        }
        public static void imagesList(){
            runCommand("docker images");
        }
    }

    public static class LabHighload1{
        public static void prepare(){
            //Удаляем все контейнеры и снимки с именами, подобными тем, что мы сейчас создадим
            Docker.removeImage("lb_roundrobin");
            Docker.removeImage("lb_random");
            Docker.removeImage("lb_leastconn");
            Docker.removeImage("lb_leasttime");
            Docker.removeImage("lb_iphash");
            Docker.removeImage("lb_hash");
            Docker.removeImage("counterapp");
            //Создаём все снимки
            Docker.buildImage("DockerFiles/dockerfile_counterApp", "counterapp");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_RoundRobin", "lb_roundrobin");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_Random", "lb_random");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_LeastConn", "lb_leastconn");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_LeastTime", "lb_leasttime");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_IpHash", "lb_iphash");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_Hash", "lb_hash");

        }
        public static void start(){
            //Стартуем всеми контейнерами
            Docker.runContainer("counterapp", "counterapp_1", "-serverid 1",true, true, new int[][]{{8081, 8080}});
            Docker.runContainer("counterapp", "counterapp_2", "-serverid 2",true, true, new int[][]{{8082, 8080}});
            Docker.runContainer("counterapp", "counterapp_3", "-serverid 3",true, true, new int[][]{{8083, 8080}});
            Docker.runContainer("counterapp", "counterapp_4", "-serverid 4",true, true, new int[][]{{8084, 8080}});

            //Docker.runContainer("lb_roundrobin", "balancer", "",true, true, new int[][]{{8080, 80}});
            Docker.runContainer("lb_random", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_leastconn", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_leasttime", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_iphash", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_hash", "balancer", "",true, true, new int[][]{{8080, 80}});
        }
        public static void stop(){
            //Останавливаем и удаляем все контейнеры
            Docker.killContainer("counterapp_1");
            Docker.killContainer("counterapp_2");
            Docker.killContainer("counterapp_3");
            Docker.killContainer("counterapp_4");
            Docker.killContainer("balancer");
        }
    }

    public static class LabCloud1{
        public static void prepare(){
            //Удаляем все контейнеры и снимки с именами, подобными тем, что мы сейчас создадим
            Docker.removeImage("lb_random");
            Docker.removeImage("counterapp");
            //Создаём все снимки
            Docker.buildImage("DockerFiles/dockerfile_counterApp", "counterapp");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_Random", "lb_random");
        }
        public static void start(){
            //Стартуем с помощью docker-compose
            runCommand("docker-compose -f DockerFiles/docker-compose.yml up -d");
        }
        public static void stop(){
            //Останавливаем и удаляем все контейнеры
            Docker.killContainer("counterapp_1");
            Docker.killContainer("counterapp_2");
            Docker.killContainer("counterapp_3");
            Docker.killContainer("balancer");
            Docker.removeContainer("counterapp_1");
            Docker.removeContainer("counterapp_2");
            Docker.removeContainer("counterapp_3");
            Docker.removeContainer("balancer");
        }
    }

    public static class LabHighload2{
        public static void prepare(){
            //Удаляем все контейнеры и снимки с именами, подобными тем, что мы сейчас создадим
            Docker.removeImage("lb_roundrobin");
            Docker.removeImage("lb_random");
            Docker.removeImage("lb_leastconn");
            Docker.removeImage("lb_leasttime");
            Docker.removeImage("lb_iphash");
            Docker.removeImage("lb_hash");
            Docker.removeImage("counterapp");
            //Создаём все снимки
            Docker.buildImage("DockerFiles/dockerfile_counterApp", "counterapp");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_RoundRobin", "lb_roundrobin");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_Random", "lb_random");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_LeastConn", "lb_leastconn");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_LeastTime", "lb_leasttime");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_IpHash", "lb_iphash");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_Hash", "lb_hash");

        }
        public static void start(){
            //Стартуем всеми контейнерами
            Docker.runContainer("counterapp", "counterapp_1", "-serverid 1",true, true, new int[][]{{8081, 8080}});
            Docker.runContainer("counterapp", "counterapp_2", "-serverid 2",true, true, new int[][]{{8082, 8080}});
            Docker.runContainer("counterapp", "counterapp_3", "-serverid 3",true, true, new int[][]{{8083, 8080}});
            Docker.runContainer("counterapp", "counterapp_4", "-serverid 4",true, true, new int[][]{{8084, 8080}});

            //Docker.runContainer("lb_roundrobin", "balancer", "",true, true, new int[][]{{8080, 80}});
            Docker.runContainer("lb_random", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_leastconn", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_leasttime", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_iphash", "balancer", "",true, true, new int[][]{{8080, 80}});
            //Docker.runContainer("lb_hash", "balancer", "",true, true, new int[][]{{8080, 80}});
        }
        public static void stop(){
            //Останавливаем и удаляем все контейнеры
            Docker.killContainer("counterapp_1");
            Docker.killContainer("counterapp_2");
            Docker.killContainer("counterapp_3");
            Docker.killContainer("counterapp_4");
            Docker.killContainer("balancer");
        }
    }

    public static void main(String[] args) {


        //Docker.imagesList();

    }
}

