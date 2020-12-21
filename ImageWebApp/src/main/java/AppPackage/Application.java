package AppPackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.List;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {


        SpringApplication.run(Application.class, args);

        //ControllerS3 controllerS3 = new ControllerS3();
        //controllerS3.createBucket("mybucket6661");
        //controllerS3.createBucket("mybucket6662");
        //controllerS3.createBucket("mybucket6663");


        //controllerS3.uploadFileToBucket("mybucket666", new File("ImageFiles/testImage.png"));
        //List<String> fileNames = controllerS3.getFileListOfBucket("mybucket666");
        //for (String fileName : fileNames) {
        //    System.out.println(fileName);
        //}
        //controllerS3.downloadFileFromBucket("mybucket666", "testImage.png");
        //controllerS3.deleteFileFromBucket("mybucket666", "testImage.png");

        //controllerS3.deleteBucket("ddddd22222");

        //List<String> bucketNames = controllerS3.getListBuckets();
        //for (String bucketName : bucketNames) {
        //    System.out.println(bucketName);
        //}


        //String request = ControllerVision.detectOnImage(new File("ImageFiles/visionTest1.jpg"));
        //System.out.println(request);

        System.out.println("Application started!");
    }
}
