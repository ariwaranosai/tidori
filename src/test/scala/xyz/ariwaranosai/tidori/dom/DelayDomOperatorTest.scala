package xyz.ariwaranosai.tidori.dom

import utest.TestSuite
import org.scalajs.dom
import org.scalajs.dom.Element
import xyz.ariwaranosai.tidori.dom.DomElements.htmlBR
import BeatDomOperator._
import BeatOperatorImplicit._
import DomOperator._

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
      val append = "1234".bb

      val t = OperatorContext(p, 10)

      for {
        v <- append.run(t)
      } yield assert(v._2.node.innerHTML == "1234")
    }

    "removeLast" - {
      val p = dom.document.createElement("p")
      val append = "1234".bb
      val t = OperatorContext(p, 10)

      for {
        v <- (append ~: removeLast()).run(t)
      } yield assert(v._2.node.innerHTML == "123")
    }

    "setContent" - {
      val p = dom.document.createElement("p")
      val set = setContext("abcd")
      val t = OperatorContext(p, 10)

      for {
        v <- set.run(t)
      } yield assert(v._2.node.innerHTML == "abcd")
    }

    "appendNode" - {
      val p = dom.document.createElement("p")
      val append = appendNode(htmlBR)
      val t = OperatorContext(p, 10)

      for {
        v <- append.run(t)
      } yield assert(v._2.node.innerHTML == "<br>")
    }

    "repeatOp" - {
      val p = dom.document.createElement("p")
      def append() = htmlBR.b

      val t = OperatorContext(p, 10)

      for {
        v <- repeatOp(append, 3).run(t)
      } yield assert(v._2.node.innerHTML == "<br><br><br>")
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
