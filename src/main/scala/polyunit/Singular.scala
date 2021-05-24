package polyunit

import cats.arrow.FunctionK.lift
import cats.implicits._
import cats.{Applicative, Monad, Parallel, Semigroup, ~>}

import scala.annotation.tailrec

/**
 * A polymorphic non-empty single item container.
 */
sealed trait Singular[+A] {}

final case class Value[+A](value : A) extends Singular[A]

object Singular {

  def apply[A](a: A): Singular[A] = Value(a)

  implicit def semigroupSingular[A: Semigroup]: Semigroup[Singular[A]] =
    (x: Singular[A], y: Singular[A]) => (x, y) match {
      case (Value(a), Value(b)) => Value(a |+| b)
    }

  implicit object monadSingular extends Monad[Singular] {
    override def pure[A](x: A): Singular[A] = Singular(x)

    override def flatMap[A, B](fa: Singular[A])(f: A => Singular[B]): Singular[B] = fa match {
      case Value(a) => f(a)
    }

    /*
     * What the function does, but with tail recursion:
     * flatMap(f(a)) {
     *   case Left(a) => tailRecM(a)(f)
     *   case Right(b) => pure(b)
     *  }
     */
    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Singular[Either[A, B]]): Singular[B] = f(a) match {
      case Value(Left(l)) => tailRecM(l)(f)
      case Value(Right(r)) => Value(r)
    }
  }

  implicit object parallelSingular extends Parallel[Singular] {
    override def applicative: Applicative[Singular] = implicitly

    override def monad: Monad[Singular] = implicitly

    override type F[A] = Singular[A]

    private def convert[A](p: Singular[A]): Singular[A] = p

    override def sequential: F ~> Singular = lift(convert)

    override def parallel: Singular ~> F = lift(convert)
  }
}