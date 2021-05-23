# polyunit
An effectful Unit type for Scala & Cats.

The polymorphic `PolyUnit[A]` is a type similar to the builtin `Unit`
in that it only contains one value, `<<>>` ,which is analogous to the builtin `()`.

`PolyUnit[_]` is a Cats `Monad`, `Monoid` and `Parallel`. It can be used in
many creative ways.

```scala
import cats._
import cats.implicits._
import polyunit._
import polyunit.implicits._

// Convert 1 and 2 to PolyUnit[int] and parMapN over them to get PolyUnit[Double].
def computeRatio(): PolyUnit[Double] = {
  (1.unit, 2.unit).parMapN((a, b) => a.toDouble / b)
}

// Use for-comprehension on PolyUnit.
def loop() : PolyUnit[List[Int]] = {
  for {
    i <- List(1, 2, 3).traverse(i => PolyUnit(i))
    j <- List(4, 5, 6).traverse(i => PolyUnit(i))
  } yield i++j
}
```
