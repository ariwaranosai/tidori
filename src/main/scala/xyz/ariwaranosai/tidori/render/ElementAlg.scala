package xyz.ariwaranosai.tidori.render

/**
  * Created by ariwaranosai on 2017/1/9.
  *
  */

trait ElementAlg[T] {
  def repeat(node: T, time: Int): T
  def SpeedChange(node: T, n: Double): T
  def delay(time: Double, node: T)
  def replace(node1: T, node2: T, time: Double): T
}
