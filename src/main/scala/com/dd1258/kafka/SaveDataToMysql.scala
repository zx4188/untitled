package com.dd1258.kafka

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by zhangxu 9:58 2017/11/21
  * Description 
  */
object SaveDataToMysql {
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志，在终端上显示所需要的日志
    Logger.getLogger("org.apache.spark").setLevel(Level.OFF)
    Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.OFF)

    //初始化spark-steaming
    val conf=new SparkConf().setAppName("Save2Mysql").setMaster("local[2]")
    val sc=new SparkContext(conf)
    val ssc=new StreamingContext(sc,Durations.seconds(5))
    val kafkaParams=new scala.collection.mutable.HashMap[String, Object]

    kafkaParams.put("bootstrap.servers","hdp02:9092")
    kafkaParams.put("group.id","g1")
    kafkaParams.put("key.deserializer",classOf[StringDeserializer])
    kafkaParams.put("value.deserializer",classOf[StringDeserializer])

    kafkaParams.put("auto.offset.reset","earliest")
    val topic = Array("mykafka","test")
    val lines=KafkaUtils.createDirectStream[String,String](ssc,LocationStrategies.PreferConsistent,ConsumerStrategies.Subscribe[String,String](topic,kafkaParams))
    //lines.map(s =>(s.key(),s.value())).print()
    lines.map(s=>s.value).flatMap(_.split(",")).map(x=>(x,1)).reduceByKeyAndWindow((x:Int,y:Int)=>x+y,Durations.seconds(15),Durations.seconds(15)).print()
    //单词统计
    lines.map(s=>s.value).flatMap(_.split(",")).map(x=>(x,1)).reduceByKey((x,y)=>x+y).foreachRDD(
      line=> {
        line.foreachPartition(
          word => {
            val conn = ConnectPoolUtil.getConnection()
            conn.setAutoCommit(false)
            val stmt = conn.createStatement()
            for (data <- word) {
              stmt.addBatch("insert into mykafka(word,count) values('" + data._1 + "','" + data._2 + "')")
            }
            stmt.executeBatch()
           
           
            conn.commit
            conn.close()
          })
      })
    println(lines.getClass)
    ssc.start
    ssc.awaitTermination()
    ssc.stop()
  }
}
