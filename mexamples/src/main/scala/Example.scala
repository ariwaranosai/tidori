import scala.scalajs.js.JSApp
import org.scalajs.dom
import xyz.ariwaranosai.tidori.dom.DomElements._
import xyz.ariwaranosai.tidori.dom.OperatorContext

import scalatags.JsDom.all._
import xyz.ariwaranosai.tidori.dom.DomOperator._
import xyz.ariwaranosai.tidori.dom.BeatOperatorImplicit._
import xyz.ariwaranosai.tidori.macros.beat

/**
  * Created by ariwaranosai on 2017/1/8.
  *
  */

object Example extends JSApp {
  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    println("hello world!")
    val pNode = dom.document.getElementById("broad")
    val c = OperatorContext(pNode, 200)

    @beat
    val blog = <a id="123" hh="keyi">hehe</a>
    //a(href:="http://ariwaranosai.xyz").render.bbs("blog")

    blog.run(c)
  }
}
