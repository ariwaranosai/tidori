package xyz.ariwaranosai.tidori.macros
import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.reflect.macros.whitebox


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

@compileTimeOnly("enable macro paradise to expand macro annotations")
class dom extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro XMLMacro.macroTransform
}


private[macros] class XMLMacro(val c: whitebox.Context) {
  import c.universe._

  private def replaceDefBody(annottees: Seq[c.universe.Tree], transformBody: Tree => Tree) = {
    val result = annottees match {
      case Seq(annottee@DefDef(mods, name, tparams, vparamss, tpt, rhs)) =>
        atPos(annottee.pos) {
          DefDef(mods, name, tparams, vparamss, tpt, transformBody(rhs))
        }
      case Seq(annottee@ValDef(mods, name, tpt, rhs)) =>
        atPos(annottee.pos) {
          ValDef(mods, name, tpt, transformBody(rhs))
        }
      case _ =>
        c.error(c.enclosingPosition, "Expect def or val")
        annottees.head
    }

    result
  }

  def macroTransform(annottees: Tree*): Tree = {
    replaceDefBody(annottees, _ => q"""
        println("hello world")
      """)
  }
}
