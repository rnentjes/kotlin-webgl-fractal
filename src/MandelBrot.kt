import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

/**
 * User: rnentjes
 * Date: 21-5-16
 * Time: 17:06
 */

class HTMLElements {
    val container: HTMLElement
    val canvas: HTMLCanvasElement
    val canvas2d: CanvasRenderingContext2D

    var windowWidth = window.innerWidth.toInt()
    var windowHeight = window.innerHeight.toInt()

    init {
        container = document.createElement("div") as HTMLElement

        canvas = document.createElement("canvas") as HTMLCanvasElement

        container.setAttribute("style", "position: relative;")
        canvas.setAttribute("style", "position: absolute; left: 0px; top: 0px; z-index: 10; width: 2000px; height: 1000px;" )

        document.body!!.appendChild(container)
        container.appendChild(canvas)

        canvas2d = canvas.getContext("2d") as CanvasRenderingContext2D
    }

    fun resize() {
        windowWidth = window.innerWidth.toInt()
        windowHeight = window.innerHeight.toInt()

        canvas.setAttribute("width", "${windowWidth}px")
        canvas.setAttribute("height", "${windowHeight}px")
        canvas.setAttribute("style", "position: absolute; left: 0px; top: 0px; z-index: 5; width: ${windowWidth}px; height: ${windowHeight}px;" )
    }

    fun drawMandel() {
        /*
        For each pixel (Px, Py) on the screen, do:
        {
          x0 = scaled x coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.5, 1))
          y0 = scaled y coordinate of pixel (scaled to lie in the Mandelbrot Y scale (-1, 1))
          x = 0.0
          y = 0.0
          iteration = 0
          max_iteration = 1000
          while (x*x + y*y < 2*2  AND  iteration < max_iteration) {
            xtemp = x*x - y*y + x0
            y = 2*x*y + y0
            x = xtemp
            iteration = iteration + 1
          }
          color = palette[iteration]
          plot(Px, Py, color)
        }*/

        var xs: Float
        var ys: Float
        var xx: Float
        var yy: Float
        var xt: Float
        var iteration: Int
        var max_iteration: Int = 511
        var halfWindowHeight = windowHeight / 2
        var red: Int
        var fillStyle: String

        println("Window width: $windowWidth, height: $windowHeight, half: $halfWindowHeight")
        for (x in 0..windowWidth) {
            for (y in 0..halfWindowHeight) {
                xs = (3.5f / windowWidth.toFloat()) * x - 2.5f
                ys = 1f - ((1f / halfWindowHeight) * y)

                xx = 0f
                yy = 0f
                iteration = 0
                while(xx*xx + yy*yy < 4 && iteration < max_iteration) {
                    xt = xx*xx - yy*yy + xs
                    yy = 2*xx*yy + ys
                    xx = xt
                    iteration++
                }
                fillStyle = "rgb(${(iteration * 2) % 256}, ${(iteration * 3) % 256}, ${(iteration) % 256})"
                if (iteration == max_iteration) {
                    fillStyle = "rgb(0, 0, 0)"
                }
                //red =
                canvas2d.fillStyle = fillStyle
                canvas2d.fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
                canvas2d.fillRect(x.toDouble(), windowHeight - y.toDouble(), 1.0, 1.0)
            }
        }

    }
}

fun main(args: Array<String>) {
    val html = HTMLElements()

    html.resize()

    html.drawMandel()
}