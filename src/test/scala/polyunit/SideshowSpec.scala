package polyunit


import cats.Eq
import cats.implicits._
import cats.laws.discipline.MonadTests
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.typelevel.discipline.scalatest.FunSuiteDiscipline
import org.scalacheck.ScalacheckShapeless._

class SideshowSpec extends AnyFunSuiteLike with Matchers with ScalaCheckDrivenPropertyChecks with FunSuiteDiscipline {

  implicit def eqSideshow[A: Eq]: Eq[Sideshow[A]] = Eq.fromUniversalEquals

/*
  Shapeless automagically creates an Arbitrary.

  implicit def arbSideshow[A: Arbitrary]: Arbitrary[Sideshow[A]] =
    Arbitrary(Gen.alphaStr.map(Bob(_)))
*/

  checkAll("Sideshow.Monad",MonadTests[Sideshow].monad[Int, Double, String])

  test("Sideshow be a monad") {

  }
}
