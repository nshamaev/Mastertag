import com.shamaevn.mastertags.tags.Note
import org.specs2._
import com.twitter.util.Await

class TagThriftServiceSpec extends Specification {
  var client = ThriftClient.buildClient()

  sequential

  def is = s2"""

   This is a specification to check Thift Interface

   The Thift Interface should
     add tag to note                                               $e1
     remove tag from note                                          $e2
     get tags by note                                              $e3
     get notes by tag                                              $e4
                                                                   """

  def e1 = {
    val note = Await.result(client.createNote("Test note"))
    val tag = Await.result(client.addTagToNote("Test tag", note.id))
    Await.result(client.getNote(note.id)).tags must contain(tag)
  }

  def e2 = {
    val note = Await.result(client.createNote("Test note"))
    val tag = Await.result(client.addTagToNote("Test tag", note.id))
    Await.result(client.deleteTagFromNote(tag.id, note.id))
    Await.result(client.getNote(note.id)).tags must not contain(tag)
  }

  def e3 = {
    val note = Await.result(client.createNote("Test note"))
    val tag1 = Await.result(client.addTagToNote("Test tag 1", note.id))
    val tag2 = Await.result(client.addTagToNote("Test tag 2", note.id))
    val tags = Await.result(client.getTagsByNote(note.id))
    tags must contain(tag1, tag2)
  }

  def e4 = {
    val note1 = Await.result(client.createNote("Test note1"))
    val note2 = Await.result(client.createNote("Test note2"))
    val note3 = Await.result(client.createNote("Test note3"))
    val tag = Await.result(client.addTagToNote("Special tag", note1.id))
    Await.result(client.addTagToNote(tag.name, note2.id))
    Await.result(client.addTagToNote(tag.name, note3.id))
    val notes = Await.result(client.getNotesByTag(tag.id))
    notes must contain (
      Note(note1.id, note1.name, Seq(tag)),
      Note(note2.id, note2.name, Seq(tag)),
      Note(note3.id, note3.name, Seq(tag))
    )
    //notes must haveLength(1)
  }
}