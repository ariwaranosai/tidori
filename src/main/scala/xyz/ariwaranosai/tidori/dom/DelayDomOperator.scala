package xyz.ariwaranosai.tidori.dom
import org.scalajs.dom

import scala.concurrent.{Future, Promise}
import org.scalajs.dom.Node

import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by ariwaranosai on 2017/1/7.
  *
  */

trait DelayDomOperator { self =>
  val op: OperatorContext => Future[OperatorContext]
  val time: Double

  def run(implicit m: OperatorContext): Future[OperatorContext] = {
    val p = Promise[OperatorContext]
    dom.window.setTimeout(() => {
      op(m).foreach(c => p.complete(Success(c)))
    }, time * m.delta)
    p.future
  }

  def insert(other: DelayDomOperator): DelayDomOperator =
    new DelayDomOperator {
      override val op: (OperatorContext) => Future[OperatorContext] =
        (c: OperatorContext) => {
          for {
            t <- other.op(c)
            v <- self.run(t)
          } yield v
        }

      override val time: Double = other.time
    }

  def after(other: DelayDomOperator): DelayDomOperator =
    new DelayDomOperator {
      override val time: Double = self.time
      override val op: (OperatorContext) => Future[OperatorContext] =
        (c: OperatorContext) => {
          for {
            t <- self.op(c)
            v <- other.run(t)
          } yield v
        }
    }

  def ~:(op: DelayDomOperator): DelayDomOperator =
    insert(op)
  def ~>(op: DelayDomOperator): DelayDomOperator =
    after(op)
}

object DelayDomOperator {
  def append(node: Node, t: Double) =
    new DelayDomOperator {
      override val time: Double = t
      override val op: (OperatorContext) => Future[OperatorContext] =
        (c: OperatorContext) => {
          c.node.appendChild(node)
          Future(c)
        }
    }

  def append(s: String, t: Double) =
    new DelayDomOperator {
      override val time: Double = t
      override val op: (OperatorContext) => Future[OperatorContext] =
        (c: OperatorContext) => {
          c.node.textContent = c.node.textContent + s
          Future(c)
        }
    }

  def delay(t: Double) = new DelayDomOperator {
    override val time: Double = t
    override val op: (OperatorContext) => Future[OperatorContext] =
      (c: OperatorContext) => Future(c)
  }

  def speed(t: Double) = new DelayDomOperator {
    override val time: Double = 0
    override val op: (OperatorContext) => Future[OperatorContext] = {
      (c: OperatorContext) =>
        Future(new OperatorContext {
          override val node: Node = c.node
          override val delta: Double = c.delta * t
        })
    }
  }

  def setContent(text: String, t: Double) = new DelayDomOperator {
    override val time: Double = t
    override val op: (OperatorContext) => Future[OperatorContext] =
      (c: OperatorContext) => {
        c.node.textContent = text
        Future(c)
      }
  }

  def sequence(l: Seq[DelayDomOperator]): DelayDomOperator = {
    l.foldRight(delay(0))((d, r) => d ~: r)
  }

  def removeLast(t: Double) = new DelayDomOperator {
    override val op: (OperatorContext) => Future[OperatorContext] =
      (c: OperatorContext) => {
        c.node.textContent = c.node.textContent.substring(0, c.node.textContent.length - 1)
        Future(c)
      }
    override val time: Double = t
  }

  def removeLastN(t: Double, n: Int) = repeat(removeLast(t), n)

  def repeat(op: DelayDomOperator, n: Int): DelayDomOperator =
    sequence(List.fill(n)(op))
}

object DelayDomImplicit {
  implicit class SToDelayOperatorOps(s: String) {
    def toDop(time: Double): DelayDomOperator =
      DelayDomOperator.append(s, time)

    def toDDop(time: Double): DelayDomOperator =
      DelayDomOperator.sequence(s.map(x =>
        DelayDomOperator.append(x.toString, time)))
  }

  implicit class NToDelayOperatorOps(node: Node) {
    def toDop(time: Double): DelayDomOperator =
      DelayDomOperator.append(node, time)
  }
}
