# Keycloak Email Provider for AWS SES (Simple Email Service)

This is an email provider SPI implementation for [Keycloak SSO](https://www.keycloak.org) server.
It's for demo purposes only and can be used as base for your own implementation.

The codebase is provided _as-is_ and might not be free of errors.
So, you're on your own when using it.
Generally, this project is _work in progress_.

## Dependencies
The implementation currently uses the AWS Java API Version 1.
This dependency will be packaged with the help of the Maven Shade Plugin into the target JAR archive.

All other dependencies are used from Keycloaks underlying Wildfly server
(see [jboss-deployment-structure.xml](./src/main/resources/META-INF/jboss-deployment-structure.xml)).

## Installation

Build the project with `mvn package` and copy the generated `.jar` file (the shaded one, not the original one!)
into the `standalone/deployments/` folder of your Keycloak installation.
It will be deployed automatically (hot deployment works the same).

## Configuration

To configure the email provider SPI, include a snippet like this in your `standalone(-ha).xml` file:

```xml
<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
  ...
  <spi name="emailSender">
    <default-provider>aws-ses</default-provider>
    <provider name="aws-ses" enabled="true">
      <properties>
        <property name="region" value="eu-west-1"/>
        <property name="from" value="sender@example.com"/>
        <property name="fromDisplayName" value="Keycloak Demo"/>
        <property name="replyTo" value="reply-to@example.com"/> <!-- optional -->
        <property name="replyToDisplayName" value="Keycloak Demo ReplyTo"/> <!-- optional -->
        <property name="configSetName" value="my-config-set"/> <!-- optional -->
      </properties>
    </provider>
  </spi>
  ...
</subsystem>
```

Alternatively, you can use this `jboss-cli` script snippet to configure your Keycloak server:

```
/subsystem=keycloak-server/spi=emailSender/:add(default-provider=aws-ses)
/subsystem=keycloak-server/spi=emailSender/provider=aws-ses/:add(enabled=true)
/subsystem=keycloak-server/spi=emailSender/provider=aws-ses/:write-attribute(name=properties,value={"region" => "eu-west-1","from" => "sender@example.com","fromDisplayName" => "Keycloak Demo",...})
```

## AWS Configuration

This SPI makes use of the [DefaultAWSCredentialsProviderChain](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html).
So, it's on you how you configure your Keycloak environment in a way that is able to authenticate itself agains AWS.

Your used profile needs the privilege to send emails with at least `ses:SendEmail`.
