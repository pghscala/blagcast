package controllers

import play.api.mvc._
import play.api.libs.ws.{WS, Response}
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsValue

object HomeController extends Controller {
  
  def index = Action(Async {
    
    val videos = client.Youtube.grabUserFeed("UCHxNwi3l5CGZo1kG47k7i2Q")
      
    videos map { vs =>
      Ok(vs mkString "  ")
    }
  })
}