package xyz.ariwaranosai.tidori.dom

import org.scalajs.dom.Node

/**
  * Created by ariwaranosai on 2017/1/9.
  *
  */

abstract class OperatorContext {
  val node: Node
  val delta: Double
}
