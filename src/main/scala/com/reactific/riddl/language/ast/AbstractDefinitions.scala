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

  /** The root trait of all things RIDDL AST. Every node in the tree is a
    * RiddlNode.
    */
  trait RiddlNode

  /** The root trait of all parsable values. If a parser returns something, its
    * a RiddlValue. The distinguishing factor is the inclusion of the parsing
    * location given by the `loc` field.
    */
  trait RiddlValue extends RiddlNode 

  /** Represents a literal string parsed between quote characters in the input
    *
    * @param loc
    *   The location in the input of the opening quote character
    * @param s
    *   The parsed value of the string content
    */
  case class LiteralString(loc: At, s: String) extends RiddlValue
  
  object LiteralString

  /** A RiddlValue that is a parsed identifier, typically the name of a
    * definition.
    *
    * @param loc
    *   The location in the input where the identifier starts
    * @param value
    *   The parsed value of the identifier
    */
  case class Identifier(loc: At, value: String) extends RiddlValue

  object Identifier {
    val empty = Identifier(At.empty, "")
  }

  /** Represents a segmented identifier to a definition in the model. Path
    * Identifiers are parsed from a dot-separated list of identifiers in the
    * input. Path identifiers are used to reference other definitions in the
    * model.
    *
    * @param loc
    *   Location in the input of the first letter of the path identifier
    * @param value
    *   The list of strings that make up the path identifier
    */
  case class PathIdentifier(loc: At, value: Seq[String]) extends RiddlValue

  object PathIdentifier {
    val empty = PathIdentifier(At.empty, Nil)
  }

  /** The description of a definition. All definitions have a name and an
    * optional description. This class provides the description part.
    */
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

  /** Base trait of all values that have an optional Description
    */
  trait DescribedValue extends RiddlValue

  /** Base trait of any definition that is also a ContainerValue
    *
    * @tparam D
    *   The kind of definition that is contained by the container
    */
  trait Container[+D <: RiddlValue] extends RiddlValue 

  /** Base trait for all definitions requiring an identifier for the definition
    * and providing the identify method to yield a string that provides the kind
    * and name
    */
  trait Definition
      extends DescribedValue
      with BrieflyDescribedValue
      with Container[Definition] 

  trait LeafDefinition extends Definition

  trait AlwaysEmpty extends Definition

  /** A reference to a definition of a specific type.
    *
    * @tparam T
    *   The type of definition to which the references refers.
    */
  abstract class Reference[+T <: Definition: ClassTag] extends RiddlValue

  /** Base class for all actions. Actions are used in the "then" and "but"
    * clauses of a Gherkin example such as in the body of a handler's `on`
    * clause or in the definition of a Function. The subclasses define different
    * kinds of actions that can be used.
    */
  trait Action extends RiddlValue

  /** Base class of any Gherkin value
    */
  trait GherkinValue extends RiddlValue

  /** Base class of one of the four Gherkin clauses (Given, When, Then, But)
    */
  trait GherkinClause extends GherkinValue

  /** Base trait of any definition that is in the content of a function.
    */
  trait FunctionDefinition extends Definition

  /** Base trait of definitions that are part of a Saga Definition */
  trait SagaDefinition extends Definition

  /** Base trait of definitions that are part of a Saga Definition */
  trait StateDefinition extends Definition

  /** Base trait of any definition that occurs in the body of a projector */
  trait ProjectorDefinition extends Definition

  /** Base trait of definitions in a UseCase, typically interactions */
  trait UseCaseDefinition extends Definition
}
