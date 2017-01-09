import scala.scalajs.js.JSApp

import org.scalajs.dom._
import org.scalajs.dom
import xyz.ariwaranosai.tidori.dom.DelayDomOperator._

/**
  * Created by ariwaranosai on 2017/1/8.
  *
  */

object Example extends JSApp {
  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    println("hello world!")

    val parNode = document.createElement("p")
    val textNode = document.createTextNode("start:")
    dom.document.body.appendChild(parNode)
    parNode.appendChild(textNode)

    val t = append("1", 3000) after (append("2", 3000) after (append("3", 3000) after append("4", 3000)))
    //val t = append("1", 3000) ~: delay(1000) ~: append("3", 3000) ~: append("4", 3000) ~:
    //  setContent("fuck", 5000) ~: removeLast(3000)


    dom.window.setInterval(() => println("--"),1000)

    t.run(textNode)
  }
}
