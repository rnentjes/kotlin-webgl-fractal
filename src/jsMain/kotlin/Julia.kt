import com.persesgames.shader.ShaderProgram
import com.persesgames.shader.VertextAttributeInfo
import kotlinx.browser.window
import kotlinx.html.InputType
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext
import kotlin.browser.window
import kotlin.js.Date
import kotlin.math.cos
import kotlin.math.sin

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
        v_coord = a_position * u_viewWindow.zw + u_viewWindow.xy;

        gl_Position = vec4(a_position, 0.0, 1.0);
    }
"""

private val fragmentShader = """
    precision mediump float;

    uniform vec2 u_julia;
    uniform float u_iteratorOffset;

    varying vec2 v_coord;

    void main(void) {
        float xx = v_coord.x;
        float yy = v_coord.y;
        float xt = 0.0;

        gl_FragColor = vec4( 0.0, 0.0, 0.0, 1.0);

        if (xx*xx + yy*yy < 4.0) {
            for (int iteration = 0; iteration < 1000; iteration++) {
                if (xx*xx + yy*yy > 4.0) {
                  float mu = u_iteratorOffset + float(iteration) + 1.0 - log(log(xx*xx + yy*yy)) / log(2.0);
                  //mu = sqrt(mu);

                  float it = mod(mu * 23.0, 768.0);

                  float red = min(it, 255.0) / 255.0;
                  float green = max(0.0, min(it, 511.0) - 256.0) / 255.0;
                  float blue = max(0.0, min(it, 767.0) - 512.0) / 255.0;

                  gl_FragColor = vec4( blue, green, red, 1.0);
                  break;
                }
                xt = xx*xx - yy*yy + u_julia.x;
                yy = 2.0*xx*yy + u_julia.y;
                xx = xt;
            }
        }
    }
"""

class JuliaData {
  var juliaX: Float = 0f
  var juliaY: Float = 0f

  var offsetX: Float = 0f
  var offsetY: Float = 0f
  var scaleX: Float = 1f
  var scaleY: Float = 1f

  var iteratorOffset: Float = 0f
}

class Julia(val html: HTMLElements) {
  val webgl = html.webgl
  val shaderProgram: ShaderProgram<JuliaData>
  val data: JuliaData = JuliaData()
  val attribBuffer: WebGLBuffer
  val vertices: Float32Array
  val start = Date().getTime()

  init {
    val array: Array<Float> = arrayOf(
      -1f, -1f,
      1f, -1f,
      1f, 1f,
      1f, 1f,
      -1f, 1f,
      -1f, -1f
    )

    vertices = Float32Array(array.size)
    vertices.set(array, 0)

    val setter = { program: ShaderProgram<JuliaData>, data: JuliaData ->
      program.setUniform2f("u_julia", data.juliaX, data.juliaY)
      program.setUniform4f("u_viewWindow", data.offsetX, data.offsetY, data.scaleX, data.scaleY)
      program.setUniform1f("u_iteratorOffset", data.iteratorOffset)
    }

    val vainfo = arrayOf(
      VertextAttributeInfo("a_position", 2)
    )

    shaderProgram = ShaderProgram(
      webgl,
      WebGLRenderingContext.TRIANGLES,
      vertexShader,
      fragmentShader,
      vainfo,
      setter
    )

    attribBuffer = webgl.createBuffer() ?: throw IllegalStateException(
      "Unable to create webgl buffer!"
    )
    webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer);
  }

  fun render() {
    html.resize()

    webgl.clearColor(1f, 1f, 1f, 1f)
    webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

    val time = ((Date().getTime()) - start) / 500.0

    data.juliaX = 0.28f + (sin(time / 31) / 100f).toFloat()
    data.juliaY = 0.008f + (cos(time / 23.07) / 100f).toFloat()

    val aspect = html.windowWidth / html.windowHeight.toFloat()

    data.scaleX = (1.3f - sin(time / 10.0).toFloat() * 0.5f) * aspect
    data.scaleY = 1.3f - sin(time / 10.0).toFloat() * 0.5f

    data.iteratorOffset = 0f //time.toFloat() * 10f

    shaderProgram.begin(attribBuffer, data)

    webgl.bufferData(
      WebGLRenderingContext.ARRAY_BUFFER,
      vertices,
      WebGLRenderingContext.DYNAMIC_DRAW
    );
    webgl.drawArrays(shaderProgram.drawType, 0, 6)

    shaderProgram.end()

    window.requestAnimationFrame {
      render()
    }
  }
}
