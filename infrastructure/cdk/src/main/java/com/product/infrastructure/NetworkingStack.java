package com.product.infrastructure;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

public class NetworkingStack extends Construct {
    private final Vpc vpc;
    private final ISecurityGroup applicationSecurityGroup;

    public NetworkingStack(final Construct scope, final String id){
        super(scope, id);

        vpc = new Vpc(this, "product-api-vpc", VpcProps.builder()
                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .maxAzs(3)
                .build());

        applicationSecurityGroup = new SecurityGroup(this, "ApplicationSecurityGroup",
                SecurityGroupProps
                        .builder()
                        .securityGroupName("applicationSG")
                        .vpc(vpc)
                        .allowAllOutbound(true)
                        .build());

        CfnOutput appSgOutput = new CfnOutput(this, "app-sg-output", CfnOutputProps.builder()
                .exportName("ApplicationSecurityGroupId")
                .value(applicationSecurityGroup.getSecurityGroupId())
                .build());

        CfnOutput subnet1 = new CfnOutput(this, "app-subnet-1", CfnOutputProps.builder()
                .exportName("Subnet1Id")
                .value(vpc.getPrivateSubnets().get(0).getSubnetId())
                .build());

        CfnOutput subnet2 = new CfnOutput(this, "app-subnet-2", CfnOutputProps.builder()
                .exportName("Subnet2Id")
                .value(vpc.getPrivateSubnets().get(1).getSubnetId())
                .build());
    }

    public Vpc getVpc() {
        return vpc;
    }

    public ISecurityGroup getApplicationSecurityGroup() {
        return applicationSecurityGroup;
    }
}
