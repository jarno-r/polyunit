package polyunit

import cats.arrow.FunctionK.lift
import cats.implicits._
import cats.{Applicative, Eval, Monad, Parallel, Semigroup, Traverse, ~>}

import scala.annotation.tailrec

/**
 * A polymorphic non-empty single item container.
 */
sealed trait Singular[+A] {
  def get() : A
}

final case class Single[+A](value: A) extends Singular[A] {
  override def get(): A = value
}

object Singular {

  def apply[A](a: A): Singular[A] = Single(a)

  implicit def semigroupSingular[A: Semigroup]: Semigroup[Singular[A]] =
    (x: Singular[A], y: Singular[A]) => (x, y) match {
      case (Single(a), Single(b)) => Single(a |+| b)
    }

  implicit object monadSingular extends Monad[Singular] {
    override def pure[A](x: A): Singular[A] = Singular(x)

    override def flatMap[A, B](fa: Singular[A])(f: A => Singular[B]): Singular[B] = fa match {
      case Single(a) => f(a)
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
      case Single(Left(l)) => tailRecM(l)(f)
      case Single(Right(r)) => Single(r)
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

  implicit object singularTraverse extends Traverse[Singular] {
    type F[A] = Singular[A]

    override def traverse[G[_] : Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]] =
      f(fa.get).map(a => Single(a))

    override def foldLeft[A, B](fa: F[A], b: B)(f: (B, A) => B): B =
      f(b, fa.get)

    override def foldRight[A, B](fa: F[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.get, lb)
  }
}

