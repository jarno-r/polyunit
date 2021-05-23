package polyunit

import cats.arrow.FunctionK.lift
import cats.{Applicative, Monad, Monoid, Parallel, ~>}

/**
 * Polymorphic Unit
 */
sealed trait PolyUnit[+A] { }
case object <<>> extends PolyUnit[Nothing]

object PolyUnit {
  def apply[A](a : A) : PolyUnit[A] = {
    val _ = a
    <<>>
  }

  implicit def monoidPolyUnit[A] : Monoid[PolyUnit[A]] = new Monoid[PolyUnit[A]] {
    override def empty: PolyUnit[A] = <<>>
    override def combine(x: PolyUnit[A], y: PolyUnit[A]): PolyUnit[A] = <<>>
  }

  implicit object monadPolyUnit extends Monad[PolyUnit] {
    override def pure[A](x: A): PolyUnit[A] = PolyUnit(x)

    override def flatMap[A, B](fa: PolyUnit[A])(f: A => PolyUnit[B]): PolyUnit[B] = {
      <<>>
    }

    /*
     * What the function does, but but with tail recursion:
     * flatMap(f(a)) {
     *   case Left(a) => tailRecM(a)(f)
     *   case Right(b) => pure(b)
     *  }
     */
    //@tailrec
    override def tailRecM[A, B](a: A)(f: A => PolyUnit[Either[A, B]]): PolyUnit[B] = <<>>
  }

  implicit object parallelPolyUnit extends Parallel[PolyUnit] {
    override def applicative: Applicative[PolyUnit] = implicitly
    override def monad: Monad[PolyUnit] = implicitly

    override type F[A] = PolyUnit[A]

    private def convert[A](p: PolyUnit[A]): PolyUnit[A] = p

    override def sequential: F ~> PolyUnit = lift(convert)
    override def parallel: PolyUnit ~> F = lift(convert)
  }
}
