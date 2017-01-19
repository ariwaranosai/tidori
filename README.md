# tidori
----
![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.8.svg) [![Build Status](https://travis-ci.org/ariwaranosai/tidori.svg?branch=master)](https://travis-ci.org/ariwaranosai/tidori)

Tidori(千鳥) is a toy inspired by [Typed.js](https://github.com/mattboldt/typed.js/) in scalajs.

[view live demo](http://ariwaranosai.xyz/main/)

###Installation

Add the following to your sbt build definition:
`
~~~scala
resolvers += Resolver.bintrayRepo("ariwarasai","maven")
libraryDependencies += "xyz.ariwaranosai" %%% "tidori" % lastVersion
~~~

###Usage

####Quick Look

You can build your typing animation with following way.

~~~scala
// select node to show animation
val pNode = dom.document.getElementById("broad")

// set speed for typing in ms
val context = OperatorContext(pNode, 200)

// write your text and change to animation
val text = "typing animation".bb

// run
text.run(context)
~~~

Add run script in your html.

A more complex example is shown in [example](https://github.com/ariwaranosai/tidori/blob/master/examples/src/main/scala/Example.scala) and [html](https://github.com/ariwaranosai/tidori/blob/master/examples/src/main/resources/web/main.html).

####More Details

`tidori` provids more power to control typing animation.

~~~scala
import xyz.ariwaranosai.tidori.dom.DomOperator._
import xyz.ariwaranosai.tidori.dom.BeatOperatorImplicit._

"kancolle".bb // typing string in each of character
"kancolle".b // typing string at once

speed(0.5) // change typing speed 1 * 0.5 = 0.5 in following typing

3.delay // pause typing in 3 beat

2.del() // delete 2 character
~~~

`tidori` uses [scalatags](https://github.com/lihaoyi/scalatags) to build html element.

~~~scala
import xyz.ariwaranosai.tidori.dom.DomOperator._
import xyz.ariwaranosai.tidori.dom.BeatOperatorImplicit._
import scalatags.JsDom.all._

val node = a(href:= "https://github.com/ariwaranosai/tidori").render

node.b // typing node

node.bs("tidori") // typing string in node at one
node.bbs("tidori") // typing each of character in node
~~~

you can use `~:` to connect two operations. There are also some html elements in `tidori.dom.DomElements`.

~~~scala
"Jintsu".bb ~: htmlBR.b ~: "Sendai".bb
~~~

You can find more operations in [DomOperator](https://github.com/ariwaranosai/tidori/blob/master/src/main/scala/xyz/ariwaranosai/tidori/dom/DomOperator.scala)

`tidori` can build animation with multi-nest elements in a easy way.

~~~scala
import xyz.ariwaranosai.tidori.dom.DomOperator._
import xyz.ariwaranosai.tidori.dom.BeatOperatorImplicit._
import xyz.ariwaranosai.tidori.dom.DomElements._
import scalatags.JsDom.all._

val pNode = p().render.b

val internalOp = "Katori".bb ~: htmlBR.b ~: "Kashima".bb
val aNode = a().render

pNode ~: "kancolle".bb ~: subElement(aNode, internalOp) ~: htmlBR.b
~~~


####implementation Details

Core type of `tidori` is 

~~~scala
class BeatDomOperator[A](time: Double, op: OpC => Future[(A, OpC)]) {
    def run(m: OpC): Future[(A, OpC)] = ???
}
~~~

`BeatDomOperator` likes `StateT[Future, T, S]`. But context which future is running in, is implemented by `setTimeout` in javascript (in fact, BeatDomOperator is not implemented by `StateT` since the type we used to replace future is not a monad). Liking `state monad`, we also provide `flatMap`, `get` and `set`, and function  for sub-context used them. So, it is easy to combine those operations.