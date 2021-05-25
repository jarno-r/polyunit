package example

import cats.implicits._
import polyunit._
import polyunit.implicits._

object Hello {
  def main(args: Array[String]): Unit = {
    println("Hello")

    val p: PolyUnit[Int] = ()
    val q: PolyUnit[String] = <<>>

    p.flatMap(a => (a+1).unit)

    val l = List(1, 2, 3, 4, 5).map(i => PolyUnit(i))
    val t = l.traverse(identity)
    println(t)

    println((PolyUnit(1), PolyUnit(2)).mapN(_ |+| _))

    val _: List[PolyUnit[Int]] = List(1.unit, 2.unit, 3.unit)

    val s = 1.singular
    println(s)
    // conflict due to Monad & Traverse
    // println(s.map(i => i+1))

    circus()
  }

  // Convert 1f and 2 to PolyUnit[Float] and PolyUnit[int] and parMapN over them to get PolyUnit[Float].
  def computeRatio(): PolyUnit[Float] = {
    (1f.unit, 2.unit).parMapN((a, b) => a / b)
  }

  // Use for comprehension on PolyUnit.
  def loop() : PolyUnit[List[Int]] = {
    for {
      i <- List(1, 2, 3).traverse(i => PolyUnit(i))
      j <- List(4, 5, 6).traverse(i => PolyUnit(i))
    } yield i++j
  }


  def circus(): Unit =  {

    println("\n\n----- CIRCUS -----\n")

    val bob1 : Sideshow[Int] = Sideshow(123)
    val bob2 : Sideshow[Int] = Bob("Lisa")
    val bob3 = bob1.map(i => i+456).flatMap(i => bob2.map(j => i+j))

    val k1 : Clown[Int] = Krusty("Bart")
    val k2 : Clown[Int] = Krusty("Lisa")

    println(bob3)
    println(bob1.product(bob2))
    println(List(bob1, bob2).sequence)
    println(List(bob1, bob2).parSequence)
    println(k1.product(k2))
    println((Krusty("L"):Clown[Int => (Int => Int)]).ap(k1).ap(k2))
  }
}
