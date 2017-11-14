package com.dd1258

import java.util.logging.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Author zhangxu
  * Create in 15:24 2017/11/13
  * Description 测试
  */
object MyScala {
  def main(args: Array[String]): Unit = {
    val log=Logger.getLogger("test")
    log.setLevel(Level.WARNING)
    //System.out.println("Hello Scala!!");
    val conf=new SparkConf().setAppName("test-spark").setMaster("local[3]")
    val sc=new SparkContext(conf)
    val rdd=sc.textFile("file:///D:/a.txt",1)
    val wc=rdd.flatMap(x=>x.split(",")).map(x=>(x,1)).reduceByKey((x,y)=>(x+y))
    wc.foreach(x=>println(x))
    wc.saveAsTextFile("D:/out")
    sc.stop()
  }
}

