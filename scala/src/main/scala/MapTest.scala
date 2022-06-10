object MapTest {

  // 对 map中的 k->v 中的v 进行转化
  def main(args: Array[String]): Unit = {
    val m = Map("red" -> "r", "green" -> "xxx")
//    val value:Map[String, String] = m.view.mapValues(s=>"color:"+s).toMap
//    println(value)
    val value1 = m.map(x => {
      x
    })
    println(value1.getClass.toString )
//    val list = value1.List.flatMap(_._1)

    value1.iterator.flatMap(_._1).foreach(println(_))

  }
}
