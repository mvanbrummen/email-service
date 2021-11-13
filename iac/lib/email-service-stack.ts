import { ApplicationLoadBalancedFargateService } from "@aws-cdk/aws-ecs-patterns";
import * as cdk from "@aws-cdk/core";
import * as ecs from "@aws-cdk/aws-ecs";
import * as ec2 from "@aws-cdk/aws-ec2";
import { FargatePlatformVersion } from "@aws-cdk/aws-ecs";

export class EmailServiceStack extends cdk.Stack {
  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const defaultVpc = ec2.Vpc.fromLookup(this, "DefaultVpc", {
      isDefault: true,
    });

    const fargateService = new ApplicationLoadBalancedFargateService(
      this,
      "EmailFargateService",
      {
        cpu: 512,
        vpc: defaultVpc,
        memoryLimitMiB: 1024,
        desiredCount: 1,
        assignPublicIp: true,
        platformVersion: FargatePlatformVersion.VERSION1_3,
        taskImageOptions: {
          containerPort: 8080,
          image: ecs.ContainerImage.fromAsset("../email-service"),
          environment: {
            SENDGRID_API_KEY: process.env.SENDGRID_API_KEY!,
            MAILGUN_API_KEY: process.env.MAILGUN_API_KEY!,
          },
        },
      }
    );

    fargateService.targetGroup.configureHealthCheck({
      path: "/actuator/health",
    });
  }
}
