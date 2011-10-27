package org.intrade.data

import xml.Node
import org.intrade.Implicits._

object EventClass {
  def apply(node: Node) = new EventClass {
    val id: String = node \ "@id"
    val name: String = node \ "name"
    val displayOrder: Int = node \ "displayOrder"
    val eventGroups = node \ "EventGroup" map EventGroup.apply
  }
}

trait EventClass {
  def id: String

  def name: String

  def displayOrder: Int

  def eventGroups: Seq[EventGroup]
}