package com.shamaevn.mastertags

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable

case class NotFoundException(error: String) extends Exception
case class TagNotFoundException() extends Exception
case class NoteNotFoundException() extends Exception

case class NoteModel(id: Long, name: String, tagIds: mutable.Set[Long] = mutable.Set())
case class TagModel(id: Long, name: String)

object Database {
  private val notes = new mutable.ArrayBuffer[NoteModel] with mutable.SynchronizedBuffer[NoteModel]
  private val tags = new mutable.ArrayBuffer[TagModel] with mutable.SynchronizedBuffer[TagModel]

  private val tagIdCounter: AtomicInteger = new AtomicInteger(0)
  private val noteIdCounter: AtomicInteger = new AtomicInteger(0)

  def createNote(noteName: String): NoteModel = {
    val note = NoteModel(noteIdCounter.incrementAndGet(), noteName)
    notes += note
    note
  }

  def getTag(tagId: Long): TagModel = {
    tags.find(tag => tag.id == tagId).getOrElse { throw new TagNotFoundException }
  }

  def getOrCreateTag(name: String): TagModel = {
    tags.find(tag => tag.name == name).getOrElse {
      val tag = TagModel(tagIdCounter.incrementAndGet(), name)
      tags += tag
      tag
    }
  }

  def getNote(noteId: Long): NoteModel = {
    notes.find(note => note.id == noteId).getOrElse { throw new NoteNotFoundException }
  }

  def getTagsByNote(noteId: Long): Seq[TagModel] = {
    val note = getNote(noteId)
    note.tagIds.map(tagId => getTag(tagId)).toSeq
  }

  def getNotesByTag(tagId: Long): Seq[NoteModel] = {
    notes.filter(note => note.tagIds.contains(tagId))
  }

  def addTagToNote(tagName: String, noteId: Long) = {
    val tag = getOrCreateTag(tagName)
    val note = getNote(noteId)
    note.tagIds.add(tag.id)
    tag
  }

  def deleteTagFromNote(tagId: Long, noteId: Long) = {
    val note = getNote(noteId)
    note.tagIds.remove(tagId)
  }
}
