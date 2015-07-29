package com.shamaevn.mastertags.db

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable


case class NoteModel(id: Long, name: String, tagIds: mutable.Set[Long])

object NoteModel {
  private val noteIdCounter: AtomicInteger = new AtomicInteger(0)

  def createNote(noteName: String): NoteModel = {
    val note = NoteModel(noteIdCounter.incrementAndGet(), noteName, mutable.Set())
    Database.notes += note
    note
  }

  def getNote(noteId: Long): NoteModel = {
    Database.notes.find(note => note.id == noteId).getOrElse {
      throw new NotFoundException("Note with id $noteId not found")
    }
  }

  def getNotesByTag(tagId: Long): Seq[NoteModel] = {
    Database.notes.filter(note => note.tagIds.contains(tagId))
  }

  def addTagToNote(tagName: String, noteId: Long) = {
    val tag = TagModel.getOrCreateTag(tagName)
    val note = getNote(noteId)
    note.tagIds.add(tag.id)
    tag
  }

  def deleteTagFromNote(tagId: Long, noteId: Long) = {
    val note = getNote(noteId)
    note.tagIds.remove(tagId)
  }
}