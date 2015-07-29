package com.shamaevn.mastertags

import com.shamaevn.mastertags.db.{TagModel, NoteModel, Database}
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

class TagServiceImplementation extends TagService.FutureIface with ThriftUtil {

  def createNote(name: String): Future[Note] = {
    val note = NoteModel.createNote(name)
    Future.value(serializeNote(note))
  }

  def addTagToNote(tagName: String, noteId: Long): Future[Tag] = {
    val tag = NoteModel.addTagToNote(tagName, noteId)
    Future.value(serializeTag(tag))
  }

  def getNote(noteId: Long): Future[Note] = {
    val note = NoteModel.getNote(noteId)
    Future.value(serializeNote(note))
  }

  def getTagsByNote(noteId: Long): Future[Seq[Tag]] = {
    val tags = TagModel.getTagsByNote(noteId)
    Future.value(serializeTags(tags))
  }

  def getNotesByTag(tagId: Long): Future[Seq[Note]] = {
    val notes = NoteModel.getNotesByTag(tagId)
    Future.value(serializeNotes(notes))
  }

  def deleteTagFromNote(tagId: Long, noteId: Long): Future[Unit] = {
    NoteModel.deleteTagFromNote(tagId, noteId)
    Future.value(Unit)
  }
}

trait ThriftUtil {
  def serializeTag(tag: TagModel): Tag = {
    Tag(tag.id, tag.name)
  }

  def serializeTags(tags: Seq[TagModel]): Seq[Tag] = {
    tags.map { tag => serializeTag(tag) }
  }

  def serializeNote(note: NoteModel): Note = {
    Note(note.id, note.name, serializeTags(TagModel.getTagsByNote(note.id)))
  }

  def serializeNotes(notes: Seq[NoteModel]): Seq[Note] = {
    notes.map { note => serializeNote(note) }
  }
}