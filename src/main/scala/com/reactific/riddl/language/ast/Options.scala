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

  //////////////////////////////////////////////////////////////////// HANDLER

  sealed abstract class HandlerOption(val name: String) extends OptionValue

  case class PartialHandlerOption(loc: At) extends HandlerOption("partial")

  /////////////////////////////////////////////////////////////////// PROJECTION

  sealed abstract class ProjectorOption(val name: String) extends OptionValue

  /////////////////////////////////////////////////////////////////// PROJECTION

  sealed abstract class RepositoryOption(val name: String) extends OptionValue

  //////////////////////////////////////////////////////////////////// ENTITY

  sealed trait EntityValue extends RiddlValue

  sealed abstract class EntityOption(val name: String)
      extends EntityValue with OptionValue

  //////////////////////////////////////////////////////////////////// FUNCTION

  sealed abstract class FunctionOption(val name: String) extends OptionValue


  //////////////////////////////////////////////////////////////////// CONTEXT

  sealed abstract class ContextOption(val name: String) extends OptionValue


  //////////////////////////////////////////////////////////////////// PROCESSOR

  sealed abstract class StreamletOption(val name: String) extends OptionValue


  //////////////////////////////////////////////////////////////////// PIPE

  sealed abstract class ConnectorOption(val name: String) extends OptionValue


  //////////////////////////////////////////////////////////////////// SAGA

  sealed abstract class SagaOption(val name: String) extends OptionValue


  ////////////////////////////////////////////////////////////////// APPLICATION

  sealed abstract class ApplicationOption(val name: String) extends OptionValue


  ////////////////////////////////////////////////////////////////// DOMAIN

  sealed abstract class DomainOption(val name: String) extends OptionValue


  ////////////////////////////////////////////////////////////////// DOMAIN

  sealed abstract class EpicOption(val name: String) extends OptionValue

}
