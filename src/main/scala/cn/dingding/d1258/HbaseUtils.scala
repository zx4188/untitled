package cn.dingding.d1258

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory}

/**
  * Created by zhangxu 9:44 2017/11/16
  * Description 
  */
object HbaseUtils {
  val conf=HBaseConfiguration.create()
  conf.set("hbase.zookeeper.property.client","2181")
  conf.set("hbase.zookeeper.quorum","hdp01,hdp03,hdp04")

  def getConn(): Connection ={
    //conf.set("hbase.master","hdp01")
    val conn=ConnectionFactory.createConnection(conf)
    conn
  }
}
