namespace scala com.shamaevn.mastertags.tags


struct Tag {
  1: i64 id
  2: string name
}


struct Note {
  1: i64 id
  2: string name
  3: list<Tag> tags
}

service TagService {
  Note createNote(1: string noteName),
  Tag addTagToNote(1: string tagName, 2: i64 noteId),
  void deleteTagFromNote(1: i64 tagId, 2: i64 noteId)
  Note getNote(1: i64 noteId)
  list<Tag> getTagsByNote(1: i64 noteId)
  list<Note> getNotesByTag(1: i64 tagId)
}