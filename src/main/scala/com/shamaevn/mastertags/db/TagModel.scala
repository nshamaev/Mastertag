package com.shamaevn.mastertags.db

import java.util.concurrent.atomic.AtomicInteger


case class TagModel(id: Long, name: String)

object TagModel {
  private val tagIdCounter: AtomicInteger = new AtomicInteger(0)

  def getTagsByNote(noteId: Long): Seq[TagModel] = {
    val note = NoteModel.getNote(noteId)
    note.tagIds.map(tagId => getTag(tagId)).toSeq
  }

  def getTag(tagId: Long): TagModel = {
    Database.tags.find(tag => tag.id == tagId).getOrElse {
      throw new NotFoundException("Tag with id $tagId not found")
    }
  }

  def getOrCreateTag(name: String): TagModel = {
    Database.tags.find(tag => tag.name == name).getOrElse {
      val tag = TagModel(tagIdCounter.incrementAndGet(), name)
      Database.tags += tag
      tag
    }
  }
}