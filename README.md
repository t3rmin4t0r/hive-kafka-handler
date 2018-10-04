# hive-kafka-handler

## Send Fake Data to Kafka Broker
Use https://github.com/b-slim/avro-kafka-producer.

## Create Table External Table

    ADD JAR target/kafka-handler-4.0.0-SNAPSHOT.jar;
    
    
    CREATE EXTERNAL TABLE kafka_table_avro
		STORED BY 'org.apache.hadoop.hive.kafka.KafkaStorageHandler'
		TBLPROPERTIES
		("kafka.topic" = "test-avro-3",
		"kafka.bootstrap.servers"="localhost:9092",
		"kafka.serde.class"="org.apache.hadoop.hive.serde2.avro.AvroSerDe",
		'avro.schema.literal'='{
		  "type" : "record",
		  "name" : "Wikipedia",
		  "namespace" : "com.slim.kafka",
		  "version": "1",
		  "fields" : [ {
		    "name" : "isrobot",
		    "type" : "boolean"
		  }, {
		    "name" : "channel",
		    "type" : "string"
		  }, {
		    "name" : "timestamp",
		    "type" : "string"
		  }, {
		    "name" : "flags",
		    "type" : "string"
		  }, {
		    "name" : "isunpatrolled",
		    "type" : "boolean"
		  }, {
		    "name" : "page",
		    "type" : "string"
		  }, {
		    "name" : "diffurl",
		    "type" : "string"
		  }, {
		    "name" : "added",
		    "type" : "long"
		  }, {
		    "name" : "comment",
		    "type" : "string"
		  }, {
		    "name" : "commentlength",
		    "type" : "long"
		  }, {
		    "name" : "isnew",
		    "type" : "boolean"
		  }, {
		    "name" : "isminor",
		    "type" : "boolean"
		  }, {
		    "name" : "delta",
		    "type" : "long"
		  }, {
		    "name" : "isanonymous",
		    "type" : "boolean"
		  }, {
		    "name" : "user",
		    "type" : "string"
		  }, {
		    "name" : "deltabucket",
		    "type" : "double"
		  }, {
		    "name" : "deleted",
		    "type" : "long"
		  }, {
		    "name" : "namespace",
		    "type" : "string"
		  } ]
		}'
		);

## To use this with LLAP 

There is an llap feature which uploads all user functions along with the LLAP packaging, which can be used to preload this jar into LLAP on restarts.

	hadoop fs -copyFromLocal target/kafka-handler-4.0.0-SNAPSHOT.jar  /tmp/kafka-handler-4.0.0-SNAPSHOT.jar 

Then

	beeline> CREATE FUNCTION loadkafka as 'org.apache.hadoop.hive.kafka.GenericUDFKafkaVersion'  USING JAR 'hdfs://tmp/kafka-handler-4.0.0-SNAPSHOT.jar'
	
Restarting LLAP from Ambari will localize the kafka handler jar on all LLAP daemons.
