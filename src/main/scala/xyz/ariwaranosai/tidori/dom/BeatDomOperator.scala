package xyz.ariwaranosai.tidori.dom

import org.scalajs.dom
import org.scalajs.dom.Element
import xyz.ariwaranosai.tidori.dom.{OperatorContext => OpC}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Success

/**
  * Created by ariwaranosai on 2017/1/17.
  *
  */

case class BeatDomOperator[A](time: Double, op: OpC => Future[(A, OpC)]) { self =>
  def run(m: OpC): Future[(A, OpC)] = {
    val p = Promise[(A, OpC)]
    dom.window.setTimeout(() => {
      op(m).foreach(c => p.complete(Success(c)))
    }, time * m.delta)
    p.future
  }

  def flatMap[B](f: A => BeatDomOperator[B]): BeatDomOperator[B] =
    BeatDomOperator(self.time,
      (c: OpC) =>
        for {
          t <- self.op(c)
          v <- f(t._1).run(t._2)
        } yield v
    )

  def map[B](f: A => B): BeatDomOperator[B] =
    BeatDomOperator(self.time,
      (c: OpC) =>
        for {
          v <- op(c)
        } yield (f(v._1), v._2)
    )

  def insert[B](other: BeatDomOperator[B]): BeatDomOperator[A] =
    BeatDomOperator(other.time,
      (c: OpC) =>
        for {
          t <- other.op(c)
          v <- self.run(t._2)
        } yield v
    )

  def after[B](other: BeatDomOperator[B]): BeatDomOperator[B] =
    BeatDomOperator[B](self.time,
      (c: OpC) =>
        for {
          t <- self.op(c)
          v <- other.run(t._2)
        } yield v
    )

  def ~:[B](op: BeatDomOperator[B]): BeatDomOperator[A] =
    insert(op)
  def ~>[B](op: BeatDomOperator[B]): BeatDomOperator[B] =
    after(op)
}

object BeatDomOperator {
  val getC: BeatDomOperator[OpC] = BeatDomOperator[OpC](0,
      c => Future((c, c))
    )

  def setC(c: OpC): BeatDomOperator[Unit] = BeatDomOperator[Unit](0,
    _ => Future(((), c)))

  val log: BeatDomOperator[Unit] = BeatDomOperator[Unit](0,
    c => {
      println(
        s"""
           |node: ${c.node.id}
           |${c.node.innerHTML}
           |
           |delta: ${c.delta}
         """.stripMargin)
      Future(((), c))
    }
  )

  def sequence[A](l: Seq[BeatDomOperator[A]]): BeatDomOperator[A] =
    l.reduceRight((d, r) => d ~: r)

}

