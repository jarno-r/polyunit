package example

import cats.implicits._
import polyunit._
import polyunit.implicits._

object Hello {
  def main(args: Array[String]): Unit = {
    println("Hello")

    val p: PolyUnit[Int] = ()
    val q: PolyUnit[String] = <<>>

    p.flatMap(a => <<>>)

    val l = List(1, 2, 3, 4, 5).map(i => PolyUnit(i))
    val t = l.traverse(identity)
    println(t)

    println((PolyUnit(1), PolyUnit(2)).mapN(_ |+| _))

    val _: List[PolyUnit[Int]] = List(1.unit, 2.unit, 3.unit)
  }

  // Convert 1 and 2 to PolyUnit[int] and parMapN over them to get PolyUnit[Double].
  def computeRatio(): PolyUnit[Double] = {
    (1.unit, 2.unit).parMapN((a, b) => a.toDouble / b)
  }

  // Use for comprehension on PolyUnit.
  def loop() : PolyUnit[List[Int]] = {
    for {
      i <- List(1, 2, 3).traverse(i => PolyUnit(i))
      j <- List(4, 5, 6).traverse(i => PolyUnit(i))
    } yield i++j
  }
}
