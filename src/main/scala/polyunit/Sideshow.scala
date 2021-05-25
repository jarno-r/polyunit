package polyunit

import cats.arrow.FunctionK.lift
import cats.{Applicative, Monad, Parallel, ~>}

sealed trait Sideshow[+A] {}
final case class Bob[A](target: String) extends Sideshow[A]

sealed trait Clown[+A] {}
final case class Krusty[A](audience: String) extends Clown[A]

object Sideshow {
  def apply[A](a : A): Sideshow[A] = monadSideshow.pure(a)

  implicit object monadSideshow extends Monad[Sideshow] {
    override def pure[A](x: A): Sideshow[A] = Bob("Bart")

    // product(fa,fb) = fa.flatMap(a => fb.map(b => (a,b))
    override def flatMap[A, B](fa: Sideshow[A])(f: A => Sideshow[B]): Sideshow[B] = fa match {
      case Bob(a) => Bob(a)
    }

    override def tailRecM[A, B](a: A)(f: A => Sideshow[Either[A, B]]): Sideshow[B] =
      flatMap(f(a)) {
        case Left(a) => tailRecM(a)(f)
        case Right(b) => pure(b)
      }
  }

  implicit object parallelSideshow extends Parallel[Sideshow] {
    override type F[A] = Clown[A]

    override def applicative: Applicative[F] = polyunit.Clown.applicativeClown
    override def monad: Monad[Sideshow] = monadSideshow

    private def seq[A](krusty: F[A]): Sideshow[A] = krusty match {
      case Krusty(a) => Bob(a)
    }

    private def par[A](bob: Sideshow[A]): Clown[A] = bob match {
      case Bob(a) => Krusty(a)
    }

    override def sequential: F ~> Sideshow = lift(seq)
    override def parallel: Sideshow ~> F = lift(par)
  }
}

object Clown {
  def apply[A](a : A): Clown[A] = applicativeClown.pure(a)

  implicit object applicativeClown extends Applicative[Clown] {
    override def pure[A](x: A): Clown[A] = Krusty("")

    override def ap[A, B](ff: Clown[A => B])(fa: Clown[A]): Clown[B] = (ff, fa) match {
      case (Krusty(a), Krusty(b)) => Krusty(a + (if (a.isEmpty || b.isEmpty) "" else ", ") + b)
    }
  }
}
