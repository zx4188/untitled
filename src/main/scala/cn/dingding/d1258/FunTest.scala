package cn.dingding.d1258

/**
  * Created by zhangxu 13:49 2017/11/14
  * Description 
  */
object FunTest {
  def getSite(s1: String, s2: String): List[Int] = {
    var site: List[Int] = List.empty
    for (i <- 0 until s1.length) {
      //if(s1(i)=='$') site=site:+FindSite(s2,transforKey(s1,i+1))
    }
    site
  }

  def FindSite(s1: String, keyReduce: String): Int = {
    var k, m: Int = 0
    var n = 1
    while ((k < s1.length) && (n != m)) {
      if (s1(k) == 'a') {
        m = m + 1
        var s = ""
        for (j <- 1 to keyReduce.length) if (k + j < s1.length) s = s + s1(k + j).toString
        if (s == keyReduce) n = m
      }
      k = k + 1
    }
    n
  }

  def transferKey(s: String, n: Int): String = {
    var i = n
    var result = ""
    while ((s(i) != 'l') && (i < s.length - 1)) {
      result = result + s(i).toString
      i = i + 1
    }
    if (i == s.length - 1) result = result + s(i)
    result
  }

  def getTimes(s: String, k: Char): Int = {
    var time = 0
    s.foreach(s => if (s == k) time = time + 1)
    time
  }

  def reduceKey(s1: String, site: List[Int]): String = {
    var k = 0
    var s: String = ""
    for (i <- 0 until s1.length - 1) {
      k = k + 1
      if (!site.contains(k)) s = s + "b" + transferKey(s1, i + 1)
    }

    s
  }

  def getKey(s1: String, site: List[Int], t: Boolean = true): String = {
    var s = ""
    site.foreach(x => if (x > 0) {
      var k = 0
      for (i <- 0 until s1.length) {
        if (s1(i) == 'c') {
          k = k + 1
          if (k == x) {
            if (t) s = s + 'd' + transferKey(s1, i + 1) else s = s + transferKey(s1, i + 1)
          }

        }
      }
    })
    s
  }

  def main(args: Array[String]): Unit = {
    var argss = Array("0","1","2")

    val inputpath=argss.map(x=>"BizvaneTemp"+x+"Bizvane.kCORP_ID.kSTORE_ID.kEMP_ID.kVIP_ID.kORDER_ID.kSKU_ID.kT_BL_Y.kT_BL_M.kT_BL_W.kT_BL_D.c.g.kORDER_ID.n.e.empty.c.vNUM_SALES.vAMT_TRADE.vAMT_SUG")
    if (inputpath.length==0) {println("Error Input");sys.exit(1)}
    //    val space=new Space("TempCal","")
    println("All input paths is: "+inputpath.length)
    inputpath.foreach(println(_))

    println(inputpath(0)+"****")
    //    val first= TempHbase(inputpath(0),"™CORP_ID™STORE_ID™EMP_ID™VIP_ID™ORDER_ID™SKU_ID™T_BL_Y™T_BL_M™T_BL_W™T_BL_D",Vector("NUM_SALES", "AMT_TRADE", "AMT_SUG"),space)
    //    var Data:Vector[DataUnit]=Vector.empty
    var Data = "--";
    val n=inputpath.length
    val Temp_retail=if (n>1) {
      inputpath.foreach(x=>Data=Data+x)
      //      UnionAll(Data,n-1)
    } else ""

    inputpath.foreach(println(_))
    System.out.println(argss.getClass+"--"+inputpath.getClass+"---"+Temp_retail.toString())

    //System.out.print(FunTest.transferKey("Helloa", 2))
  }
}
