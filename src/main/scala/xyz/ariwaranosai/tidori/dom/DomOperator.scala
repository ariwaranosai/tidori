package xyz.ariwaranosai.tidori.dom


import BeatDomOperator._
import org.scalajs.dom
import org.scalajs.dom.Element
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ariwaranosai on 2017/1/18.
  *
  */

object DomOperator {
  def appendStr(s: String, t: Double = 1): BeatDomOperator[Unit] =
    BeatDomOperator(t,
      c => {
        c.node.appendChild(dom.document.createTextNode(s))
        Future(((), c))
      }
    )

  def appendNode(n: Element, t: Double = 1): BeatDomOperator[Unit] =
    BeatDomOperator(t,
      c => {
        c.node.appendChild(n)
        Future(((), c))
      })

  def subContext[A](dom: Element, time: Double, o: BeatDomOperator[A]): BeatDomOperator[A] =
    for {
      c <- getC
      _ <- setC(OperatorContext(dom, c.delta * time))
      v <- o
      _ <- setC(c)
    } yield v

  def subElement[A](dom: Element, o: BeatDomOperator[A]): BeatDomOperator[A] =
    subContext(dom, 1, o)

  def subSpeed[A](speed: Double, o: BeatDomOperator[A]): BeatDomOperator[A] =
    for {
      t <- getC
      _ <- setC(OperatorContext(t.node, t.delta * speed))
      v <- o
      _ <- setC(t)
    } yield v

  def delay(t: Double = 1): BeatDomOperator[Unit] = BeatDomOperator(t,
    c => Future(((), c)))

  def speed(t: Double = 1) = BeatDomOperator(0,
    c => Future(((), OperatorContext(c.node, c.delta * t))))

  def setContext(text: String, t: Double = 1) = BeatDomOperator(t,
    c => {
      c.node.innerHTML = text
      Future(((), c))
    })

  def removeLast(t: Double = 1) = BeatDomOperator(t,
    c => {
      val length = c.node.innerHTML.length
      c.node.innerHTML = c.node.innerHTML.substring(0, length - 1)
      Future(((), c))
    })

  def removeLastN(n: Int, t: Double = 1) = BeatDomOperator(t,
    c => {
      val length = c.node.innerHTML.length
      c.node.innerHTML = c.node.innerHTML.substring(0, length - n)
      Future(((), c))
    })

  def repeat[A](op: BeatDomOperator[A], n: Int): BeatDomOperator[A] =
    sequence(List.fill(n)(op))

  def repeatOp[A](fun: () => BeatDomOperator[A], n: Int): BeatDomOperator[A] =
    sequence(List.fill(n)(fun()))

  def repeat[A](zero: BeatDomOperator[A], fun: A => BeatDomOperator[A], n: Int): BeatDomOperator[A] =
    (0 to n).foldRight(zero)((_, c) => for {
      t <- c
      v <- fun(t)
    } yield v)
}
