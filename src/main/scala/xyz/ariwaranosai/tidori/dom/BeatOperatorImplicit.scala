package xyz.ariwaranosai.tidori.dom

import BeatDomOperator._
import DomOperator._
import org.scalajs.dom.Element

/**
  * Created by ariwaranosai on 2017/1/18.
  *
  */

object BeatOperatorImplicit {
  implicit class StrToBeatOperatorOps(s: String) {
    def toB(time: Double = 1): BeatDomOperator[Unit] =
      appendStr(s, time)

    def b: BeatDomOperator[Unit] =
      appendStr(s)

    def toDB(time: Double = 1): BeatDomOperator[Unit] =
      sequence(s.map(x => appendStr(x.toString, time)))

    def bb: BeatDomOperator[Unit] =
      sequence(s.map(x => appendStr(x.toString)))
  }

  implicit class NodeToBeatOperatorOps(node: Element) {
    def toB(time: Double = 1): BeatDomOperator[Unit] =
      appendNode(node, time)

    def b: BeatDomOperator[Unit] =
      appendNode(node)

    def toBwithS(s: String, time: Double = 1): BeatDomOperator[Unit] =
      subContext(node, time, s.b)

    def toBBwithS(s: String, time: Double = 1): BeatDomOperator[Unit] =
      subContext(node, time, s.bb)

    def bbs(s: String): BeatDomOperator[Unit] =
      toBBwithS(s)

    def bs(s: String): BeatDomOperator[Unit] =
      toBwithS(s)

  }

  implicit class InActionOps(n: Int) {
    def del(time: Double = 1): BeatDomOperator[Unit] =
      repeat(removeLast(time), n)

    def delay: BeatDomOperator[Unit] =
      DomOperator.delay(n)
  }
}
