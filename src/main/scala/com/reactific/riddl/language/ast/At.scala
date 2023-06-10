/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

import com.reactific.riddl.language.parsing.RiddlParserInput

import scala.language.implicitConversions

case class At(
  source: String,
  offset: Int = 0)
    extends Ordered[At] {

  override def compare(that: At): Int =  Ordering[(String, Int)].compare((source, offset), (that.source, that.offset))
}

object At {
  val empty: At = At("")
  def empty(input: RiddlParserInput): At = { At("") }
  final val defaultSourceName = RiddlParserInput.empty.origin

  implicit def apply(): At = { At("") }
  implicit def apply(line: Int): At = { At("") }

}
