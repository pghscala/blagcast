package model

/**
 * An episode consists of an id, a title, a description, a timestamp,
 * and media types..
 */
case class Episode(
    id: String, 
    title: String, 
    desc: String, 
    timestamp: java.util.Date,
    audio: Option[String],
    video: Option[String]
) {
  // TODO - This belongs on the view side...
  def friendlyTimeString = {
    val df = new java.text.SimpleDateFormat("MMM dd")
    df.format(timestamp)
  }
}
object Episode {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  implicit val format = (
    (__ \ "id").format[String] and
    (__ \ "title").format[String] and
    (__ \ "desc").format[String] and
    (__ \ "timestamp").format[java.util.Date] and
    (__ \ "audio").format[Option[String]] and
    (__ \ "video").format[Option[String]]
  )(Episode.apply, unlift(Episode.unapply))  
}