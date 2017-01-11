package xyz.ariwaranosai.tidori.dom
import org.scalajs.dom

import scala.concurrent.{Future, Promise}
import org.scalajs.dom.Element

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
  def appendNode(node: Element, t: Double = 1) =
    new DelayDomOperator {
      override val time: Double = t
      override val op: (OperatorContext) => Future[OperatorContext] =
        (c: OperatorContext) => {
          c.node.appendChild(node)
          Future(c)
        }
    }

  def appendStr(s: String, t: Double = 1) =
    new DelayDomOperator {
      override val time: Double = t
      override val op: (OperatorContext) => Future[OperatorContext] =
        (c: OperatorContext) => {
          c.node.innerHTML = c.node.innerHTML + s
          Future(c)
        }
    }

  def delay(t: Double = 1) = new DelayDomOperator {
    override val time: Double = t
    override val op: (OperatorContext) => Future[OperatorContext] =
      (c: OperatorContext) => Future(c)
  }

  def speed(t: Double = 1) = new DelayDomOperator {
    override val time: Double = 0
    override val op: (OperatorContext) => Future[OperatorContext] = {
      (c: OperatorContext) =>
        Future(new OperatorContext {
          override val node: Element = c.node
          override val delta: Double = c.delta * t
        })
    }
  }

  def setContent(text: String, t: Double = 1) = new DelayDomOperator {
    override val time: Double = t
    override val op: (OperatorContext) => Future[OperatorContext] =
      (c: OperatorContext) => {
        c.node.innerHTML = text
        Future(c)
      }
  }

  def sequence(l: Seq[DelayDomOperator]): DelayDomOperator = {
    l.foldRight(delay(0))((d, r) => d ~: r)
  }

  def removeLast(t: Double = 1) = new DelayDomOperator {
    override val op: (OperatorContext) => Future[OperatorContext] =
      (c: OperatorContext) => {
        c.node.innerHTML = c.node.innerHTML.substring(0, c.node.innerHTML.length - 1)
        Future(c)
      }
    override val time: Double = t
  }

  def removeLastN(n: Int, t: Double = 1): DelayDomOperator = repeat(removeLast(t), n)

  def repeat(op: DelayDomOperator, n: Int): DelayDomOperator =
    sequence(List.fill(n)(op))

  def repeatOp(fun: () => DelayDomOperator, n: Int): DelayDomOperator =
    sequence(List.fill(n)(fun()))
}

object DelayDomImplicit {
  implicit class SToDelayOperatorOps(s: String) {
    def toDop(time: Double = 1): DelayDomOperator =
      DelayDomOperator.appendStr(s, time)

    def toDDop(time: Double = 1): DelayDomOperator =
      DelayDomOperator.sequence(s.map(x =>
        DelayDomOperator.appendStr(x.toString, time)))
  }

  implicit class NToDelayOperatorOps(node: Element) {
    def toDop(time: Double = 1): DelayDomOperator =
      DelayDomOperator.appendNode(node, time)
  }
}
