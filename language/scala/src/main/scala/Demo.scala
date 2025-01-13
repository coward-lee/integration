class Demo {

}

object Demo {

  def main(args: Array[String]): Unit = {

    // 20200101
    val base = raw"(\d{8})".r
    // 2020-01-01
    val local = raw"(\d{4}-\d{1,2}-\d{1,2})".r
    // 2020-01-01 *
    val localDateTime = raw"(\d{4}-\d{1,2}-\d{1,2}) .*".r
//    println(base())
    println(local)
    println(localDateTime)
  }
}
