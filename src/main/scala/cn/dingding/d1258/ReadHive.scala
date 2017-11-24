package cn.dingding.d1258

import java.sql.{DriverManager, ResultSet}

import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by zhangxu 10:37 2017/11/20
  * Description 
  */
object ReadHive {
  val driverName="org.apache.hive.jdbc.HiveDriver"
  Class.forName(driverName)
  val conn=DriverManager.getConnection("jdbc:hive2://hdp01:10000/dd_tianqi","root","root")

  /**
    *   插入数据
    */
  def insertHive():Unit={
    val pstm=conn.prepareStatement("insert into city2id(id,city_id,cityzh,provincezh) values(?,?,?,?)")
   pstm.setInt(1,20172016)
   pstm.setString(2,"20172017")
   pstm.setString(3,"曲阜")
   pstm.setString(4,"山东")
   /*pstm.setObject(1,20172016)
   pstm.setObject(2,"20172017")
   pstm.setObject(3,"曲阜")
   pstm.setObject(4,"山东")*/
   pstm.executeUpdate()
   println("数据插入完毕！！！！")
   pstm.close()
   conn.close()
  }

  /**
    *  从Hive表中查询
    */
  def selectHive():Unit={
    val st=conn.createStatement()
    var rs:ResultSet=st.executeQuery("select * from city2id")
    var count=0
    while(rs.next()) count+=1 //println("id:"+rs.getString(1)+" code:"+rs.getString(2)+" countyName:"+rs.getString(3)+" provinceName:"+rs.getString(4))
    println("该表内总计有 "+count+"行！")
    //println(rs.getString(1))

    st.close()
    val pstm=conn.prepareStatement("select * from city2id where id>?")
    pstm.setInt(1,2017000)
    rs=pstm.executeQuery()
    while(rs.next()) println("id:"+rs.getString(1)+" code:"+rs.getString(2)+" countyName:"+rs.getString(3)+" provinceName:"+rs.getString(4))


    pstm.close()
    conn.close()

  }

  /**
    *   Hive 不支持delete
    */
  def deleteHive():Unit={
    val st=conn.createStatement()
    st.executeUpdate("delete from city2id where id=20172016")
    println("delete  is  over!!")
    st.close()
  }

  /**
    * operate mysql
    */
  def writeMysql(sql:String):Unit={
    Class.forName("com.mysql.jdbc.Driver")
    val mysqlConn=DriverManager.getConnection("jdbc:mysql://192.168.1.12:3306/zx","root","root")
    val mysqlStat=mysqlConn.createStatement()
    val rs=mysqlStat.executeQuery(sql)
    while(rs.next()) println(rs.getNString(1))
    mysqlStat.close()
    mysqlConn.close()
  }
  def testSort(): Unit ={
    val conf=new SparkConf().setAppName("sortbyKey").setMaster("local[3]")
    val sc=new SparkContext(conf)
    val rdd1=sc.parallelize(Array((1,6,3),(2, 3, 3), (1, 1, 2), (1, 3, 5), (2, 1, 2)))
    val rdd2=rdd1.map(f=>((f._1,f._3),f))
    //rdd2.foreach(x=>println(x))
    val rdd3=rdd2.sortByKey(true,2)
 /*   rdd3.foreach(x=>println(x))
    rdd3.foreach(x=>println(x._1+""+x._2))
    rdd3.values.foreach(x=>println(x))*/

    rdd1.top(3)(Ordering.by[(Int,Int,Int),Int](_._1)).foreach(x=>println(x))

  }

    def add01(a:Int)=(b:Int)=>(a-3)*b //注意a

    def demo02(): Unit ={
      val add02=add01(5) //add01返回一个函数b=>5+b
      println(add02(3)) //结果:8  为什么5被留下来，涉及到闭包
    }

  //使用java里面的线程，无缝结合
  def runInThread(fun:() => Unit): Unit = { //参数是一个函数，它没有参数和返回值
    new Thread() {
      override def run(): Unit = {
        fun()
      }
    }.start()  //完全是java的内容
  }

  //测试代码，无规则交替输出了T1 T2 T3的相应值
  def demo(): Unit ={
    for (i <- 1 to 3)
      runInThread(() => {
        Range(1, 100).foreach(x => println("T" + i + " " + x))
      })
  }
  def main(args: Array[String]): Unit = {
    val sql="show tables"
    //this.selectHive()
    //this.deleteHive()
    //this.writeMysql(sql)
    //conn.close()

    //this.testSort()
    //this.demo02()
    this.demo()
    println(add01(5)(3))
  }
}
