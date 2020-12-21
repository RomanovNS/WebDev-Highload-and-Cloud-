package AppPackage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllerS3 {
    AmazonS3 s3;

    public ControllerS3() {
        AWSCredentials credentials = new BasicAWSCredentials("xqtEiq43W9XvKNsmNjCGAA", "335XVoKFfGbo3gYKUJ382CAMeFLm5eZTkue1aeoDhsT5");
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("https://hb.bizmrg.com", "ru-msk");
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    //Operations on buckets
    public boolean existsBucket(String bucketName) {
        return s3.doesBucketExistV2(bucketName);
    }
    public boolean createBucket(String bucketName) {
        if (s3.doesBucketExistV2(bucketName)) {
            System.out.println("Bucket \"" + bucketName + "\" already exists!");
            return false;
        }
        else{
            try {
                s3.createBucket(bucketName);
            } catch (AmazonS3Exception e) {
                System.out.println("Cannot create bucket \"" + bucketName + "\"!");
                return false;
            }
        }
        return true;
    }
    public List<String> getListBuckets() {
        List<Bucket> buckets = s3.listBuckets();
        LinkedList<String> bucketNames = new LinkedList<String>();
        for (Bucket bucket : buckets) {
            bucketNames.add(bucket.getName());
        }
        return bucketNames;
    }
    public boolean deleteBucket(String bucketName) {
        ObjectListing objectListing = s3.listObjects(bucketName);
        while (true) {
            for (Iterator<?> iterator =
                 objectListing.getObjectSummaries().iterator();
                 iterator.hasNext(); ) {
                S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                s3.deleteObject(bucketName, summary.getKey());
            }
            // more object_listing to retrieve?
            if (objectListing.isTruncated()) {
                objectListing = s3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
        s3.deleteBucket(bucketName);
        return true;
    }
    public Bucket getBucket(String bucketName) {
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets){
            if (bucketName.equals(bucket.getName()))
                return bucket;
        }
        return null;
    }
    public List<Bucket> getAllBuckets(){
        return s3.listBuckets();
    }

    //Operations on objects, which is in bucket
    public boolean uploadFileToBucket(String bucketName, File file) {
        try {
            s3.putObject(bucketName, file.getName(), file);
        } catch (AmazonS3Exception e) {
            return false;
        }
        return true;
    }
    public List<String> getFileListOfBucket(String bucketName) {
        LinkedList<String> fileNames = new LinkedList<String>();
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            fileNames.add(objectSummary.getKey());
        }
        return fileNames;
    }
    public boolean downloadFileFromBucket(String bucketName, String fileKey) {
        try {
            S3Object object = s3.getObject(bucketName, fileKey);
            S3ObjectInputStream s3inpStream = object.getObjectContent();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("ImageFiles/" + fileKey));
            byte[] buffer = new byte[4096];
            int dataSize = 0;
            while ((dataSize = s3inpStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, dataSize);
            }
            s3inpStream.close();
            fileOutputStream.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean deleteFileFromBucket(String bucketName, String fileKey) {
        try {
            s3.deleteObject(bucketName, fileKey);
        } catch (Exception e){
            return false;
        }
        return true;
    }

}
