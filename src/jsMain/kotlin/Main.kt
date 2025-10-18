import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

/**
 * User: rnentjes
 * Date: 21-5-16
 * Time: 17:06
 */

class HTMLElements {
  val container: HTMLElement = document.createElement("div") as HTMLElement
  val canvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement
  var webgl: WebGLRenderingContext

  var windowWidth = 0
  var windowHeight = 0

  init {
    container.setAttribute("style", "position: relative;")
    canvas.setAttribute(
      "style",
      "position: absolute; left: 0px; top: 0px; z-index: 10; width: 100%; height: 100%;"
    )

    document.body!!.appendChild(container)
    container.appendChild(canvas)

    webgl = canvas.getContext("webgl") as WebGLRenderingContext
  }

  fun resize() {
    val windowWidth = window.innerWidth.toInt()
    val windowHeight = window.innerHeight.toInt()

    if (this.windowWidth != windowWidth ||
      this.windowHeight != windowHeight
    ) {

      this.windowWidth = windowWidth
      this.windowHeight = windowHeight
      canvas.setAttribute("width", "${windowWidth}px")
      canvas.setAttribute("height", "${windowHeight}px")
      canvas.setAttribute(
        "style",
        "position: absolute; left: 0px; top: 0px; z-index: 5; width: ${windowWidth}px; height: ${windowHeight}px;"
      )
      webgl.viewport(0, 0, windowWidth, windowHeight)
    }
  }

  fun render() {
    webgl.clearColor(1f, 1f, 1f, 1f)
    webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

//        window.requestAnimationFrame {
//            render()
//        }
  }
}

fun main() {
  val html = HTMLElements()

  val julia = Julia(html)

  julia.render()
}