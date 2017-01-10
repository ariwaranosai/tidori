package xyz.ariwaranosai.tidori.render

import org.scalajs.dom.Node
import xyz.ariwaranosai.tidori.dom.{OperatorContext, DelayDomOperator => Dop}
import xyz.ariwaranosai.tidori.dom.DelayDomImplicit._

/**
  * Created by ariwaranosai on 2017/1/9.
  *
  */
/*
object DomOp {
  implicit def domOpInstance(implicit e: OperatorContext) = new ElementAlg[Dop] {
    override def lit(x: String): Dop = x.toDDop(1)
    override def lit(x: Node): Dop = x.toDop(1)
    override def repeat(op: Dop, time: Int): Dop =
      Dop.sequence(List.fill(time)(op))
    override def speedChange(op: Dop, n: Double): Dop = ???
    override def replace(t1: String, t2: String, time: Double): Dop = ???
    override def append(op1: Dop, op2: Dop): Dop = ???
  }
}
*/
