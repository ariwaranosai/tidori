package xyz.ariwaranosai.tidori.dom

import org.scalajs.dom.Element

/**
  * Created by ariwaranosai on 2017/1/9.
  *
  */

abstract class OperatorContext {
  val node: Element
  val delta: Double
}
