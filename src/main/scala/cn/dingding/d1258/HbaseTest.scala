package cn.dingding.d1258

import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.filter._
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by zhangxu 15:26 2017/11/15
  * Description 
  */
object HbaseTest {
  /*val conf=HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.cilentPort","2181")
    conf.set("hbase.zookeeper.quorum","hdp01,hdp03,hdp04")
    //conf.set("hbase.master","hdp01:16000")
    val conn=ConnectionFactory.createConnection(conf)
    conf.addResource("hbase-site.xml")*/
  val conn = HbaseUtils.getConn()

  def getNameSpace(): Unit = {

    val admin = conn.getAdmin
    val namespaces = admin.listNamespaceDescriptors()
    for (namespace <- namespaces) {
      println(namespace)
    }
    val tables = admin.listTableNames()
    tables.foreach(x => println("NameSpace为 " + x.getNamespaceAsString + ",里边的表为-" + x))
    admin.close()
    conn.close()
  }

  def putData(): Unit = {
    //val tableName=TableName.valueOf("test")
    val table = new HTable(HbaseUtils.conf, "test")
    val put = new Put("test".getBytes())
    put.addColumn("cf".getBytes(), "column".getBytes(), "value".getBytes)

    table.put(put)
    table.close()
  }

  def scanTable(): Unit = {
    //在创建scan对象的时候定义起止key
    val scan = new Scan //("1".getBytes,"110".getBytes)
    scan.addFamily("cf1".getBytes)
    val filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("1")))
    //val filter1=new QualifierFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("box")))
    val filter1 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("avg"))
    val filter2 = new PrefixFilter("9".getBytes)
    //只返回容RowKey,columnfamily,qualifier不返回值
    val filter3 = new KeyOnlyFilter()
    //只返回每一行的第一列
    val filter4 = new FirstKeyOnlyFilter
    val filter5 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("36"))
    scan.setFilter(filter5)
    val table = new HTable(HbaseUtils.conf, "dd_movie")
    val res = table.getScanner(scan)
    /* for(r:Result<-res) {
       val cells = r.listCells()
       println(cells.size())
       //遍历每一行中所有的cells并输出
       for (cell <- cells) {
         println(new String(cell.getRow) + "--" + new String(cell.getFamily) + "--" + new String(cell.getQualifier) + "--" + new String(cell.getValue))
       }
     }*/
    table.getRegionLocator
    val it = res.iterator()
    var count = 0
    while (it.hasNext) {
      count += 1
      val cells: Result = it.next()
      for (kv <- cells.rawCells()) {
        println(kv + "||" + new String(kv.getRow) + "--" + new String(kv.getFamily) + "--" + kv.getRowLength + "**" +
          new String(kv.getQualifier) + "--" + new String(kv.getValue) + "--" + kv.getTimestamp)
        println("---------------------")
      }
    }
    println("总共有约：" + count + " 行数据！")
  }

  def getRegion(name: String): Unit = {
    val admin = conn.getAdmin
    val tableName = TableName.valueOf(name)
    val regions = admin.getTableRegions(tableName)
    println("region的个数：" + regions.size())
    val it = regions.iterator()
    //
    while (it.hasNext) {
      val region = it.next()
      println(region.getRegionNameAsString + "--" + new String(region.getStartKey) + "--" + new String(region.getEndKey))
    }
    admin.close()
  }

  def createTable(): Unit = {
    val admin = conn.getAdmin
    val tableName = TableName.valueOf("create_from_IDEA")
    if (admin.tableExists(tableName)) {
      admin.disableTable(tableName); admin.deleteTable(tableName); println(tableName + "已经存在，要删除。。")
    }
    else {

      val tableDesc = new HTableDescriptor(tableName)
      tableDesc.addFamily(new HColumnDescriptor("cf1"))
      admin.createTable(tableDesc, "1".getBytes, "100".getBytes, 5)
      println("创建表完毕！！")
      admin.close()
      //tableDesc.setRegionReplication(3)
    }
  }

  def main(args: Array[String]): Unit = {
    val tableName = "create_from_IDEA";
    //HbaseTest.getNameSpace()
    //HbaseTest.scanTable()
    HbaseTest.getRegion(tableName)
    //this.createTable()

  }
}
