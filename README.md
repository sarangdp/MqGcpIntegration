# MqGcpIntegration
Integration Of GCP Dataflow with IBM Mq with JmsIO

# To test this code, install Docker on VM. Follow below steps, (Debian GCP VM)
https://docs.docker.com/engine/install/debian/

# Get IBM MQ for development in a container
https://developer.ibm.com/tutorials/mq-connect-app-queue-manager-containers/

Add below Maven dependencies, for Mq spring starter

    <dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
		</dependency>
		<dependency>
			<groupId>com.ibm.mq</groupId>
			<artifactId>mq-jms-spring-boot-starter</artifactId>
			<version>2.7.2</version>
		</dependency>

# MQ JMS application Development using SpringBoot
https://developer.ibm.com/tutorials/mq-jms-application-development-with-spring-boot/
