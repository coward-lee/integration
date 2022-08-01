class MultiStateMain {

  def test(): Unit ={
    new MultiState("", "")
    new MultiState("", "")
    new MultiState("", "", "null")
    new MultiState("", "", 1)
  }
}

object MultiStateMain {
  def main(args: Array[String]): Unit = {
  }
}
