package com.product.infrastructure;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcAttributes;
import software.amazon.awscdk.services.ec2.VpcProps;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.ArrayList;
import java.util.List;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        NetworkingStack networking = new NetworkingStack(this, "NetworkingStack");

        DatabaseStack databaseStack = new DatabaseStack(this, "DatabaseStack", DatabaseStackProps.builder()
                .withVpc(networking.getVpc())
                .withApplicationSecurityGroup(networking.getApplicationSecurityGroup())
                .build());
    }
}
