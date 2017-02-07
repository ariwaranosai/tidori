package xyz.ariwaranosai.tidori.macros
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


/**
  * Created by ariwaranosai on 2017/2/7.
  *
  */

object test {
  def hello(s: String): Unit = macro helloImpl
  def helloImpl(c: blackbox.Context)(s: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._
    c.Expr(q"""println("hello " + ${s.tree} + "!")""")
  }
}
