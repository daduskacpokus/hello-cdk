package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.iam.AccountRootPrincipal;

public class HelloCdkStack extends Stack {

    public HelloCdkStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public HelloCdkStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        Vpc vpc = Vpc.Builder.create(this, "MyVpc")
                .maxAzs(3)  // Default is all AZs in region
                .build();

        Cluster cluster = Cluster.Builder.create(this, "MyCluster")
                .vpc(vpc).build();
        Repository ecr = Repository.Builder.create(this, "my-ecr-repo")
                .repositoryName("my-ecr-repo")
                .build();
        ecr.grantPullPush(AccountRootPrincipal::new);
        // Create a load-balanced Fargate service and make it public
        ApplicationLoadBalancedFargateService service = ApplicationLoadBalancedFargateService.Builder.create(this, "MyFargateService")
                .cluster(cluster)           // Required
                .cpu(1024)                   // Default is 256
                .taskImageOptions(
                        ApplicationLoadBalancedTaskImageOptions.builder()
                                .image(ContainerImage.fromRegistry("amazon/amazon-ecs-sample"))
                                .build())
                .memoryLimitMiB(4096)       // Default is 512
                .publicLoadBalancer(true)   // Default is false
                .build();
//        CfnOutput.Builder.create(this, "service")
//                .description("serviceName")
//                .value(service.getService().getServiceName())
//                .build();
//        CfnOutput.Builder.create(this, "ecr")
//                .value(ecr.getRepositoryUri())
//                .description("ecrRepoUrl")
//                .build();
    }

}
