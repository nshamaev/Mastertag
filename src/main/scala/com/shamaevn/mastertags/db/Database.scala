package com.shamaevn.mastertags.db

import scala.collection.mutable


object Database {
  val notes = new mutable.ArrayBuffer[NoteModel] with mutable.SynchronizedBuffer[NoteModel]
  val tags = new mutable.ArrayBuffer[TagModel] with mutable.SynchronizedBuffer[TagModel]
}



