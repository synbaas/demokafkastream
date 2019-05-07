# Kafka Streaming 
Kafka Streams is a library for building streaming applications, specifically applications that transform input Kafka topics into output Kafka topics (or calls to external services, or updates to databases, or  other ). Kafka Streams addresses the  problems in stream processing such as;
  Event-at-a-time processing (not microbatch) with millisecond latency
  Stateful processing including distributed joins and aggregations
  Windowing with out-of-order data using a DataFlow-like model
  Distributed processing and fault-tolerance with fast failover
  Reprocessing capabilities so you can recalculate output when your code changes
  No-downtime rolling deployments

# Steps to setup Kafka 
  https://dzone.com/articles/running-apache-kafka-on-windows-os

  Downloading the Required Files ---
  Download Server JRE according to your OS and CPU architecture from http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
  Download and install 7-zip from http://www.7-zip.org/download.html
  Download and extract ZooKeeper using 7-zip from http://zookeeper.apache.org/releases.html
  Download and extract Kafka using 7-zip from http://kafka.apache.org/downloads.html
  
 # Start Kafka server
  kafka-server-start.bat C:\kafka_2.11-2.1.0\kafka_2.11-2.1.0\config\server.properties
 
 # Start ZooKeeper 
 Run ZooKeeper by opening a new cmd and type zkserver.
 
 # Command to create Topics
 
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-plaintext-input

kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-wordcount-output

# Run producer - Input topic
kafka-console-producer --topic streams-plaintext-input --broker-list localhost:9092

# Run consumer -Output Topic
./bin/kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic streams-wordcount-output --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer



