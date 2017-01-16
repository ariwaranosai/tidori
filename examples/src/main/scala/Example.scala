import scala.scalajs.js.JSApp
import org.scalajs.dom._
import org.scalajs.dom
import xyz.ariwaranosai.tidori.dom.DomElements._
import xyz.ariwaranosai.tidori.dom.OperatorContext
import xyz.ariwaranosai.tidori.dom.DelayDomImplicit._
import scalatags.JsDom.all._

/**
  * Created by ariwaranosai on 2017/1/8.
  *
  */

object Example extends JSApp {
  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {
    println("hello world!")

    val pNode = dom.document.getElementById("broad")

    implicit object T extends OperatorContext {
      override val node: Element = pNode
      override val delta: Double = 178
    }

    val twitter = a(href:="https://twitter.com/ariwaranosai").render.ds("twitter")
    val blog = a(href:="http://ariwaranosai.xyz").render.ds("blog")

    val title = "在原佐为-ありわら の さい ".dd ~: htmlBR.d ~:
      " 不入流的码农,不入流的失业党,不入流的死宅 ".dd ~: htmlBR.d ~:
      " 脑洞略大,盛产负能量,喜欢读书".dd ~: 4.del() ~: 1.delay ~:
      "常买书,不怎么读 ".dd ~: htmlBR.d ~:
      " 机器学习? 数据挖掘? 概率图模型? ".dd ~: htmlBR.d ~:
      " vim党 血崩党 冷场达人 玩3ds的任黑 ".dd ~: htmlBR.d ~:
      " 学haskell中途夭折,勉强写写Scala凑数,懒得时候写Python ".dd ~: htmlBR.d ~:
      " C++什么的逼急了写写,反正都不能帮我毕业 ".dd ~: htmlBR.d ~:
      " 审美差写不了前端,也不会写后台 ".dd ~: htmlBR.d ~:
      " OSX Fedora Arch Win HHKB ".dd ~: htmlBR.d ~:
      " 銀魂最高だ,周三就是该看金光布袋戏 ".dd ~: htmlBR.d ~:
      " 链接: ".dd ~: twitter ~: " ".dd ~: blog

    title.run
  }
}
