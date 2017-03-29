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
class beat extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro XMLMacro.macroTransform
}


private[macros] class XMLMacro(val c: whitebox.Context) {
  import c.universe._


  val transformer = new  Transformer {
    override def transform(tree: Tree): Tree = {
      atPos(tree.pos) {
        tree match {
          case q"""
              {
                var $$md: _root_.scala.xml.MetaData = _root_.scala.xml.Null;
                ..$attributes
                new _root_.scala.xml.Elem(null, ${Literal(Constant(label: String))}, $$md, $$scope, $minimizeEmpty, ..$child)
              }
            """ => {
            val attrs = for { attribute <- attributes } yield {
              attribute match {
                case q"""$$md = new _root_.scala.xml.UnprefixedAttribute(${Literal(Constant(key: String))}, $value, $$md)""" =>
                  atPos(attribute.pos) { q""" attr(${Literal(Constant(key))}) := ${transform(value)} """}
              }
            }

            child match {
              case Seq() => Nil
              case Seq(q"""$nodeBuffer: _*""") => List(
                q"""{
                   }"""
              )
            }

            val res = q"""${Ident(TermName(label))}(..$attrs).render.bbs("test")"""
            super.transform(res)
          }
          case q"""new _root_.scala.xml.Text($text)""" => atPos(tree.pos) {
            transform(text)
          }
          case x => super.transform(x)
        }
      }
    }
  }


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

  import transformer.transform

  def macroTransform(annottees: Tree*): Tree = {
    println(showRaw(annottees))
    replaceDefBody(annottees, { body => q"""
        import _root_.xyz.ariwaranosai.tidori.dom.DomElements._
        import _root_.xyz.ariwaranosai.tidori.dom.OperatorContext
        import _root_.xyz.ariwaranosai.tidori.dom.DomOperator._
        import _root_.xyz.ariwaranosai.tidori.dom.BeatOperatorImplicit._
        import _root_.scalatags.JsDom.all._
        ${transform(body)}
      """
    })
  }
}
