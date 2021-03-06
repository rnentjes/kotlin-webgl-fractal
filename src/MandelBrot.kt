import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.VertextAttributeInfo
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext
import kotlin.browser.window

/**
 * User: rnentjes
 * Date: 21-5-16
 * Time: 17:06
 */

private val vertexShader = """
    attribute vec2 a_position;

    uniform vec4 u_viewWindow;

    varying vec2 v_coord;

    void main(void) {
        v_coord = a_position + u_viewWindow.xy;

        gl_Position = vec4(a_position, 0.0, 1.0);
    }
"""

private val fragmentShader = """
    precision mediump float;

    varying vec2 v_coord;

    void main(void) {
        float xx = 0.0;
        float yy = 0.0;
        float xt = 0.0;

        gl_FragColor = vec4( 0.0, 0.0, 0.0, 1.0);

        for (int iteration = 0; iteration < 1000; iteration++) {
            if (xx*xx + yy*yy > 4.0) {
              float it = mod(float(iteration) * 13.0, 768.0);
              float red = min(it, 255.0) / 255.0;
              float green = max(0.0, min(it, 511.0) - 256.0);
              float blue = max(0.0, min(it, 767.0) - 512.0);
              gl_FragColor = vec4( red, green, blue, 1.0);
              break;
            }
            xt = xx*xx - yy*yy + v_coord.x;
            yy = 2.0*xx*yy + v_coord.y;
            xx = xt;
        }
    }
"""

class ShaderData {
    var offsetX: Float = 0f
    var offsetY: Float = 0f
}

class MandelBrot(val html: HTMLElements) {
    val webgl = html.webgl
    val shaderProgram: ShaderProgram<ShaderData>
    val data: ShaderData = ShaderData()
    val attribBuffer: WebGLBuffer
    val vertices: Float32Array
    val start = Date().getTime()

    init {
        val array: Array<Float> = arrayOf(
          -1f,-1f,
           1f,-1f,
           1f, 1f,
           1f, 1f,
          -1f, 1f,
          -1f,-1f
        )

        vertices = Float32Array(array.size)
        vertices.set(array, 0)

        val setter = { program: ShaderProgram<ShaderData>, data: ShaderData ->
            program.setUniform4f("u_viewWindow", data.offsetX, data.offsetY, 0f, 0f)
        }

        val vainfo = arrayOf(
          VertextAttributeInfo("a_position", 2)
        )

        shaderProgram = ShaderProgram(webgl, WebGLRenderingContext.TRIANGLES, vertexShader, fragmentShader, vainfo, setter)


        attribBuffer = webgl.createBuffer() ?: throw IllegalStateException("Unable to create webgl buffer!")
        webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer);
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

/*    fun drawMandel() {
        *//*
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
        }*//*

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

        for (x in 0..windowWidth) {
            for (y in 0..windowHeight) {
                xs = (4.0 / windowWidth.toFloat()) * x - 2.0
                ys = 4.0 - ((4.0 / windowHeight) * y) - 2.0

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
                    //mu = iteration + 1 - Math.log(Math.log(xx*xx + yy*yy)) / Math.log(2.0);
                    iteration = (iteration * 13) % 768
                    red = Math.min(iteration, 255)
                    green = Math.max(0, Math.min(iteration, 511) - 256)
                    blue = Math.max(0, Math.min(iteration, 767) - 512)
                    fillStyle = "rgb($red, $green, $blue)"
                }

                //canvas2d.fillStyle = fillStyle
                //canvas2d.fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
            }
        }

    }*/

/*    fun drawJulia(xc: Double, yc: Double) {
        var xx: Double
        var yy: Double
        var xt: Double
        var iteration: Int
        val max_iteration: Int = 511
        var red: Int
        var green: Int
        var blue: Int
        var fillStyle: String

        for (x in 0..windowWidth) {
            for (y in 0..windowHeight) {
                xx = (4.0 / windowWidth.toFloat()) * x - 2.0
                yy = 4.0 - ((4.0 / windowHeight) * y) - 2.0

                iteration = 0
                while(xx*xx + yy*yy < 4 && iteration < max_iteration) {
                    xt = xx*xx - yy*yy + xc
                    yy = 2*xx*yy + yc
                    xx = xt
                    iteration++
                }
                if (iteration == max_iteration) {
                    fillStyle = "rgb(0, 0, 0)"
                } else {
                    //mu = iteration + 1 - Math.log(Math.log(xx*xx + yy*yy)) / Math.log(2.0);
                    iteration = (iteration * 31) % 512
                    red = Math.min(iteration, 255)
                    green = Math.max(0, Math.min(iteration, 511) - 256)
                    blue = Math.max(0, Math.min(iteration, 767) - 512)
                    fillStyle = "rgb($red, $green, $blue)"
                }

                //canvas2d.fillStyle = fillStyle
                //canvas2d.fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
            }
        }

    }*/

    fun render() {
        html.resize()

        webgl.clearColor(1f, 1f, 1f, 1f)
        webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        var time = (start - (Date().getTime())) / 1000.0

        data.offsetX = Math.sin(time).toFloat()
        data.offsetY = Math.cos(time).toFloat()

        shaderProgram.begin(attribBuffer, data)

        webgl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, vertices, WebGLRenderingContext.DYNAMIC_DRAW);
        webgl.drawArrays(shaderProgram.drawType, 0, 6)

        shaderProgram.end()

        window.requestAnimationFrame {
            render()
        }
    }
}
