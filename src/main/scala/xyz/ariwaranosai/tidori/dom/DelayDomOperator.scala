package xyz.ariwaranosai.tidori.dom

import org.scalajs.dom.html.Div
import org.scalajs.dom
import org.scalajs.dom.Node



/**
  * Created by ariwaranosai on 2017/1/7.
  *
  */

class DelayDomOperator(val f: Node => Unit, val time: Double) { self =>
  def run(div: Node): Unit = {
    dom.window.setTimeout(() =>
      f(div)
    , time)
  }

  def before(op: DelayDomOperator): DelayDomOperator =
    new DelayDomOperator((node: Node) => {
      op.f(node)
      self.run(node)
    }, op.time)

  def after(op: DelayDomOperator): DelayDomOperator =
    new DelayDomOperator((node: Node) => {
      self.f(node)
      op.run(node)
    }, self.time)

  def ~> = after _


  def ~:(op: DelayDomOperator): DelayDomOperator =
    before(op)
}

object DelayDomOperator {
  def apply(f: Node => Unit, time: Double): DelayDomOperator = new DelayDomOperator(f, time)

  def delay(time: Double) = DelayDomOperator(_ => (), time)
  def setContent(text: String, time: Double) =
    DelayDomOperator((div: Node) => {
      div.textContent = text
    }, time)

  def append(text: String, time: Double) =
    DelayDomOperator((div: Node) => {
      div.textContent = div.textContent + text
    }, time)

  def removeLast(time: Double) = DelayDomOperator((node: Node) => {
    node.textContent = node.textContent.substring(0, node.textContent.length - 1)
  }, time)
}