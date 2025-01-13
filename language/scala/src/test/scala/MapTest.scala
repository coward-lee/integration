import org.junit.jupiter.api.Test

class MapTest {

  // 对 map中的 k->v 中的v 进行转化
//  @Test
//  def test_simple : Unit = {
//    val m = Map("red" -> "r", "green" -> "xxx")
//    val value1 = m.map(x => {
//      x
//    })
//    println(value1.getClass.toString )
//
//    value1.iterator.flatMap(_._1).foreach(println(_))
//  }

  @Test
  def test_flatMap : Unit = {
    val m = Map("red" -> List("a","b"), "green" -> List("a","c"))
    m.flatMap(li => li._2).foreach(l=>println(l.getClass.getSimpleName))
  }
}
