package org.lee

import akka.stream.alpakka.slick.javadsl.SlickSession

object Main {
  def main(args: Array[String]): Unit = {
    print("xxxx")
  }

  def a(): SlickSession = {
    SlickSession.forConfig("slick-mysql")
  }
}
