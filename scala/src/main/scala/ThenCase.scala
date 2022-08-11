import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class ThenCase {

}
object ThenCase{
  def main(args: Array[String]): Unit = {
    Future.successful("xxxxx").andThen {
      case Failure(exception) => println("成功了")
      case Success(value) => println("失败了")
    }
  }
}
