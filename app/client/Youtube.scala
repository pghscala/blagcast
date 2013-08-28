package client

import concurrent.{Future, ExecutionContext}
import play.api.libs.ws.WS
import play.api.libs.json._


/** A video we've parsed from youtube. */
case class YoutubeVideo(
    id: String, 
    title: String, 
    timestamp: String, 
    thumbnail: String, 
    content: String)
object YoutubeVideo {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val format = (
    (__ \ "id").format[String] and
    (__ \ "title").format[String] and
    (__ \ "timestamp").format[String] and
    (__ \ "thumbnail").format[String] and
    (__ \ "content").format[String]
  )(YoutubeVideo.apply, unlift(YoutubeVideo.unapply))  
}


object Youtube {
  
  def grabUserFeedJson(user: String)(implicit ctx: ExecutionContext): Future[JsValue] = {
    val url = s"https://gdata.youtube.com/feeds/api/users/${user}/uploads?alt=json"
    WS.url(url).get.map(_.json)
  }
  
  
  def grabUserFeed(user: String)(implicit ctx: ExecutionContext): Future[Seq[YoutubeVideo]] = {
    grabUserFeedJson(user).map(parseYoutubeVideos)
  }
  
  def parseYoutubeVideos(json: JsValue): Seq[YoutubeVideo] = {
    val feed = json \ "feed"
    val entries = feed \ "entry"
    entries.as[JsArray].value map parseJsonEntry
  }
  
  
  def parseJsonEntry(json: JsValue): YoutubeVideo = {
    
    val date: String = (json \ "published" \ "$t").as[String];
    
    val id =
      (json \ "id" \ "$t").as[String].split("/").last
    val title = (json \ "title" \ "$t").as[String]
    val mediaGroup = (json \ "media$group")
    val thumbnail =
      (mediaGroup \ "media$thumbnail").as[JsArray].value.head.\("url").as[String]
    val content =
      (mediaGroup \ "media$content").as[JsArray].value.head.\("url").as[String]
    YoutubeVideo(id, title, date, thumbnail, content)
  }
  
 
  def main(args: Array[String]): Unit = {
    import concurrent.Await
    import concurrent.ExecutionContext.Implicits.global
    import concurrent.duration._
    val result = client.Youtube.grabUserFeed("UCHxNwi3l5CGZo1kG47k7i2Q")
    println(Await.result(result, 2.minutes))
  }
}