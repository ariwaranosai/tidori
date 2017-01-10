package xyz.ariwaranosai.tidori.dom

import utest.TestSuite
import org.scalajs.dom
import org.scalajs.dom.Element
import xyz.ariwaranosai.tidori.dom.DelayDomImplicit._
import scala.concurrent.ExecutionContext.Implicits.global
import utest._

import scala.concurrent.Future

/**
  * Created by ariwaranosai on 2017/1/10.
  *
  */

object DelayDomOperatorTest extends TestSuite {

  val tests = this {
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
  }

  tests.runAsync().map { results =>
    assert(results.toSeq.head.value.isSuccess)
  }
}
