/*
* Copyright 2019 Ossum, Inc.
*
* SPDX-License-Identifier: Apache-2.0
*/
package com.reactific.riddl.language.ast

import java.net.URL
import java.nio.file.Path
import scala.reflect.ClassTag
import scala.reflect.classTag

trait AbstractDefinitions {

  trait RiddlNode
  trait RiddlValue extends RiddlNode 
  case class LiteralString(loc: At, s: String) extends RiddlValue
  
  object LiteralString
  case class Identifier(loc: At, value: String) extends RiddlValue

  object Identifier {
    val empty = Identifier(At.empty, "")
  }
  case class PathIdentifier(loc: At, value: Seq[String]) extends RiddlValue

  object PathIdentifier {
    val empty = PathIdentifier(At.empty, Nil)
  }
  trait Description extends RiddlValue

  case class BlockDescription(
    loc: At = At.empty,
    lines: Seq[LiteralString] = Seq.empty[LiteralString]
  ) extends Description {
    def format: String = ""
  }

  case class FileDescription(loc: At, file: Path) extends Description

  case class URLDescription(loc: At, url: URL) extends Description

  trait BrieflyDescribedValue extends RiddlValue
  trait DescribedValue extends RiddlValue
  trait Container[+D <: RiddlValue] extends RiddlValue 
  trait Definition
      extends DescribedValue
      with BrieflyDescribedValue
      with Container[Definition] 

  trait LeafDefinition extends Definition

  trait AlwaysEmpty extends Definition

  abstract class Reference[+T <: Definition: ClassTag] extends RiddlValue

  trait Action extends RiddlValue

  trait GherkinValue extends RiddlValue

  trait GherkinClause extends GherkinValue

  trait FunctionDefinition extends Definition

  trait SagaDefinition extends Definition

  trait StateDefinition extends Definition

  trait ProjectorDefinition extends Definition

  trait UseCaseDefinition extends Definition
}
