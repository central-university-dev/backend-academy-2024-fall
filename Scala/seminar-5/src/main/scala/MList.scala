sealed trait MList[+A] {
  def head: A

  def tail: MList[A]

  def isEmpty: Boolean

  def size: Int

  def apply(i: Int): A

  def appended[B >: A](elem: B): MList[B]

  def prepended[B >: A](elem: B): MList[B]

  def map[B](f: A => B): MList[B]

  def flatMap[B](f: A => MList[B]): MList[B]

  def foldLeft[B](z: B)(op: (B, A) => B): B

  // advanced

  def foreach(f: A => Unit): MList[A]

  def foldRight[B](z: B)(op: (A, B) => B): B

  def reduceLeft[B >: A](op: (B, A) => B): B

  def reduceRight[B >: A](op: (A, B) => B): B

  def take(n: Int): MList[A]

  def drop(n: Int): MList[A]

  def reverse: MList[A]
}

object MList {
  def apply[A](elems: A*): MList[A] =
    elems.foldRight[MList[A]](MNil)(Cons.apply)

  case object MNil extends MList[Nothing] {
    override def head: Nothing = throw new UnsupportedOperationException("MList.head of MNil")

    override def tail: MList[Nothing] = throw new UnsupportedOperationException("MList.tail of MNil")

    override def isEmpty: Boolean = true

    override def size: Int = 0

    override def apply(i: Int): Nothing = throw new UnsupportedOperationException("MList.tail of MNil")

    override def appended[B >: Nothing](elem: B): MList[B] = Cons(elem, this)

    override def prepended[B >: Nothing](elem: B): MList[B] = Cons(elem, this)

    override def take(n: Int): MList[Nothing] = this

    override def drop(n: Int): MList[Nothing] = this

    override def map[B](f: Nothing => B): MList[B] = this

    override def foreach(f: Nothing => Unit): MList[Nothing] = this

    override def flatMap[B](f: Nothing => MList[B]): MList[B] = this

    override def reduceLeft[B >: Nothing](op: (B, Nothing) => B): B = {
      throw new UnsupportedOperationException("MList.reduceLift of MNil")
    }

    override def reduceRight[B >: Nothing](op: (Nothing, B) => B): B = {
      throw new UnsupportedOperationException("MList.reduceRight of MNil")
    }

    override def foldLeft[B](z: B)(op: (B, Nothing) => B): B = z

    override def foldRight[B](z: B)(op: (Nothing, B) => B): B = z

    override def reverse: MList[Nothing] = this
  }

  final case class Cons[+A](override val head: A, override val tail: MList[A]) extends MList[A] {
    override def isEmpty: Boolean = false

    override def size: Int = foldLeft(0)((acc, _) => acc + 1)

    override def apply(i: Int): A = drop(i).head

    override def appended[B >: A](elem: B): MList[B] = foldRight(MNil.appended(elem))(Cons.apply)

    override def prepended[B >: A](elem: B): MList[B] = Cons(elem, this)

    override def take(n: Int): MList[A] = {
      def loop(res: MList[A], acc: MList[A], rest: Int): MList[A] =
        acc match {
          case Cons(_, _) if rest == 0 => res.reverse
          case Cons(head, tail) => loop(Cons(head, res), tail, rest - 1)
          case MNil => MNil
        }

      loop(MNil, this, n)
    }

    override def drop(n: Int): MList[A] = {
      def loop(res: MList[A], rest: Int): MList[A] =
        res match {
          case _ if rest == 0 => res
          case Cons(_, tail) => loop(tail, rest - 1)
          case MNil => MNil
        }

      loop(this, n)
    }

    override def map[B](f: A => B): MList[B] =
      flatMap(a => Cons(f(a), MNil))

    override def foreach(f: A => Unit): MList[A] = {
      map(f)
      this
    }

    override def flatMap[B](f: A => MList[B]): MList[B] = {
      def concat(l1: MList[B], l2: MList[B]): MList[B] =
        l1.foldRight(l2)(Cons.apply)

      concat(f(head), tail.flatMap(f))
    }

    override def reduceLeft[B >: A](op: (B, A) => B): B =
      tail.foldLeft[B](head)(op(_, _))

    override def reduceRight[B >: A](op: (A, B) => B): B = {
      val lastIndex = size - 1
      val init = take(lastIndex)
      val last = drop(lastIndex).head

      init.foldRight[B](last)(op(_, _))
    }

    override def foldLeft[B](z: B)(op: (B, A) => B): B = {
      def loop(res: B, rest: MList[A]): B =
        rest match {
          case MNil => res
          case Cons(head, tail) => loop(op(res, head), tail)
        }

      loop(z, this)
    }

    //    Non-tailrec
    //    override def foldRight[B](z: B)(op: (A, B) => B): B = {
    //      def loop(rest: MList[A]): B =
    //        rest match {
    //          case MNil => z
    //          case Cons(head, tail) => op(head, loop(tail))
    //        }
    //
    //      loop(this)
    //    }

    override def foldRight[B](z: B)(op: (A, B) => B): B =
      reverse.foldLeft(z)((b, a) => op(a, b))

    override def reverse: MList[A] = {
      def loop(res: MList[A], rest: MList[A]): MList[A] =
        rest match {
          case MNil => res
          case Cons(head, tail) => loop(Cons(head, res), tail)
        }

      loop(MNil, this)
    }
  }
}
