package com.dd1258.kafka


import java.sql.{Connection, PreparedStatement, ResultSet}

import org.apache.commons.dbcp.BasicDataSource

/**
  * Created by zhangxu 9:58 2017/11/21
  * Description 
  */
object ConnectPoolUtil {
  private var bs:BasicDataSource=null

  /**
    * 创建数据源
    * @return
    */
  def getDataSource():BasicDataSource={
    if(bs==null){
      bs=new BasicDataSource
      bs.setDriverClassName("com.mysql.jdbc.Driver")
      bs.setUrl("jdbc:mysql://192.168.1.12:3306/zx")
      bs.setUsername("root")
      bs.setPassword("root")
      bs.setMaxActive(200)  //设置最大并发数
      bs.setInitialSize(30) //数据库初始化时，创建的连接个数
      bs.setMinIdle(50)     //最小空闲连接数
      bs.setMaxWait(200)    //数据库最大连接数
      bs.setMinEvictableIdleTimeMillis(60*1000) //空闲连接60秒钟后释放
      bs.setTimeBetweenEvictionRunsMillis(5*60*1000)  //5分钟检测一次是否有死掉的线程
    }
    bs
  }

  /**
    * 释放数据源
    */
  def shutDownDataSource(): Unit ={
    if(bs!=null){
      bs.close()
    }
  }


  /**
    * 获取数据库连接
    */
  def getConnection():Connection={
    var con:Connection=null
    try {
      if (bs != null) {
        con = bs.getConnection
      } else {
        con = getDataSource().getConnection()
      }
    } catch {
      case  e:Exception=>println(e.getMessage)
    }
    con
  }

  /**
    * 关闭连接
    */
  def closeCon(rs:ResultSet,ps:PreparedStatement,con:Connection): Unit ={
    if(rs!=null) rs.close()
    if(ps!=null) ps.close()
    if(con!=null) con.close()
  }
}
