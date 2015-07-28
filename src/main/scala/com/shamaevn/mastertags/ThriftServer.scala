package com.shamaevn.mastertags

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.util.Future
import com.shamaevn.mastertags.tags.{Note, Tag, TagService}
import org.apache.thrift.protocol.TBinaryProtocol
import java.net.SocketAddress


object ThriftServer {
  def buildServer(address: SocketAddress) = {
    val protocol = new TBinaryProtocol.Factory()
    val serverService = new TagService.FinagledService(new TagServiceImplementation, protocol)
    ServerBuilder()
      .codec(ThriftServerFramedCodec())
      .name("tag_service")
      .bindTo(address)
      .build(serverService)
  }
}

class TagServiceImplementation extends TagService.FutureIface {

  def createNote(name: String): Future[Note] = {
    val note = Database.createNote(name)
    Future.value(Note(note.id, note.name))
  }

  def addTagToNote(tagName: String, noteId: Long): Future[Tag] = {
    val tag = Database.addTagToNote(tagName, noteId)
    Future.value(Tag(tag.id, tag.name))
  }

  def getNote(noteId: Long): Future[Note] = {
    val note = Database.getNote(noteId)
    val tags = note.tagIds.map(tagId => {
      val tag = Database.getTag(tagId)
      Tag(tag.id, tag.name)
    }).toSeq
    Future.value(Note(note.id, note.name, tags))
  }

  def getTagsByNote(noteId: Long): Future[Seq[Tag]] = {
    val tags = Database.getTagsByNote(noteId).map(tag =>
      Tag(tag.id, tag.name)
    )
    Future.value(tags)
  }

  def getNotesByTag(tagId: Long): Future[Seq[Note]] = {
    val notes = Database.getNotesByTag(tagId).map(note => {
      val tags = note.tagIds.map(tagId => {
        val tag = Database.getTag(tagId)
        Tag(tag.id, tag.name)
      }).toSeq
      Note(note.id, note.name, tags)
    })
    Future.value(notes)
  }

  def deleteTagFromNote(tagId: Long, noteId: Long): Future[Unit] = {
    Database.deleteTagFromNote(tagId, noteId)
    Future.value(Unit)
  }
}