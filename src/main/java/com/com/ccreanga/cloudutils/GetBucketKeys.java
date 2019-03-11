package com.com.ccreanga.cloudutils;

import static com.com.ccreanga.cloudutils.util.Util.configureLog;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.util.logging.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "get-bucket-keys")
public class GetBucketKeys implements Runnable {

    public static final Logger log = Logger.getLogger("log");

    @CommandLine.Option(names = {"-b", "--bucket"}, required = true, description = "Bucket name")
    private String bucketName;

    @CommandLine.Option(names = {"-p", "--profile"}, required = false, description = "Profile name", defaultValue = "default")
    private String profile;

    @CommandLine.Option(names = {"-e", "--extended"}, required = false, description = "Extended info, not yet implemented")
    private boolean extended;

    @Override
    public void run() {

        configureLog(log);
        try {
            AmazonS3 s3client = AmazonS3ClientBuilder.standard().
                withCredentials(new ProfileCredentialsProvider(profile)).
                withRegion("us-east-1").
                build();

            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(1000);
            ListObjectsV2Result result;
            do {
                result = s3client.listObjectsV2(req);

                for (S3ObjectSummary summary : result.getObjectSummaries()) {
                    //S3Object object = s3client.getObject(new GetObjectRequest("", summary.getKey()));
                    log.info(summary.getKey() + "," + summary.getSize() + "," + summary.getETag());
                }
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());

        } catch (
            AmazonServiceException ase) {
            System.err.println("AmazonServiceException Message:" + ase.getMessage() + " Status Code:" + ase.getStatusCode() +
                " AWS Error Code:" + ase.getErrorCode() + " Error Type:" + ase.getErrorType() + " Request ID:" + ase.getRequestId());
        } catch (
            AmazonClientException ace) {
            System.err.println("AmazonClientException Message:" + ace.getMessage());
        }

    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new GetBucketKeys());
        if (args.length == 0) {
            commandLine.usage(System.out);
        } else {
            commandLine.parseWithHandler(new CommandLine.RunAll(), args);
        }
    }
}
