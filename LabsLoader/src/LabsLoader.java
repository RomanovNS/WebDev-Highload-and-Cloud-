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
        public static void runContainer(String imageName, String containerName, String args, boolean detached, boolean removeAfterExit, int[][] portPairsHostContainer){
            String commandLine = "docker run --name=" + containerName + " ";
            for (int[] portsPair : portPairsHostContainer) {
                commandLine += "-p " + portsPair[0] + ":" + portsPair[1] + " ";
            }
            if (detached) commandLine += "-d ";
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
        public static void killAllContainers(){
            String str;
            Process proc;
            List<String> containersIDs = new LinkedList<String>();
            try{
                proc = Runtime.getRuntime().exec("docker ps -q");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                while((str = bufferedReader.readLine()) != null){
                    containersIDs.add(str);
                }
                proc.waitFor();
                System.out.println("Containers IDs extracted.");
                proc.destroy();
            }
            catch (Exception e){}
            for (String containerID : containersIDs) {
                try{
                    proc = Runtime.getRuntime().exec("docker kill " + containerID);
                    proc.waitFor();
                    System.out.println("Container (ID: " + containerID + ") killed.");
                    proc.destroy();
                }
                catch (Exception e){
                    System.out.println("Container (ID: " + containerID + ") killing error!");
                }
            }
        }
        public static void removeAllContainers(){
            String str;
            Process proc;
            List<String> containersIDs = new LinkedList<String>();
            try{
                proc = Runtime.getRuntime().exec("docker ps -a -q");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                while((str = bufferedReader.readLine()) != null){
                    containersIDs.add(str);
                }
                proc.waitFor();
                System.out.println("Containers IDs extracted.");
                proc.destroy();
            }
            catch (Exception e){}
            for (String containerID : containersIDs) {
                try{
                    proc = Runtime.getRuntime().exec("docker rm " + containerID);
                    proc.waitFor();
                    System.out.println("Container (ID: " + containerID + ") removed.");
                    proc.destroy();
                }
                catch (Exception e){
                    System.out.println("Container (ID: " + containerID + ") removing error!");
                }
            }
        }
        public static void executeCommandInContainer(String container, String commandLine) {
            runCommand("docker exec -it " + container + " " + commandLine);
        }
    }

    public static class LabHighLoad1{
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
            runCommand("docker-compose -f DockerFiles/docker-compose_cloudLaba1.yml up -d");
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

    public static class LabHighLoad2{
        public static void prepare(){
            //Удаляем все контейнеры и снимки с именами, подобными тем, что мы сейчас создадим
            Docker.removeImage("lb_random");
            Docker.removeImage("counterappredis");
            //Создаём все снимки
            Docker.buildImage("DockerFiles/dockerfile_counterAppRedis", "counterappredis");
            Docker.buildImage("DockerFiles/dockerfile_nginxLoadBalancer_Random", "lb_random");
        }
        public static void start(){
            //Стартуем с помощью docker-compose
            runCommand("docker-compose -f DockerFiles/docker-compose_redis.yml up -d");
        }
        public static void stop(){
            //Останавливаем и удаляем все контейнеры
            Docker.killContainer("counterapp_1");
            Docker.killContainer("counterapp_2");
            Docker.killContainer("counterapp_3");
            Docker.killContainer("balancer");
            Docker.killContainer("myredis");
            Docker.removeContainer("counterapp_1");
            Docker.removeContainer("counterapp_2");
            Docker.removeContainer("counterapp_3");
            Docker.removeContainer("balancer");
            Docker.removeContainer("myredis");
        }
    }

    public static class LabHighLoad3{
        public static void prepare(){

        }
        public static void start(){
            runCommand("docker-compose -f DockerFiles/docker-compose_kafkaBitnami.yml up -d");

        }
        public static void stop(){

        }
    }

    public static void main(String[] args) {

        //Docker.buildImage("DockerFiles/dockerfile_counterAppForKubernetes", "romanovns/kubernetesapp");
        //Docker.imagesList();
        //Docker.runContainer("kubernetesapp", "test1", "-serverid 5", true, true, new int[][]{});
        //Docker.containersListAll();
        //Docker.killContainer("test1");

        //LabHighLoad2.prepare();
        //LabHighLoad2.start();
        //LabHighLoad2.stop();

        //Docker.killAllContainers();
        //Docker.removeAllContainers();

        //runCommand("docker attach 49decf055e69");

        //Docker.imagesList();
        //Docker.containersList();
        //Docker.containersListAll();
        //Docker.executeCommandInContainer("49decf055e69", "echo");

        //Docker.removeImage("counterapp");
        //Docker.buildImage("DockerFiles/dockerfile_counterApp", "counterapp");
        //Docker.runContainer("counterapp", "app", "-serverid 1", false, true, new int[][]{{8080, 8080}});

        //runCommand("docker-compose -f DockerFiles/docker-compose_kafkaConfluent.yml up -d");

        //Docker.containersListAll();

        //Docker.removeImage("counterappredis");
        //Docker.buildImage("DockerFiles/dockerfile_counterAppRedis", "counterappredis");
        //Docker.runContainer("counterappredis", "redisapp", "-serverid 1", false, true, new int[][]{{8080, 8080}});
    }
}

