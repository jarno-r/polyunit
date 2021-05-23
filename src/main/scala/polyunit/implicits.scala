package polyunit

import scala.language.implicitConversions

object implicits {
  implicit class PolyUnitConverted[A](a : A) {
    def unit : PolyUnit[A] = PolyUnit(a)
  }

  implicit def convertUnitToPolyUnit[A](u : Unit) : PolyUnit[A] = {
    val _ = u
    <<>>
  }
}
