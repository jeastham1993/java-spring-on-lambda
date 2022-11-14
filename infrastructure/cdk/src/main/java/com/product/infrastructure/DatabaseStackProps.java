package com.product.infrastructure;

import software.amazon.awscdk.services.ec2.ISecurityGroup;
import software.amazon.awscdk.services.ec2.IVpc;

public class DatabaseStackProps {
    static DatabaseStackProps props;

    private IVpc vpc;
    private ISecurityGroup applicationSecurityGroup;

    public static DatabaseStackProps builder() {
        props = new DatabaseStackProps();

        return props;
    }

    public DatabaseStackProps withVpc(IVpc vpc) {
        props.setVpc(vpc);

        return props;
    }

    public DatabaseStackProps withApplicationSecurityGroup(ISecurityGroup sg) {
        props.setApplicationSecurityGroup(sg);

        return props;
    }

    public DatabaseStackProps build() {
        return props;
    }

    public IVpc getVpc() {
        return vpc;
    }

    private void setVpc(IVpc vpc) {
        this.vpc = vpc;
    }

    public ISecurityGroup getApplicationSecurityGroup()
    {
        return applicationSecurityGroup;
    }

    private void setApplicationSecurityGroup(ISecurityGroup sg) {
        this.applicationSecurityGroup = sg;
    }
}
