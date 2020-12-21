package AppPackage.controllers;

import AppPackage.ControllerS3;
import AppPackage.ControllerVision;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RestController
public class BucketsRestController {
    private ControllerS3 controllerS3 = new ControllerS3();


    //Работа с Бакетами
    @RequestMapping(value = "/allBuckets", method = RequestMethod.GET)
    public List<String> allBucketNames() {
        List<String> bucketNames = controllerS3.getListBuckets();
        System.out.println("Request for buckets list received.");
        return bucketNames;
    }
    @RequestMapping(value = "/createBucket/{bucketName}", method = RequestMethod.GET)
    public @ResponseBody String createBucket(@PathVariable("bucketName") String bucketName) {
        boolean result = controllerS3.createBucket(bucketName);
        System.out.println("Request for for bucket '" + bucketName + "' creation received.");
        return (result)? "created" : "error";
    }
    @RequestMapping(value = "/deleteBucket/{bucketName}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteBucket(@PathVariable("bucketName") String bucketName) {
        boolean result = controllerS3.deleteBucket(bucketName);
        System.out.println("Request for bucket '" + bucketName + "' deletion received.");
        return (result)? "deleted" : "error";
    }

    //Работа с изображениями
    @RequestMapping(value = "/imagesList/{bucketName}", method = RequestMethod.GET)
    public List<String> getImagesList(@PathVariable("bucketName") String bucketName){
        List<String> fileNames = controllerS3.getFileListOfBucket(bucketName);
        System.out.println("Request for list of files in bucket '" + bucketName + "' received.");
        return fileNames;
    }
    @RequestMapping(value = "/uploadImage/{bucketName}/{imageName}", method = RequestMethod.POST)
    public String uploadImage(@PathVariable("bucketName") String bucketName,@PathVariable("imageName") String imageName, @RequestParam("file") MultipartFile imageFile) {
        System.out.println("Request for image '" + imageName + "' upload to bucket '" + bucketName + "' received.");
        File localFile = new File("ImageFiles/" + imageName);
        if (localFile.exists()) localFile.delete();
        String visionData = null;
        try{
            localFile.createNewFile();
            Files.write(localFile.toPath(), imageFile.getBytes(), StandardOpenOption.WRITE);
            visionData = ControllerVision.detectOnImage(localFile);
            if (!controllerS3.uploadFileToBucket(bucketName, localFile)) return "error";
            saveVisionDataForImage(imageName, visionData);
        }catch (IOException e){
            return "error";
        }
        return visionData;
    }
    @RequestMapping(value = "/images/{bucketName}/{imageName}", method = RequestMethod.GET)
    public byte[] downloadImage(@PathVariable("bucketName") String bucketName, @PathVariable("imageName") String imageName) {
        System.out.println("Request for image '" + imageName + "' download in bucket '" + bucketName + "' received.");
        controllerS3.downloadFileFromBucket(bucketName, imageName);
        File imageFile = new File("ImageFiles/" + imageName);

        if (!imageFile.exists()) System.out.println("file not exist");

        byte[] imageBytes = null;
        try{
            imageBytes = Files.readAllBytes(imageFile.toPath());

        }catch (IOException e){
            if (imageBytes == null) System.out.println("imageBytes is null");
        }
        imageFile.delete();

        return imageBytes;
    }
    @RequestMapping(value = "/vision/{bucketName}/{imageName}", method = RequestMethod.GET)
    public String getImageVisionData(@PathVariable("bucketName") String bucketName,@PathVariable("imageName") String imageName) {
        System.out.println("Request for vision data of image '" + imageName + "' from bucket '" + bucketName + "' received.");
        String imageData = loadVisionDataForImage(imageName);
        if (imageData == null){
            controllerS3.downloadFileFromBucket(bucketName, imageName);
            File imageFile = new File("ImageFiles/" + imageName);
            imageData = ControllerVision.detectOnImage(imageFile);
            saveVisionDataForImage(imageName, imageData);
            imageFile.delete();
        }
        return imageData;
    }
    @RequestMapping(value = "/delete/{bucketName}/{imageName}", method = RequestMethod.DELETE)
    public String deleteImage(@PathVariable("bucketName") String bucketName,@PathVariable("imageName") String imageName) {
        System.out.println("Request for image '" + imageName + "' deletion from bucket '" + bucketName + "' received.");
        File dataFile = new File("ImageFiles/" + imageName + ".txt");
        if (dataFile.exists()) dataFile.delete();
        return (controllerS3.deleteFileFromBucket(bucketName, imageName))? "deleted" : "error";
    }

    public void saveVisionDataForImage(String imageFileName, String visionData){
        String dataFileName = "ImageFiles/" + imageFileName + ".txt";
        File dataFile = new File(dataFileName);
        if (dataFile.exists()) dataFile.delete();
        try {
            dataFile.createNewFile();
            FileWriter fileWriter = new FileWriter(dataFile);
            fileWriter.write(visionData);
            fileWriter.close();
        } catch (IOException e){

        }
    }
    public String loadVisionDataForImage(String imageFileName){
        String dataFileName = "ImageFiles/" + imageFileName + ".txt";
        File dataFile = new File(dataFileName);
        String imageData = null;
        if (!dataFile.exists()) return null;
        try {
            List<String> strings = Files.readAllLines(dataFile.toPath());
            imageData = "";
            for (String string : strings){
                imageData += string;
            }
        } catch (IOException e){
            return null;
        }
        return imageData;
    }
}
