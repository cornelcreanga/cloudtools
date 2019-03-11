package com.com.ccreanga.cloudutils;

import static com.com.ccreanga.cloudutils.util.Util.configureLog;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.com.ccreanga.cloudutils.util.Wildcard;
import java.util.List;
import java.util.logging.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "get-buckets")
public class GetBuckets  implements Runnable {

    @CommandLine.Option(names = {"-b", "--bucketPattern"}, required = false, description = "Bucket name pattern")
    private String bucketNamePattern;

    @CommandLine.Option(names = {"-p", "--profile"}, required = false, description = "Profile name", defaultValue = "default")
    private String profile;

    @CommandLine.Option(names = {"-e", "--extended"}, required = false, description = "Extended info, not yet implemented")
    private boolean extended;


    public static final Logger log = Logger.getLogger("log");

    @Override
    public void run() {
        configureLog(log);
        try {
            AmazonS3 s3client = AmazonS3ClientBuilder.standard().
                withCredentials(new ProfileCredentialsProvider(profile)).
                withRegion("us-east-1").
                build();

            List<Bucket> buckets = s3client.listBuckets();
            for (Bucket bucket : buckets) {
                if ((bucketNamePattern != null) && (!Wildcard.matches(bucket.getName(), bucketNamePattern))) {
                    continue;
                }
                if (extended) {

                } else {
                    log.info(bucket.getName());
                }
            }
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
        CommandLine commandLine = new CommandLine(new GetBuckets());
        commandLine.parseWithHandler(new CommandLine.RunAll(), args);

    }
}
