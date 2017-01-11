package xyz.ariwaranosai.tidori.dom

import org.scalajs.dom.html.{BR, Span}

import scalatags.JsDom.all._

/**
  * Created by ariwaranosai on 2017/1/9.
  *
  */

object DomElements {
  val cursor: Span = span(`class`:="typed_cursor")("|").render
  def htmlBR: BR = br().render
}
