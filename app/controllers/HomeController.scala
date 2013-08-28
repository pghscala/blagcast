package controllers

import play.api.mvc._
import play.api.libs.ws.{WS, Response}
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsValue
import Global._

object HomeController extends Controller {
  
  def index = Action(Async {
    for {
      casts <- fakeDataModelCall
    } yield Ok(views.html.podcastList("Scalawags", casts))
  })
  
  
  // Hacky method for testing...  In the future we'll just store
  // our podcasts inside 
  def fakeDataModelCall: Future[Seq[model.Episode]] = {
    val videos = client.Youtube.grabUserFeed(youtubeUser)
    val audios = client.Libsyn.readFeed(audioFeed)
    def join(pair: (client.LibsynAudio, client.YoutubeVideo)): model.Episode = {
      val (audio, video) = pair
      model.Episode(
        id = audio.id,
        title = audio.title,
        desc = audio.desc,
        timestamp = new java.util.Date(audio.timestamp),
        audio = Some(audio.link),
        video = Some(video.content)
      )
    }
    for {
      as <- audios
      vs <- videos
    } yield as zip vs map join
    
  }
  
  // TODO - Authenticate.
  def admin = Action(
     Ok(views.html.admin())
  )
  
  
  // TODO - Cache the audio rss?
  def audioRss = Action(Async {
    val result = WS.url(audioFeed).get
    result map (x => Ok(x.xml))
  })
}