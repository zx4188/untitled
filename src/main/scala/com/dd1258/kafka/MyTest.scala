package com.dd1258.kafka

import scala.util.Random

/**
  * Created by zhangxu 16:15 2017/11/21
  * Description 
  */
object MyTest {
  def main(args: Array[String]): Unit = {
    val r1="hello"
    val r2="world!!!"
    val sql = "insert into streaming(item,count) values('" + r1 + "'," + r2 + ")"
    val sql2="insert into test_log2(time, ip, user_id, user_type, source, scene) values('" + r1+"','"+r1+"','"+r1+"','"+r1+"','"+r1+"','"+r1 + "')"
    println(sql2)
    var count=0
    while(true){
      count+=1
      val index=Random.nextInt(5)
      print(index)
      if (count==10) System.exit(1)
    }
  }
}
