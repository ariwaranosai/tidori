import scala.scalajs.js.JSApp
import org.scalajs.dom._
import org.scalajs.dom
import xyz.ariwaranosai.tidori.dom.DelayDomOperator._
import xyz.ariwaranosai.tidori.dom.DomElements._
import xyz.ariwaranosai.tidori.dom.OperatorContext
import xyz.ariwaranosai.tidori.dom.DelayDomImplicit._

/**
  * Created by ariwaranosai on 2017/1/8.
  *
  */

object Example extends JSApp {
  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    println("hello world!")
    val fNode = dom.document.createElement("p")
    fNode.appendChild(htmlBR)
    fNode.appendChild(htmlBR)
    fNode.appendChild(htmlBR)
    dom.document.body.appendChild(fNode)
    println(fNode.innerHTML)

    val pNode = dom.document.getElementById("typed")
    val str = "1234567890"

    implicit object T extends OperatorContext {
      override val node: Element = pNode
      override val delta: Double = 1000
    }

    //val a = str.map(x => appendStr(x.toString, 1))
    //val t = sequence(a)
    // val t = a(0) ~: a(1) ~: a(2) ~: a(3) ~: a(4)
    //val t = a1 ~: a2 ~: a3 ~: a4 ~: a5
    //val t = append("1", 1000) after (append("2", 1000) after (append("3", 1000) after append("4", 1000)))
    //val t = append("1", 1) ~: append("2", 1) ~: speed(0.5) ~: append("3", 2) ~: append("4", 2) ~: speed(2) ~: append("5", 1) ~: append("6", 1)
    //dom.window.setInterval(() => println("--"), 1000)
    //val nums = "123456".toDDop()
    //val alphabet = "abcdef".toDDop()
    def br() = htmlBR.toDop()
    val delete = repeatOp(br, 5)
    delete.run

    //val p = nums ~: delete ~: br ~: alphabet

    //p.run
  }
}
