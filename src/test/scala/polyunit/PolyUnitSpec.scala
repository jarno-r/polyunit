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

class PolyUnitSpec extends AnyFunSuiteLike with Matchers with ScalaCheckDrivenPropertyChecks with FunSuiteDiscipline {

  implicit def eqPolyUnit[A: Eq]: Eq[PolyUnit[A]] = Eq.fromUniversalEquals

  checkAll("Sideshow.Monad",MonadTests[PolyUnit].monad[Int, Double, String])

  test("Some random test") {

  }
}
