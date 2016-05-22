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

    fun getColor(mu: Double) {
        var clr1 = mu.toInt()
        var t2 = mu - clr1
        var t1 = 1 - t2
        clr1 = clr1 % 768

//        int clr1 = (int)mu;
//        double t2 = mu - clr1;
//        double t1 = 1 - t2;
//        clr1 = clr1 % Colors.Count;
//        int clr2 = (clr1 + 1) % Colors.Count;
//
//        byte r = (byte)(Colors[clr1].R * t1 + Colors[clr2].R * t2);
//        byte g = (byte)(Colors[clr1].G * t1 + Colors[clr2].G * t2);
//        byte b = (byte)(Colors[clr1].B * t1 + Colors[clr2].B * t2);
//
//        return Color.FromArgb(255, r, g, b);
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

        var xs: Double
        var ys: Double
        var xx: Double
        var yy: Double
        var xt: Double
        var iteration: Int
        val max_iteration: Int = 767
        val halfWindowHeight = windowHeight / 2
        var red: Int
        var green: Int
        var blue: Int
        var fillStyle: String
        var mu: Double

        println("Window width: $windowWidth, height: $windowHeight, half: $halfWindowHeight")
        for (x in 0..windowWidth) {
            for (y in 0..halfWindowHeight) {
                xs = (4.0 / windowWidth.toFloat()) * x - 2.0
                ys = 2.0 - ((2.0 / halfWindowHeight) * y)

                xx = 0.0
                yy = 0.0
                iteration = 0
                while(xx*xx + yy*yy < 4 && iteration < max_iteration) {
                    xt = xx*xx - yy*yy + xs
                    yy = 2*xx*yy + ys
                    xx = xt
                    iteration++
                }
                if (iteration == max_iteration) {
                    fillStyle = "rgb(0, 0, 0)"
                } else {
                    mu = iteration + 1 -
                      Math.log(Math.log(xx*xx + yy*yy)) / Math.log(2.0);
                    iteration = (iteration * 13) % 768
                    red = Math.min(iteration, 255)
                    green = Math.max(0, Math.min(iteration, 511) - 256)
                    blue = Math.max(0, Math.min(iteration, 767) - 512)
                    fillStyle = "rgb($red, $green, $blue)"
                }

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