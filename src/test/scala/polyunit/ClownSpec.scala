package polyunit


import cats.Eq
import cats.implicits._
import cats.laws.discipline.{ApplicativeTests, MonadTests}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.typelevel.discipline.scalatest.FunSuiteDiscipline
import org.scalacheck.ScalacheckShapeless._

class ClownSpec extends AnyFunSuiteLike with Matchers with ScalaCheckDrivenPropertyChecks with FunSuiteDiscipline {

  implicit def eqClown[A: Eq]: Eq[Clown[A]] = Eq.fromUniversalEquals

  checkAll("Clown.Applicative",ApplicativeTests[Clown].applicative[Int, Double, String])
}
