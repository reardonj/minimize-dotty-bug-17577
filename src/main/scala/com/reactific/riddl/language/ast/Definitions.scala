/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

import com.reactific.riddl.language.parsing.RiddlParserInput
import com.reactific.riddl.language.parsing.Terminals.Keywords

trait Definitions extends Expressions with Options {

  sealed trait AdaptorDefinition extends Definition

  sealed trait ApplicationDefinition extends Definition

  sealed trait ContextDefinition extends Definition

  sealed trait DomainDefinition extends Definition

  sealed trait EntityDefinition extends Definition

  sealed trait HandlerDefinition extends Definition

  sealed trait OnClauseDefinition extends Definition

  sealed trait ProcessorDefinition
      extends Definition
      with AdaptorDefinition
      with ApplicationDefinition
      with ContextDefinition
      with EntityDefinition
      with ProjectorDefinition
      with RepositoryDefinition
      with StreamletDefinition
      with SagaDefinition

  sealed trait RepositoryDefinition extends Definition

  sealed trait RootDefinition extends Definition

  sealed trait StreamletDefinition extends Definition

  sealed trait EpicDefinition extends Definition

  sealed trait VitalDefinitionDefinition
      extends AdaptorDefinition
      with ApplicationDefinition
      with ContextDefinition
      with DomainDefinition
      with EntityDefinition
      with FunctionDefinition
      with StreamletDefinition
      with ProjectorDefinition
      with RepositoryDefinition
      with SagaDefinition
      with EpicDefinition

  trait ProcessorRef[+T <: Processor[?, ?]] extends Reference[T]

  case class Term(
    loc: At,
    id: Identifier,
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends LeafDefinition
      with VitalDefinitionDefinition 

  trait WithTerms 

  case class Include[T <: Definition](
    loc: At = At(""),
    contents: Seq[T] = Seq.empty[T],
    source: Option[String] = None
  ) extends Definition
      with VitalDefinitionDefinition
      with RootDefinition

  trait WithIncludes[T <: Definition] extends Container[T]

  case class Author(
    loc: At,
    id: Identifier,
    name: LiteralString,
    email: LiteralString,
    organization: Option[LiteralString] = None,
    title: Option[LiteralString] = None,
    url: Option[java.net.URL] = None,
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends LeafDefinition
      with RootDefinition
      with DomainDefinition

  case class AuthorRef(loc: At, pathId: PathIdentifier) extends Reference[Author]

  trait WithAuthors extends Definition 

  sealed trait VitalDefinition[OPT <: OptionValue, DEF <: Definition]
      extends Definition
      with WithOptions[OPT]
      with WithAuthors
      with WithIncludes[DEF]
      with WithTerms


  trait WithTypes extends Definition

  trait Processor[OPT <: OptionValue, DEF <: Definition] extends VitalDefinition[OPT, DEF] with WithTypes


  sealed trait MessageRef extends Reference[Type] 

  object MessageRef

  case class Constant(
    loc: At,
    id: Identifier,
    typeEx: TypeExpression,
    value: Expression,
    brief: Option[LiteralString],
    description: Option[Description]
  ) extends LeafDefinition
      with ProcessorDefinition
      with DomainDefinition

  case class Type(
    loc: At,
    id: Identifier,
    typ: TypeExpression,
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Definition
      with StateDefinition
      with ProcessorDefinition
      with FunctionDefinition
      with DomainDefinition

  case class TypeRef(
    loc: At = At.empty,
    pathId: PathIdentifier = PathIdentifier.empty
  ) extends Reference[Type]


  case class Example(
    loc: At,
    id: Identifier,
  ) extends LeafDefinition
      with OnClauseDefinition
      with FunctionDefinition
      with EpicDefinition

  // ////////////////////////////////////////////////////////// Entities

  case class EntityRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Entity] 

  case class FunctionRef(loc: At, pathId: PathIdentifier) extends Reference[Function] 

  case class Function(
    loc: At,
    id: Identifier,
    input: Option[Aggregation] = None,
    output: Option[Aggregation] = None,
    types: Seq[Type] = Seq.empty[Type],
    functions: Seq[Function] = Seq.empty[Function],
    examples: Seq[Example] = Seq.empty[Example],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    includes: Seq[Include[FunctionDefinition]] = Seq
      .empty[Include[FunctionDefinition]],
    options: Seq[FunctionOption] = Seq.empty[FunctionOption],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends VitalDefinition[FunctionOption, FunctionDefinition]
      with WithTypes
      with AdaptorDefinition
      with ApplicationDefinition
      with ContextDefinition
      with EntityDefinition
      with FunctionDefinition
      with ProjectorDefinition
      with RepositoryDefinition
      with SagaDefinition
      with StreamletDefinition

  case class Invariant(
    loc: At,
    id: Identifier,
    expression: Option[Condition] = None,
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends LeafDefinition
      with EntityDefinition
      with ProjectorDefinition
      with StateDefinition

  sealed trait OnClause extends HandlerDefinition 

  case class Handler(
    loc: At,
    id: Identifier,
    clauses: Seq[OnClause] = Seq.empty[OnClause],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Container[HandlerDefinition]
      with AdaptorDefinition
      with ApplicationDefinition
      with ContextDefinition
      with EntityDefinition
      with StateDefinition
      with RepositoryDefinition
      with StreamletDefinition
      with ProjectorDefinition

  case class HandlerRef(loc: At, pathId: PathIdentifier) extends Reference[Handler]

  case class State(
    loc: At,
    id: Identifier,
    typ: TypeRef,
    types: Seq[Type] = Seq.empty[Type],
    handlers: Seq[Handler] = Seq.empty[Handler],
    invariants: Seq[Invariant] = Seq.empty[Invariant],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends EntityDefinition

  case class StateRef(loc: At, pathId: PathIdentifier) extends Reference[State]

  case class Entity(
    loc: At,
    id: Identifier,
    options: Seq[EntityOption] = Seq.empty[EntityOption],
    states: Seq[State] = Seq.empty[State],
    types: Seq[Type] = Seq.empty[Type],
    constants: Seq[Constant] = Seq.empty[Constant],
    handlers: Seq[Handler] = Seq.empty[Handler],
    functions: Seq[Function] = Seq.empty[Function],
    invariants: Seq[Invariant] = Seq.empty[Invariant],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    includes: Seq[Include[EntityDefinition]] = Seq
      .empty[Include[EntityDefinition]],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Processor[EntityOption, EntityDefinition]
      with ContextDefinition

  sealed trait AdaptorDirection extends RiddlValue

  case class Adaptor(
    loc: At,
    id: Identifier,
    direction: AdaptorDirection,
    context: ContextRef,
    handlers: Seq[Handler] = Seq.empty[Handler],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    types: Seq[Type] = Seq.empty[Type],
    constants: Seq[Constant] = Seq.empty[Constant],
    functions: Seq[Function] = Seq.empty[Function],
    includes: Seq[Include[AdaptorDefinition]] = Seq
      .empty[Include[AdaptorDefinition]],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    options: Seq[AdaptorOption] = Seq.empty[AdaptorOption],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Processor[AdaptorOption, AdaptorDefinition]
      with ContextDefinition

  case class AdaptorRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Adaptor]

  case class Repository(
    loc: At,
    id: Identifier,
    types: Seq[Type] = Seq.empty[Type],
    handlers: Seq[Handler] = Seq.empty[Handler],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    functions: Seq[Function] = Seq.empty[Function],
    includes: Seq[Include[RepositoryDefinition]] = Seq
      .empty[Include[RepositoryDefinition]],
    options: Seq[RepositoryOption] = Seq.empty[RepositoryOption],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Processor[RepositoryOption, RepositoryDefinition]
      with ContextDefinition

  case class RepositoryRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Projector]

  case class Projector(
    loc: At,
    id: Identifier,
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    options: Seq[ProjectorOption] = Seq.empty[ProjectorOption],
    includes: Seq[Include[ProjectorDefinition]] = Seq
      .empty[Include[ProjectorDefinition]],
    types: Seq[Type] = Seq.empty[Type],
    constants: Seq[Constant] = Seq.empty[Constant],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    handlers: Seq[Handler] = Seq.empty[Handler],
    functions: Seq[Function] = Seq.empty[Function],
    invariants: Seq[Invariant] = Seq.empty[Invariant],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Processor[ProjectorOption, ProjectorDefinition]
      with ContextDefinition
      with WithTypes

  case class Replica(
    loc: At,
    id: Identifier,
    typeExp: TypeExpression,
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends LeafDefinition
      with ContextDefinition

  case class ProjectorRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Projector] 

  case class Context(
    loc: At,
    id: Identifier,
    options: Seq[ContextOption] = Seq.empty[ContextOption],
    types: Seq[Type] = Seq.empty[Type],
    constants: Seq[Constant] = Seq.empty[Constant],
    entities: Seq[Entity] = Seq.empty[Entity],
    adaptors: Seq[Adaptor] = Seq.empty[Adaptor],
    sagas: Seq[Saga] = Seq.empty[Saga],
    streamlets: Seq[Streamlet] = Seq.empty[Streamlet],
    functions: Seq[Function] = Seq.empty[Function],
    terms: Seq[Term] = Seq.empty[Term],
    includes: Seq[Include[ContextDefinition]] = Seq.empty[Include[ContextDefinition]],
    handlers: Seq[Handler] = Seq.empty[Handler],
    projectors: Seq[Projector] = Seq.empty[Projector],
    repositories: Seq[Repository] = Seq.empty[Repository],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    connections: Seq[Connector] = Seq.empty[Connector],
    replicas: Seq[Replica] = Seq.empty[Replica],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Processor[ContextOption, ContextDefinition]
      with DomainDefinition 

  case class ContextRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Context] 

  sealed trait Portlet extends Definition

  case class Inlet(
    loc: At,
    id: Identifier,
    type_ : Reference[Type],
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends Portlet
      with LeafDefinition
      with ProcessorDefinition
      with AlwaysEmpty 

  case class Outlet(
    loc: At,
    id: Identifier,
    type_ : Reference[Type],
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends Portlet
      with LeafDefinition
      with ProcessorDefinition
      with AlwaysEmpty 

  case class Connector(
    loc: At,
    id: Identifier,
    options: Seq[ConnectorOption] = Seq.empty[ConnectorOption],
    flows: Option[TypeRef] = Option.empty[TypeRef],
    from: Option[OutletRef] = Option.empty[OutletRef],
    to: Option[InletRef] = Option.empty[InletRef],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = Option.empty[Description]
  ) extends LeafDefinition
      with ContextDefinition
      with WithOptions[ConnectorOption] 

  sealed trait StreamletShape extends RiddlValue 

  case class Streamlet(
    loc: At,
    id: Identifier,
    shape: StreamletShape,
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    handlers: Seq[Handler] = Seq.empty[Handler],
    functions: Seq[Function] = Seq.empty[Function],
    types: Seq[Type] = Seq.empty[Type],
    includes: Seq[Include[StreamletDefinition]] = Seq
      .empty[Include[StreamletDefinition]],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    options: Seq[StreamletOption] = Seq.empty[StreamletOption],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends Processor[StreamletOption, StreamletDefinition]
      with ContextDefinition 

  case class StreamletRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Streamlet] 

  sealed trait PortletRef[+T <: Portlet] extends Reference[T]

  case class InletRef(loc: At, pathId: PathIdentifier) extends PortletRef[Inlet] 

  case class OutletRef(loc: At, pathId: PathIdentifier) extends PortletRef[Outlet] 

  case class SagaStep(
    loc: At,
    id: Identifier,
    doAction: Seq[Example] = Seq.empty[Example],
    undoAction: Seq[Example] = Seq.empty[Example],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends SagaDefinition 

  case class Saga(
    loc: At,
    id: Identifier,
    options: Seq[SagaOption] = Seq.empty[SagaOption],
    input: Option[Aggregation] = None,
    output: Option[Aggregation] = None,
    sagaSteps: Seq[SagaStep] = Seq.empty[SagaStep],
    functions: Seq[Function] = Seq.empty[Function],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    includes: Seq[Include[SagaDefinition]] = Seq.empty[Include[SagaDefinition]],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends VitalDefinition[SagaOption, SagaDefinition]
      with ContextDefinition
      with DomainDefinition 

  case class SagaRef(loc: At, pathId: PathIdentifier) extends Reference[Saga] 

  case class User(
    loc: At,
    id: Identifier,
    is_a: LiteralString,
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends LeafDefinition
      with DomainDefinition 

  case class UserRef(loc: At, pathId: PathIdentifier) extends Reference[User] 

  sealed trait Interaction extends UseCaseDefinition 

  case class ParallelInteractions(
    loc: At,
    id: Identifier = Identifier.empty,
    contents: Seq[Interaction] = Seq.empty[Interaction],
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends Interaction 

  case class SequentialInteractions(
    loc: At,
    id: Identifier = Identifier.empty,
    contents: Seq[Interaction] = Seq.empty[Interaction],
    brief: Option[LiteralString],
    description: Option[Description] = None
  ) extends Interaction 

  case class OptionalInteractions(
    loc: At,
    id: Identifier = Identifier.empty,
    contents: Seq[Interaction] = Seq.empty[Interaction],
    brief: Option[LiteralString],
    description: Option[Description] = None
  ) extends Interaction 

  sealed trait GenericInteraction extends Interaction with LeafDefinition 


  case class UseCase (
    loc: At,
    id: Identifier,
    userStory: Option[UserStory] = None,
    contents: Seq[Interaction] = Seq.empty[Interaction],
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends EpicDefinition
      with Container[Interaction] 

  case class UserStory(
    loc: At,
    user: UserRef,
    capability: LiteralString,
    benefit: LiteralString
  ) extends RiddlValue 
  
  case class Epic(
    loc: At,
    id: Identifier,
    userStory: Option[UserStory] = Option.empty[UserStory],
    shownBy: Seq[java.net.URL] = Seq.empty[java.net.URL],
    cases: Seq[UseCase] = Seq.empty[UseCase],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    includes: Seq[Include[EpicDefinition]] = Seq
      .empty[Include[EpicDefinition]],
    options: Seq[EpicOption] = Seq.empty[EpicOption],
    terms: Seq[Term] = Seq.empty[Term],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends VitalDefinition[EpicOption, EpicDefinition]
      with DomainDefinition 

  case class EpicRef(loc: At, pathId: PathIdentifier) extends Reference[Epic] 

  sealed trait UIElement extends ApplicationDefinition

  case class Group(
    loc: At,
    id: Identifier,
    types: Seq[Type] = Seq.empty[Type],
    elements: Seq[UIElement] = Seq.empty[UIElement],
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends UIElement

  case class GroupRef(loc: At, pathId: PathIdentifier) extends Reference[Group] 

  case class Output(
    loc: At,
    id: Identifier,
    types: Seq[Type],
    putOut: MessageRef,
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends UIElement 

  case class OutputRef(loc: At, pathId: PathIdentifier) extends Reference[Output] 

  case class Input(
    loc: At,
    id: Identifier,
    types: Seq[Type],
    putIn: MessageRef,
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends UIElement 

  case class InputRef(loc: At, pathId: PathIdentifier) extends Reference[Input] 

  case class Application(
    loc: At,
    id: Identifier,
    options: Seq[ApplicationOption] = Seq.empty[ApplicationOption],
    types: Seq[Type] = Seq.empty[Type],
    constants: Seq[Constant] = Seq.empty[Constant],
    groups: Seq[Group] = Seq.empty[Group],
    handlers: Seq[Handler] = Seq.empty[Handler],
    inlets: Seq[Inlet] = Seq.empty[Inlet],
    outlets: Seq[Outlet] = Seq.empty[Outlet],
    functions: Seq[Function] = Seq.empty[Function],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    terms: Seq[Term] = Seq.empty[Term],
    includes: Seq[Include[ApplicationDefinition]] = Seq.empty,
    brief: Option[LiteralString] = None,
    description: Option[Description] = None
  ) extends Processor[ApplicationOption, ApplicationDefinition]
      with DomainDefinition

  case class ApplicationRef(loc: At, pathId: PathIdentifier) extends ProcessorRef[Application]

  case class Domain(
    loc: At,
    id: Identifier,
    options: Seq[DomainOption] = Seq.empty[DomainOption],
    authors: Seq[AuthorRef] = Seq.empty[AuthorRef],
    authorDefs: Seq[Author] = Seq.empty[Author],
    types: Seq[Type] = Seq.empty[Type],
    constants: Seq[Constant] = Seq.empty[Constant],
    contexts: Seq[Context] = Seq.empty[Context],
    users: Seq[User] = Seq.empty[User],
    epics: Seq[Epic] = Seq.empty[Epic],
    sagas: Seq[Saga] = Seq.empty[Saga],
    applications: Seq[Application] = Seq.empty[Application],
    domains: Seq[Domain] = Seq.empty[Domain],
    terms: Seq[Term] = Seq.empty[Term],
    includes: Seq[Include[DomainDefinition]] = Seq
      .empty[Include[DomainDefinition]],
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends VitalDefinition[DomainOption, DomainDefinition]
      with RootDefinition
      with WithTypes
      with DomainDefinition

  case class DomainRef(loc: At, pathId: PathIdentifier) extends Reference[Domain]
}
