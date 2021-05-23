package polyunit

import scala.language.implicitConversions

object implicits {
  implicit def convertUnitToPolyUnit[A](u : Unit) : PolyUnit[A] = {
    val _ = u
    <<>>
  }
}
