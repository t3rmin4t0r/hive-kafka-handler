/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.hadoop.hive.kafka.KafkaStreamingUtils.KAFKA_METADATA_COLUMN_NAMES;
import static org.apache.hadoop.hive.kafka.KafkaStreamingUtils.consumerProperties;
import static org.apache.hadoop.hive.kafka.KafkaStreamingUtils.recordWritableFnMap;

/**
 * Test for Utility class.
 */
public class KafkaStreamingUtilsTest {
  public KafkaStreamingUtilsTest() {
  }

  @Test public void testConsumerProperties() {
    Configuration configuration = new Configuration();
    configuration.set("kafka.bootstrap.servers", "localhost:9090");
    configuration.set("kafka.consumer.fetch.max.wait.ms", "40");
    configuration.set("kafka.consumer.my.new.wait.ms", "400");
    Properties properties = consumerProperties(configuration);
    Assert.assertEquals("localhost:9090", properties.getProperty("bootstrap.servers"));
    Assert.assertEquals("40", properties.getProperty("fetch.max.wait.ms"));
    Assert.assertEquals("400", properties.getProperty("my.new.wait.ms"));
  }

  @Test(expected = IllegalArgumentException.class) public void canNotSetForbiddenProp() {
    Configuration configuration = new Configuration();
    configuration.set("kafka.bootstrap.servers", "localhost:9090");
    configuration.set("kafka.consumer." + ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    consumerProperties(configuration);
  }

  @Test(expected = IllegalArgumentException.class) public void canNotSetForbiddenProp2() {
    Configuration configuration = new Configuration();
    configuration.set("kafka.bootstrap.servers", "localhost:9090");
    configuration.set("kafka.consumer." + ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "value");
    consumerProperties(configuration);
  }

  @Test public void testMetadataEnumLookupMapper() {
    int partition = 1;
    long offset = 5L;
    long ts = System.currentTimeMillis();
    long startOffset = 0L;
    long endOffset = 200L;
    byte[] value = "value".getBytes();
    byte[] key = "key".getBytes();
    // ORDER MATTERS here.
    List<Writable>
        expectedWritables =
        Arrays.asList(new BytesWritable(key),
            new IntWritable(partition),
            new LongWritable(offset),
            new LongWritable(ts),
            new LongWritable(startOffset),
            new LongWritable(endOffset));
    KafkaRecordWritable KRWritable = new KafkaRecordWritable(partition, offset, ts, value, startOffset, endOffset, key);

    List<Writable>
        actual =
        KAFKA_METADATA_COLUMN_NAMES.stream()
            .map(recordWritableFnMap::get)
            .map(fn -> fn.apply(KRWritable))
            .collect(Collectors.toList());

    Assert.assertEquals(expectedWritables, actual);
  }
}
