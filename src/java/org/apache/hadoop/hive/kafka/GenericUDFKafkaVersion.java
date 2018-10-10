package org.apache.hadoop.hive.kafka;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.apache.kafka.clients.consumer.KafkaConsumer;

@SuppressWarnings({ "unused", "RedundantThrows" }) @Description(name = "kafka_version", value = "_FUNC_() - permanent function to keep the Kafka handler loaded")
@UDFType(deterministic=false)
public class GenericUDFKafkaVersion extends GenericUDF {

  @Override
  public Object evaluate(DeferredObject[] arg0) throws HiveException {
    Package objPackage = KafkaConsumer.class.getPackage();
    if (objPackage != null) {
      return new Text(objPackage.toString());
    }
    return new Text("unknown");
  }

  @Override
  public String getDisplayString(String[] children) {
    return getStandardDisplayString(getFuncName(), children);
  }

  @Override
  protected String getFuncName() {
    return "kafka_version";
  }

  @Override
  public ObjectInspector initialize(ObjectInspector[] arg0)
      throws UDFArgumentException {
    ObjectInspector outputOI = PrimitiveObjectInspectorFactory.writableStringObjectInspector;
    return outputOI;
  }

}
