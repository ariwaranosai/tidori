package xyz.ariwaranosai.tidori.dom

import utest.TestSuite
import org.scalajs.dom
import org.scalajs.dom.Element
import xyz.ariwaranosai.tidori.dom.DelayDomImplicit._
import xyz.ariwaranosai.tidori.dom.DelayDomOperator._
import xyz.ariwaranosai.tidori.dom.DomElements.htmlBR

import scala.concurrent.ExecutionContext.Implicits.global
import utest._
import utest.framework.{Test, Tree}


/**
  * Created by ariwaranosai on 2017/1/10.
  *
  */

object DelayDomOperatorTest extends TestSuite {

  val tests: Tree[Test] = this {
    "append" - {
      val p = dom.document.createElement("p")
      val append = "1234".toDDop()

      implicit object TT extends OperatorContext {
        override val node: Element = p
        override val delta: Double = 10
      }

      for {
        v <- append.run
      } yield assert(v.node.innerHTML == "1234")
    }

    "removeLast" - {
      val p = dom.document.createElement("p")
      val append = "1234".toDDop()
      implicit object TT extends OperatorContext {
        override val node: Element = p
        override val delta: Double = 10
      }

      for {
        v <- (append ~: removeLast()).run
      } yield assert(v.node.innerHTML == "123")
    }

    "setContent" - {
      val p = dom.document.createElement("p")
      val set = setContent("abcd")
      implicit object TT extends OperatorContext {
        override val node: Element = p
        override val delta: Double = 10
      }

      for {
        v <- set.run
      } yield assert(v.node.innerHTML == "abcd")
    }

    "appendNode" - {
      val p = dom.document.createElement("p")
      val append = appendNode(htmlBR)
      implicit object TT extends OperatorContext {
        override val node: Element = p
        override val delta: Double = 10
      }

      for {
        v <- append.run
      } yield assert(v.node.innerHTML == "<br>")
    }

    "repeatOp" - {
      val p = dom.document.createElement("p")
      def append() = htmlBR.toDop()

      implicit object TT extends OperatorContext {
        override val node: Element = p
        override val delta: Double = 10
      }

      for {
        v <- repeatOp(append, 3).run
      } yield assert(v.node.innerHTML == "<br><br><br>")
    }
  }

  tests.runAsync().map { results =>
    assert(results.toSeq.head.value.isSuccess)
    assert(results.toSeq(1).value.isSuccess)
    assert(results.toSeq(2).value.isSuccess)
    assert(results.toSeq(3).value.isSuccess)
    assert(results.toSeq(4).value.isSuccess)
    assert(results.toSeq(5).value.isSuccess)
  }
}
