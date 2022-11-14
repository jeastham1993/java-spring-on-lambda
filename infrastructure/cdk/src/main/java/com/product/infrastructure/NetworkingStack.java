package com.product.infrastructure;

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
                .natGateways(2)
                .maxAzs(3)
                .build());

        applicationSecurityGroup = new SecurityGroup(this, "ApplicationSecurityGroup",
                SecurityGroupProps
                        .builder()
                        .securityGroupName("applicationSG")
                        .vpc(vpc)
                        .allowAllOutbound(true)
                        .build());
    }

    public Vpc getVpc() {
        return vpc;
    }

    public ISecurityGroup getApplicationSecurityGroup() {
        return applicationSecurityGroup;
    }
}
