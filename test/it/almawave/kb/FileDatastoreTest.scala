package it.almawave.kb

import org.junit.Assert
import org.junit.Test
import java.net.URI
import java.nio.file.Paths
import java.nio.file.Files
import scala.io.Source
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class FileDatastoreTest {

  val fs = new FileDatastore("ontologies")

  @Test
  def test_local_relative_files {
    Assert.assertTrue(fs.list("owl", "ttl").size > 0)
  }

  @Test
  def test_local_uri() {
    val foaf_local_uri = new URI("file:///C:/Users/Al.Serafini/repos/DAF/lod_manager/ontologies/foaf/foaf.rdf")
    val list = fs.list("owl", "ttl", "rdf")
    Assert.assertTrue(list.contains(foaf_local_uri))
  }

  @Test
  def test_metadata {
    val list = fs.list("owl", "ttl", "rdf")

    // trying to get metadata, with error
    val uri_no = new URI("file:///not/existing/path/file.rdf")
    val meta_no = fs.getMetadata(uri_no)
    Assert.assertEquals(ConfigFactory.empty(), meta_no)

    // get metadata
    val uri = new URI("file:///C:/Users/Al.Serafini/repos/DAF/lod_manager/ontologies/foaf/foaf.rdf")
    val meta = fs.getMetadata(uri)
    Assert.assertEquals("foaf", meta.getString("prefix"))
    Assert.assertEquals("http://xmlns.com/foaf/0.1/", meta.getString("uri"))
    Assert.assertTrue(meta.getList("contexts").unwrapped().contains("http://xmlns.com/foaf/0.1/"))

  }

}