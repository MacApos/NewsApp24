package com.infrastructure;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.route53.*;
import software.amazon.awscdk.services.route53.targets.BucketWebsiteTarget;
import software.amazon.awscdk.services.s3.*;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.List;

import static com.infrastructure.InfrastructureApp.*;

public class StaticSiteStack extends Stack {
    public StaticSiteStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        String ROOT_DOMAIN = "newsapp24.com";
        String HOSTED_ZONE_ID = "Z0954086RSQN4LCK08H6";
        String staticSiteBucketId = APP_NAME + "Static";
        String subdomainBucketId = APP_NAME + "Subdomain";
        String subdomainName = "www." + ROOT_DOMAIN;

        // Create bucket for static files
        Bucket staticSiteBucket = Bucket.Builder.create(this, staticSiteBucketId)
                .autoDeleteObjects(true)
                .publicReadAccess(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .bucketName(ROOT_DOMAIN)
                .blockPublicAccess(new BlockPublicAccess(
                        BlockPublicAccessOptions.builder()
                                .blockPublicAcls(false)
                                .blockPublicPolicy(false)
                                .ignorePublicAcls(false)
                                .restrictPublicBuckets(false)
                                .build()))
                .websiteIndexDocument("index.html")
                .build();

        // Create bucket for redirection to root domain
        Bucket subdomainBucket = Bucket.Builder.create(this, subdomainBucketId)
                .autoDeleteObjects(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .bucketName(subdomainName)
                .websiteRedirect(RedirectTarget.builder()
                        .hostName(ROOT_DOMAIN)
                        .protocol(RedirectProtocol.HTTP)
                        .build())
                .build();

        // Deploy static files
        BucketDeployment.Builder.create(this, staticSiteBucketId + "Deployment")
                .sources(List.of(Source.asset("../software/StaticSite/build")))
                .destinationBucket(staticSiteBucket)
                .build();

        IHostedZone myZone = HostedZone.fromHostedZoneAttributes(this, APP_NAME + "Zone",
                HostedZoneAttributes.builder()
                        .zoneName(ROOT_DOMAIN)
                        .hostedZoneId(HOSTED_ZONE_ID)
                        .build());

        ARecord.Builder.create(this, APP_NAME + "ARecord")
                .recordName(ROOT_DOMAIN)
                .target(RecordTarget.fromAlias(new BucketWebsiteTarget(staticSiteBucket)))
                .zone(myZone)
                .build();

        ARecord.Builder.create(this, APP_NAME + "SubdomainARecord")
                .recordName(subdomainName)
                .target(RecordTarget.fromAlias(new BucketWebsiteTarget(subdomainBucket)))
                .zone(myZone)
                .build();
    }
}
