package com.shamaevn.mastertags

import java.net.InetSocketAddress

object Application {
  def main(args: Array[String]) {
    ThriftServer.buildServer(new InetSocketAddress("localhost", 8080))
  }
}


