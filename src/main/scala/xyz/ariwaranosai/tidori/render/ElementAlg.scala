package xyz.ariwaranosai.tidori.render

import org.scalajs.dom.Node

/**
  * Created by ariwaranosai on 2017/1/9.
  *
  */

trait ElementAlg[T] {
  def lit(x: String): T
  def lit(x: Node): T
  def repeat(op: T, time: Int): T
  def speedChange(op: T, n: Double): T
  def replace(t1: String, t2: String, time: Double): T
  def append(op1: T, op2: T): T
}

object ElementAlg {
  implicit class ToElementAlgOps[T](s: T)(implicit e: ElementAlg[T]) {
    def repeat(t: Int): T = e.repeat(s, t)
    def append(op: T): T = e.append(s, op)
    def speedChange(n: Double): T = e.speedChange(s, n)
  }

  def replace[T](t1: String, t2: String, time: Double)(implicit e: ElementAlg[T]): T =
    e.replace(t1, t2, time)

  implicit class SToElementOps(s: String) {
    def lit[T](implicit e: ElementAlg[T]): T = e.lit(s)
    def replace[T](s1: String, time: Double)(implicit e: ElementAlg[T]): T = e.replace(s, s1, time)
  }

  implicit class NodeToElementOps(s: Node) {
    def lit[T](implicit e: ElementAlg[T]): T = e.lit(s)
  }

}
