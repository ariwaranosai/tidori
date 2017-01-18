import scala.scalajs.js.JSApp
import org.scalajs.dom._
import org.scalajs.dom
import xyz.ariwaranosai.tidori.dom.DomElements._
import xyz.ariwaranosai.tidori.dom.{BeatDomOperator, OperatorContext}
import xyz.ariwaranosai.tidori.dom.BeatDomOperator._

import scalatags.JsDom.all._
import scala.concurrent.ExecutionContext.Implicits.global
import xyz.ariwaranosai.tidori.dom.DomOperator._
import xyz.ariwaranosai.tidori.dom.BeatOperatorImplicit._

import scala.concurrent.Future

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

    val twitter = a(href:="https://twitter.com/ariwaranosai").render.bs("twitter")
    val blog = a(href:="http://ariwaranosai.xyz").render.bs("blog")

    val title = "在原佐为-ありわら の さい ".bb ~: htmlBR.b ~:
      " 不入流的码农,不入流的失业党,不入流的死宅 ".bb ~: htmlBR.b ~:
      " 脑洞略大,盛产负能量,喜欢读书".bb ~: 4.del() ~: 1.delay ~:
      "常买书,不怎么读 ".bb ~: htmlBR.b ~:
      " 机器学习? 数据挖掘? 概率图模型? ".bb ~: htmlBR.b ~:
      " vim党 血崩党 冷场达人 玩3ds的任黑 ".bb ~: htmlBR.b ~:
      " 学haskell中途夭折,勉强写写Scala凑数,懒得时候写Python ".bb ~: htmlBR.b ~:
      " C++什么的逼急了写写,反正都不能帮我毕业 ".bb ~: htmlBR.b ~:
      " 审美差写不了前端,也不会写后台 ".bb ~: htmlBR.b ~:
      " OSX Fedora Arch Win HHKB ".bb ~: htmlBR.b ~:
      " 銀魂最高だ,周三就是该看金光布袋戏 ".bb ~: htmlBR.b ~:
      " 链接: ".bb ~: twitter ~: " ".bb ~: blog

    title.run(c)
  }
}
