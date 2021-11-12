#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "@aws-cdk/core";
import { EmailServiceStack } from "../lib/email-service-stack";

const app = new cdk.App();
new EmailServiceStack(app, "EmailServiceStack", {
  env: {
    account: process.env.CDK_DEFAULT_ACCOUNT,
    region: process.env.CDK_DEFAULT_REGION,
  },
});
