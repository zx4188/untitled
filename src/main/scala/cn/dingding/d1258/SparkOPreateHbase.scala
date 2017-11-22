package cn.dingding.d1258

import org.apache.hadoop.hbase.Cell
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhangxu 15:32 2017/11/17
  * Description Spark操作Hbase表
  */
object SparkOPreateHbase {
  def main(args: Array[String]): Unit = {
    val conf = HbaseUtils.conf
    val tableName = "test1"
    val sc = new SparkContext(new SparkConf().setMaster("local[2]").setAppName("Spark&Hbase"))
    //设置查询的表名。
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    val rdd = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    println(tableName + "表中共有：" + rdd.count() + " 条数据!")
    //rdd.foreach(x=>println(new String(x._2.getRow)))
    rdd.foreach({ x =>
      val ress: Array[Cell] = x._2.rawCells()
      for (res <- ress) println("rowkey=" + new String(res.getRow) + " ,column=" + new String(res.getQualifier) + " value=" + new String(res.getValue))
      println("-------------------------------------")
    }
    )
    //val conn=HbaseUtils.getConn()
  }
}
