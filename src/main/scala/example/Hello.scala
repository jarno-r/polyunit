package example

import polyunit._
import polyunit.implicits._
import cats.implicits._

object Hello {
  def main(args : Array[String]): Unit = {
    println("Hello")

    val p : PolyUnit[Int] = ()
    val q : PolyUnit[String] = <<>>

    p.flatMap(a => <<>>)

    val l = List(1,2,3,4,5).map(i => PolyUnit(i))
    val t = l.traverse(identity)
    println(t)

    println((PolyUnit(1), PolyUnit(2)).mapN(_ |+| _))
  }
}
