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
