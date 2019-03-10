package com.com.ccreanga.cloudutils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class S3Items {

    public static void main(String[] args) {

        try {
            AmazonS3 s3client = AmazonS3ClientBuilder.standard().
                withCredentials(new ProfileCredentialsProvider()).
                withRegion("us-east-1").
                build();

            final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName("").withMaxKeys(1000);
            ListObjectsV2Result result;
            do {
                result = s3client.listObjectsV2(req);

                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    S3Object object = s3client.getObject(new GetObjectRequest("", objectSummary.getKey()));
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    System.out.println( Base64.getEncoder().encodeToString(md.digest(ByteStreams.toByteArray(object.getObjectContent()))));
                    System.out.println(objectSummary.getKey() + " " + objectSummary.getSize());
                    System.out.println("---");
                }

                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());

        } catch (AmazonServiceException ase) {
            System.out.println("AmazonServiceException Message:" + ase.getMessage() + " Status Code:" + ase.getStatusCode() +
                    " AWS Error Code:" + ase.getErrorCode() + " Error Type:" + ase.getErrorType() + " Request ID:" + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException Message:" + ace.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
