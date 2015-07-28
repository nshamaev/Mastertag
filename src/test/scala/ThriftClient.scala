import com.twitter.finagle.Thrift
import com.shamaevn.mastertags.tags.TagService

object ThriftClient {
    def buildClient() = {
      Thrift.newIface[TagService.FutureIface]("localhost:8080")
    }
}
