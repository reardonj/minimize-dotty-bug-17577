/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.parsing

import com.reactific.riddl.language.ast.At

import java.io.File
import java.net.URL
import java.nio.file.Path
import scala.collection.Searching
import scala.io.Source
import scala.language.implicitConversions

abstract class RiddlParserInput {
  def origin: String
  def data: String
  def root: File

  def isEmpty: Boolean = { data.isEmpty }
  final def nonEmpty: Boolean = !isEmpty

  def checkTraceable(): Unit = ()


  @inline final def location(index: Int): At = { At("", index) }

  def prettyIndex(index: Int): String = { location(index).toString }

  val nl: String = System.getProperty("line.separator")

}

private case class EmptyParserInput() extends RiddlParserInput {
  override def origin: String = "empty"

  override def data: String = ""

  override def root: File = File.listRoots().head

}

case class StringParserInput(
  data: String,
  origin: String = At.defaultSourceName)
    extends RiddlParserInput {
  val root: File = new File(System.getProperty("user.dir"))
  override def isEmpty: Boolean = data.isEmpty
}

case class FileParserInput(file: File) extends RiddlParserInput {

  lazy val data: String = {
    val source: Source = Source.fromFile(file)
    try { source.getLines().mkString("\n") }
    finally { source.close() }
  }
  override def isEmpty: Boolean = data.isEmpty
  val root: File = file.getParentFile
  def origin: String = file.getName
  def this(path: Path) = this(path.toFile)
}

case class URLParserInput(url: URL) extends RiddlParserInput {
  require(url.getProtocol.startsWith("http"), s"Non-http URL protocol '${url.getProtocol}``")
  lazy val data: String = {
    val source: Source = Source.fromURL(url)
    try { source.getLines().mkString("\n") }
    finally { source.close() }
  }
  override def isEmpty: Boolean = data.isEmpty
  val root: File = new File(url.getFile)
  def origin: String = url.toString
}

case class SourceParserInput(source: Source, origin: String)
    extends RiddlParserInput {

  lazy val data: String =
    try { source.mkString }
    finally { source.close() }
  val root: File = new File(System.getProperty("user.dir"))
}

object RiddlParserInput {

  val empty: RiddlParserInput = EmptyParserInput()

  implicit def apply(
    data: String
  ): RiddlParserInput = { StringParserInput(data) }

  implicit def apply(source: Source): RiddlParserInput = {
    SourceParserInput(source, source.descr)
  }
  implicit def apply(file: File): RiddlParserInput = { FileParserInput(file) }

  implicit def apply(path: Path): RiddlParserInput = {
    FileParserInput(path.toFile)
  }

  implicit def apply(url: URL): RiddlParserInput = URLParserInput(url)
}
