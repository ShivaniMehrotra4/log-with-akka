package com.knoldus

import java.io.File

class ReadDirectory {
  def getListOfFile(dir: String): List[File] = {
    val file = new File(dir)
    if (file.exists && file.isDirectory) {
      file.listFiles.toList
    } else {
      List[File]()
    }
  }
}
