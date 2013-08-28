package client

import concurrent.{Future, ExecutionContext}
import play.api.libs.ws.WS
import play.api.libs.json._
import scala.xml.Elem

/**
 * A libsyn audio feed item.
 * 
 * 
 * @param id  The id associated with the RSS entry.
 * @param content a Url to the audio
 */
case class LibsynAudio(
    id: String, 
    title: String, 
    link: String, 
    desc: String, 
    timestamp: String, 
    content: String)
object LibsynAudio {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val format = (
    (__ \ "id").format[String] and
    (__ \ "title").format[String] and
    (__ \ "link").format[String] and
    (__ \ "desc").format[String] and
    (__ \ "timestamp").format[String] and
    (__ \ "content").format[String]
  )(LibsynAudio.apply, unlift(LibsynAudio.unapply))  
}

object Libsyn {
  
  def readFeedRaw(url: String)(implicit ctx: ExecutionContext): Future[Elem] =
    WS.url(url).get.map(_.xml)
    
    
  def readFeed(url:String)(implicit ctx: ExecutionContext): Future[Seq[LibsynAudio]] = {
    val xml = readFeedRaw(url: String)
    xml map extractAudioFeed
  }
  
  
  def extractAudioFeed(xml: Elem): Seq[LibsynAudio] = {
    val items = xml \\ "item"
    for(item <- items)
    yield {
      val id = (item \\ "guid").text
      val title = (item \\ "title").text
      val link = (item \\ "link").text
      val desc = (item \\ "description").text
      val date = (item \\ "pubDate").text
      val enclosure = (item \\ "enclosure" \\ "@url").text
      LibsynAudio(id, title, link, desc, date, enclosure)
    }
  }
}