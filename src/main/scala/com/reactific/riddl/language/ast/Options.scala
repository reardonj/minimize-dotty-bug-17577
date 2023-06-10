/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast
import com.reactific.riddl.language.parsing.Terminals

import scala.reflect.ClassTag

trait Options extends AbstractDefinitions {

  trait OptionValue extends RiddlValue 

  trait WithOptions[T <: OptionValue] extends Definition

  //////////////////////////////////////////////////////////////////// ADAPTOR

  sealed abstract class AdaptorOption(val name: String) extends OptionValue

  case class AdaptorTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends AdaptorOption("technology")

  //////////////////////////////////////////////////////////////////// HANDLER

  sealed abstract class HandlerOption(val name: String) extends OptionValue

  case class PartialHandlerOption(loc: At) extends HandlerOption("partial")

  /////////////////////////////////////////////////////////////////// PROJECTION

  sealed abstract class ProjectorOption(val name: String) extends OptionValue

  case class ProjectorTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends ProjectorOption("technology")

  /////////////////////////////////////////////////////////////////// PROJECTION

  sealed abstract class RepositoryOption(val name: String) extends OptionValue

  case class RepositoryTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends RepositoryOption("technology")

  //////////////////////////////////////////////////////////////////// ENTITY

  sealed trait EntityValue extends RiddlValue

  sealed abstract class EntityOption(val name: String)
      extends EntityValue with OptionValue

  case class EntityEventSourced(loc: At) extends EntityOption("event sourced")

  case class EntityValueOption(loc: At) extends EntityOption("value")

  case class EntityTransient(loc: At) extends EntityOption("transient")

  case class EntityIsAggregate(loc: At) extends EntityOption("aggregate")

  case class EntityIsConsistent(loc: At) extends EntityOption("consistent")

  case class EntityIsAvailable(loc: At) extends EntityOption("available")

  case class EntityIsFiniteStateMachine(loc: At)
      extends EntityOption("finite state machine")

  case class EntityMessageQueue(loc: At) extends EntityOption("message queue")

  case class EntityIsDevice(loc: At) extends EntityOption("device")

  case class EntityTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends EntityOption("technology")

  case class EntityKind(loc: At, args: Seq[LiteralString])
      extends EntityOption("kind")

  //////////////////////////////////////////////////////////////////// FUNCTION

  sealed abstract class FunctionOption(val name: String) extends OptionValue

  case class TailRecursive(loc: At) extends FunctionOption("tail-recursive")

  //////////////////////////////////////////////////////////////////// CONTEXT

  sealed abstract class ContextOption(val name: String) extends OptionValue

  case class ContextPackageOption(
    loc: At,
    args: Seq[LiteralString])
      extends ContextOption("package")

  case class WrapperOption(loc: At) extends ContextOption("wrapper")

  case class ServiceOption(loc: At) extends ContextOption("service")

  case class GatewayOption(loc: At) extends ContextOption("gateway")

  case class ContextTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends ContextOption("technology")

  //////////////////////////////////////////////////////////////////// PROCESSOR

  sealed abstract class StreamletOption(val name: String) extends OptionValue

  case class StreamletTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends StreamletOption(Terminals.Options.technology)

  //////////////////////////////////////////////////////////////////// PIPE

  sealed abstract class ConnectorOption(val name: String) extends OptionValue

  case class ConnectorPersistentOption(loc: At)
    extends ConnectorOption("package")

  case class ConnectorTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends ConnectorOption("technology")

  //////////////////////////////////////////////////////////////////// SAGA

  sealed abstract class SagaOption(val name: String) extends OptionValue

  case class SequentialOption(loc: At) extends SagaOption("sequential")

  case class ParallelOption(loc: At) extends SagaOption("parallel")

  case class SagaTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends SagaOption("technology")

  ////////////////////////////////////////////////////////////////// APPLICATION

  sealed abstract class ApplicationOption(val name: String) extends OptionValue

  case class ApplicationTechnologyOption(
    loc: At,
    args: Seq[LiteralString] = Seq.empty[LiteralString])
      extends ApplicationOption("technology")

  ////////////////////////////////////////////////////////////////// DOMAIN

  sealed abstract class DomainOption(val name: String) extends OptionValue

  case class DomainPackageOption(
    loc: At,
    args: Seq[LiteralString])
      extends DomainOption("package")

  case class DomainExternalOption(loc: At) extends DomainOption("external")

  case class DomainTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends DomainOption("technology")

  ////////////////////////////////////////////////////////////////// DOMAIN

  sealed abstract class EpicOption(val name: String) extends OptionValue

  case class EpicTechnologyOption(
    loc: At,
    args: Seq[LiteralString])
      extends EpicOption("technology")

  case class EpicSynchronousOption(loc: At) extends EpicOption("synch")

}
