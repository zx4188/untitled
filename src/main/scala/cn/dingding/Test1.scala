package cn.dingding

/**
  * Author zhangxu
  * create in 13:40 2017/11/14
  * Description
  */

object Test1{
    def main(args:Array[String]): Unit ={
      val list=List(1,3,5,7,9)
      val str="Congratulations"
      System.out.println(str(1))
      for(i<-0 until list.length) if(list(i)<5) System.out.println(list(i))

    }

}
