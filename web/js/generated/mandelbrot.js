(function (Kotlin) {
  'use strict';
  var _ = Kotlin.defineRootPackage(function () {
    this.vertexShader_rfnujb$ = '\n    attribute vec2 a_position;\n\n    uniform vec4 u_viewWindow;\n\n    varying vec2 v_coord;\n\n    void main(void) {\n        v_coord = a_position + u_viewWindow.xy;\n\n        gl_Position = vec4(a_position, 0.0, 1.0);\n    }\n';
    this.fragmentShader_jzx1nv$ = '\n    precision mediump float;\n\n    varying vec2 v_coord;\n\n    void main(void) {\n        float xx = 0.0;\n        float yy = 0.0;\n        float xt = 0.0;\n\n        gl_FragColor = vec4( 0.0, 0.0, 0.0, 1.0);\n\n        for (int iteration = 0; iteration < 1000; iteration++) {\n            if (xx*xx + yy*yy > 4.0) {\n              float it = mod(float(iteration) * 13.0, 768.0);\n              float red = min(it, 255.0) / 255.0;\n              float green = max(0.0, min(it, 511.0) - 256.0);\n              float blue = max(0.0, min(it, 767.0) - 512.0);\n              gl_FragColor = vec4( red, green, blue, 1.0);\n              break;\n            }\n            xt = xx*xx - yy*yy + v_coord.x;\n            yy = 2.0*xx*yy + v_coord.y;\n            xx = xt;\n        }\n    }\n';
    this.vertexShader_rfnujb$ = '\n    attribute vec2 a_position;\n\n    uniform vec4 u_viewWindow;\n\n    varying vec2 v_coord;\n\n    void main(void) {\n        v_coord = a_position * u_viewWindow.zw + u_viewWindow.xy;\n\n        gl_Position = vec4(a_position, 0.0, 1.0);\n    }\n';
    this.fragmentShader_jzx1nv$ = '\n    precision mediump float;\n\n    uniform vec2 u_julia;\n    uniform float u_iteratorOffset;\n\n    varying vec2 v_coord;\n\n    void main(void) {\n        float xx = v_coord.x;\n        float yy = v_coord.y;\n        float xt = 0.0;\n\n        gl_FragColor = vec4( 0.0, 0.0, 0.0, 1.0);\n\n        for (int iteration = 0; iteration < 1000; iteration++) {\n            if (xx*xx + yy*yy > 4.0) {\n              float mu = u_iteratorOffset + float(iteration) + 1.0 - log(log(xx*xx + yy*yy)) / log(2.0);\n\n              float it = mod(mu * 7.0, 768.0);\n\n              float red = min(it, 255.0) / 255.0;\n              float green = max(0.0, min(it, 511.0) - 256.0) / 255.0;\n              float blue = max(0.0, min(it, 767.0) - 512.0) / 255.0;\n\n              gl_FragColor = vec4( red, green, blue, 1.0);\n              break;\n            }\n            xt = xx*xx - yy*yy + u_julia.x;\n            yy = 2.0*xx*yy + u_julia.y;\n            xx = xt;\n        }\n    }\n';
  }, /** @lends _ */ {
    com: Kotlin.definePackage(null, /** @lends _.com */ {
      persesgames: Kotlin.definePackage(null, /** @lends _.com.persesgames */ {
        shader: Kotlin.definePackage(null, /** @lends _.com.persesgames.shader */ {
          VertextAttributeInfo: Kotlin.createClass(null, function (locationName, numElements) {
            this.locationName = locationName;
            this.numElements = numElements;
            this.location = 0;
            this.offset = 0;
          }),
          ShaderProgram: Kotlin.createClass(null, function (webgl, drawType, vertexShaderSource, fragmentShaderSource, vainfo, setter) {
            var tmp$0, tmp$1, tmp$2;
            this.webgl = webgl;
            this.drawType = drawType;
            this.vainfo = vainfo;
            this.setter = setter;
            this.verticesBlockSize = 0;
            this.drawLength = 0;
            this.vertex = this.compileShader(vertexShaderSource, WebGLRenderingContext.VERTEX_SHADER);
            this.fragment = this.compileShader(fragmentShaderSource, WebGLRenderingContext.FRAGMENT_SHADER);
            tmp$0 = this.webgl.createProgram();
            if (tmp$0 == null)
              throw new Kotlin.IllegalStateException('Unable to request shader program from webgl context!');
            this.shaderProgram = tmp$0;
            this.webgl.attachShader(this.shaderProgram, this.vertex);
            this.webgl.attachShader(this.shaderProgram, this.fragment);
            this.webgl.linkProgram(this.shaderProgram);
            if (Kotlin.equals(this.webgl.getProgramParameter(this.shaderProgram, WebGLRenderingContext.LINK_STATUS), false)) {
              Kotlin.println(this.webgl.getProgramInfoLog(this.shaderProgram));
              throw new Kotlin.IllegalStateException('Unable to compile shader program!');
            }
            this.webgl.useProgram(this.shaderProgram);
            this.verticesBlockSize = 0;
            tmp$1 = Kotlin.modules['stdlib'].kotlin.collections.iterator_123wqf$(Kotlin.arrayIterator(this.vainfo));
            while (tmp$1.hasNext()) {
              var info = tmp$1.next();
              info.location = this.webgl.getAttribLocation(this.shaderProgram, info.locationName);
              info.offset = this.verticesBlockSize;
              this.verticesBlockSize += info.numElements;
              Kotlin.println('attrib: ' + info.locationName + ', info.location: ' + info.location + ', info.offset: ' + info.offset);
            }
            tmp$2 = this.drawType;
            if (tmp$2 === WebGLRenderingContext.TRIANGLES)
              this.drawLength = this.verticesBlockSize * 3;
            else {
              this.drawLength = this.verticesBlockSize;
            }
            Kotlin.println('verticesBlockSize ' + this.verticesBlockSize);
            Kotlin.println('ShaderProgram constructor done');
          }, /** @lends _.com.persesgames.shader.ShaderProgram.prototype */ {
            compileShader: function (source, type) {
              var tmp$0;
              var result;
              tmp$0 = this.webgl.createShader(type);
              if (tmp$0 == null)
                throw new Kotlin.IllegalStateException('Unable to request shader from webgl context!');
              result = tmp$0;
              this.webgl.shaderSource(result, source);
              this.webgl.compileShader(result);
              if (Kotlin.equals(this.webgl.getShaderParameter(result, WebGLRenderingContext.COMPILE_STATUS), false)) {
                throw new Kotlin.IllegalStateException('Unable to compile shader!' + '\n' + source + '\n' + '\n' + Kotlin.toString(this.webgl.getShaderInfoLog(result)));
              }
              return result;
            },
            begin_t0l48p$: function (attribBuffer, userdata) {
              var tmp$0;
              this.webgl.useProgram(this.shaderProgram);
              this.webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer);
              tmp$0 = Kotlin.modules['stdlib'].kotlin.collections.iterator_123wqf$(Kotlin.arrayIterator(this.vainfo));
              while (tmp$0.hasNext()) {
                var info = tmp$0.next();
                this.webgl.enableVertexAttribArray(info.location);
                this.webgl.vertexAttribPointer(info.location, info.numElements, WebGLRenderingContext.FLOAT, false, this.verticesBlockSize * 4, info.offset * 4);
              }
              this.setter(this, userdata);
            },
            end: function () {
              var tmp$0;
              tmp$0 = Kotlin.modules['stdlib'].kotlin.collections.iterator_123wqf$(Kotlin.arrayIterator(this.vainfo));
              while (tmp$0.hasNext()) {
                var info = tmp$0.next();
                this.webgl.disableVertexAttribArray(info.location);
              }
              this.webgl.useProgram(null);
            },
            getAttribLocation_61zpoe$: function (location) {
              return this.webgl.getAttribLocation(this.shaderProgram, location);
            },
            getUniformLocation_61zpoe$: function (location) {
              return this.webgl.getUniformLocation(this.shaderProgram, location);
            },
            setUniform1f_9sobi5$: function (location, value) {
              this.webgl.uniform1f(this.getUniformLocation_61zpoe$(location), value);
            },
            setUniform2f_9xt0da$: function (location, v1, v2) {
              this.webgl.uniform2f(this.getUniformLocation_61zpoe$(location), v1, v2);
            },
            setUniform4f_kjn4ou$: function (location, v1, v2, v3, v4) {
              this.webgl.uniform4f(this.getUniformLocation_61zpoe$(location), v1, v2, v3, v4);
            },
            setUniform1i_bm4lxs$: function (location, value) {
              this.webgl.uniform1i(this.getUniformLocation_61zpoe$(location), value);
            },
            setUniformMatrix4fv_pphpxd$: function (location, value) {
              this.webgl.uniformMatrix4fv(this.getUniformLocation_61zpoe$(location), false, value);
            }
          })
        })
      })
    }),
    ShaderData: Kotlin.createClass(null, function () {
      this.offsetX = 0.0;
      this.offsetY = 0.0;
    }),
    MandelBrot: Kotlin.createClass(null, function (html) {
      var tmp$0;
      this.html = html;
      this.webgl = this.html.webgl;
      this.data = new _.ShaderData();
      this.start = (new Date()).getTime();
      var array = [-1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, -1.0];
      this.vertices = new Float32Array(array.length);
      this.vertices.set(array, 0);
      var setter = _.MandelBrot.MandelBrot$f;
      var vainfo = [new _.com.persesgames.shader.VertextAttributeInfo('a_position', 2)];
      this.shaderProgram = new _.com.persesgames.shader.ShaderProgram(this.webgl, WebGLRenderingContext.TRIANGLES, _.vertexShader_rfnujb$, _.fragmentShader_jzx1nv$, vainfo, setter);
      tmp$0 = this.webgl.createBuffer();
      if (tmp$0 == null)
        throw new Kotlin.IllegalStateException('Unable to create webgl buffer!');
      this.attribBuffer = tmp$0;
      this.webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, this.attribBuffer);
    }, /** @lends _.MandelBrot.prototype */ {
      getColor_14dthe$: function (mu) {
        var clr1 = mu | 0;
        var t2 = mu - clr1;
        var t1 = 1 - t2;
        clr1 = clr1 % 768;
      },
      render: function () {
        this.html.resize();
        this.webgl.clearColor(1.0, 1.0, 1.0, 1.0);
        this.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT);
        var time = (this.start - (new Date()).getTime()) / 1000.0;
        this.data.offsetX = Math.sin(time);
        this.data.offsetY = Math.cos(time);
        this.shaderProgram.begin_t0l48p$(this.attribBuffer, this.data);
        this.webgl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, this.vertices, WebGLRenderingContext.DYNAMIC_DRAW);
        this.webgl.drawArrays(this.shaderProgram.drawType, 0, 6);
        this.shaderProgram.end();
        window.requestAnimationFrame(_.MandelBrot.render$f(this));
      }
    }, /** @lends _.MandelBrot */ {
      render$f: function (this$MandelBrot) {
        return function (it) {
          this$MandelBrot.render();
        };
      },
      MandelBrot$f: function (program, data) {
        program.setUniform4f_kjn4ou$('u_viewWindow', data.offsetX, data.offsetY, 0.0, 0.0);
      }
    }),
    HTMLElements: Kotlin.createClass(null, function () {
      var tmp$0, tmp$1, tmp$2, tmp$3;
      this.windowWidth = 0;
      this.windowHeight = 0;
      this.container = Kotlin.isType(tmp$0 = document.createElement('div'), HTMLElement) ? tmp$0 : Kotlin.throwCCE();
      this.canvas = Kotlin.isType(tmp$1 = document.createElement('canvas'), HTMLCanvasElement) ? tmp$1 : Kotlin.throwCCE();
      this.container.setAttribute('style', 'position: relative;');
      this.canvas.setAttribute('style', 'position: absolute; left: 0px; top: 0px; z-index: 10; width: 2000px; height: 1000px;');
      ((tmp$2 = document.body) != null ? tmp$2 : Kotlin.throwNPE()).appendChild(this.container);
      this.container.appendChild(this.canvas);
      this.webgl = Kotlin.isType(tmp$3 = this.canvas.getContext('webgl'), WebGLRenderingContext) ? tmp$3 : Kotlin.throwCCE();
    }, /** @lends _.HTMLElements.prototype */ {
      resize: function () {
        var windowWidth = window.innerWidth | 0;
        var windowHeight = window.innerHeight | 0;
        if (this.windowWidth !== windowWidth || this.windowHeight !== windowHeight) {
          this.windowWidth = windowWidth;
          this.windowHeight = windowHeight;
          this.canvas.setAttribute('width', windowWidth.toString() + 'px');
          this.canvas.setAttribute('height', windowHeight.toString() + 'px');
          this.canvas.setAttribute('style', 'position: absolute; left: 0px; top: 0px; z-index: 5; width: ' + windowWidth + 'px; height: ' + windowHeight + 'px;');
          this.webgl.viewport(0, 0, windowWidth, windowHeight);
        }
      },
      getColor_14dthe$: function (mu) {
        var clr1 = mu | 0;
        var t2 = mu - clr1;
        var t1 = 1 - t2;
        clr1 = clr1 % 768;
      },
      drawMandel: function () {
        var tmp$0, tmp$1;
        var xs;
        var ys;
        var xx;
        var yy;
        var xt;
        var iteration;
        var max_iteration = 767;
        var halfWindowHeight = this.windowHeight / 2 | 0;
        var red;
        var green;
        var blue;
        var fillStyle;
        var mu;
        tmp$0 = this.windowWidth;
        for (var x = 0; x <= tmp$0; x++) {
          tmp$1 = this.windowHeight;
          for (var y = 0; y <= tmp$1; y++) {
            xs = 4.0 / this.windowWidth * x - 2.0;
            ys = 4.0 - 4.0 / this.windowHeight * y - 2.0;
            xx = 0.0;
            yy = 0.0;
            iteration = 0;
            while (xx * xx + yy * yy < 4 && iteration < max_iteration) {
              xt = xx * xx - yy * yy + xs;
              yy = 2 * xx * yy + ys;
              xx = xt;
              iteration++;
            }
            if (iteration === max_iteration) {
              fillStyle = 'rgb(0, 0, 0)';
            }
             else {
              iteration = iteration * 13 % 768;
              red = Math.min(iteration, 255);
              green = Math.max(0, Math.min(iteration, 511) - 256);
              blue = Math.max(0, Math.min(iteration, 767) - 512);
              fillStyle = 'rgb(' + red + ', ' + green + ', ' + blue + ')';
            }
          }
        }
      },
      drawJulia_lu1900$: function (xc, yc) {
        var tmp$0, tmp$1;
        var xx;
        var yy;
        var xt;
        var iteration;
        var max_iteration = 511;
        var red;
        var green;
        var blue;
        var fillStyle;
        tmp$0 = this.windowWidth;
        for (var x = 0; x <= tmp$0; x++) {
          tmp$1 = this.windowHeight;
          for (var y = 0; y <= tmp$1; y++) {
            xx = 4.0 / this.windowWidth * x - 2.0;
            yy = 4.0 - 4.0 / this.windowHeight * y - 2.0;
            iteration = 0;
            while (xx * xx + yy * yy < 4 && iteration < max_iteration) {
              xt = xx * xx - yy * yy + xc;
              yy = 2 * xx * yy + yc;
              xx = xt;
              iteration++;
            }
            if (iteration === max_iteration) {
              fillStyle = 'rgb(0, 0, 0)';
            }
             else {
              iteration = iteration * 31 % 512;
              red = Math.min(iteration, 255);
              green = Math.max(0, Math.min(iteration, 511) - 256);
              blue = Math.max(0, Math.min(iteration, 767) - 512);
              fillStyle = 'rgb(' + red + ', ' + green + ', ' + blue + ')';
            }
          }
        }
      },
      render: function () {
        this.webgl.clearColor(1.0, 1.0, 1.0, 1.0);
        this.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT);
      }
    }),
    main_kand9s$: function (args) {
      var html = new _.HTMLElements();
      var mandelBrot = new _.MandelBrot(html);
      var julia = new _.Julia(html);
      julia.render();
    },
    JuliaData: Kotlin.createClass(null, function () {
      this.juliaX = 0.0;
      this.juliaY = 0.0;
      this.offsetX = 0.0;
      this.offsetY = 0.0;
      this.scaleX = 1.0;
      this.scaleY = 1.0;
      this.iteratorOffset = 0.0;
    }),
    Julia: Kotlin.createClass(null, function (html) {
      var tmp$0;
      this.html = html;
      this.webgl = this.html.webgl;
      this.data = new _.JuliaData();
      this.start = (new Date()).getTime() - 20000;
      var array = [-1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, -1.0];
      this.vertices = new Float32Array(array.length);
      this.vertices.set(array, 0);
      var setter = _.Julia.Julia$f;
      var vainfo = [new _.com.persesgames.shader.VertextAttributeInfo('a_position', 2)];
      this.shaderProgram = new _.com.persesgames.shader.ShaderProgram(this.webgl, WebGLRenderingContext.TRIANGLES, _.vertexShader_rfnujb$, _.fragmentShader_jzx1nv$, vainfo, setter);
      tmp$0 = this.webgl.createBuffer();
      if (tmp$0 == null)
        throw new Kotlin.IllegalStateException('Unable to create webgl buffer!');
      this.attribBuffer = tmp$0;
      this.webgl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, this.attribBuffer);
    }, /** @lends _.Julia.prototype */ {
      render: function () {
        this.html.resize();
        this.webgl.clearColor(1.0, 1.0, 1.0, 1.0);
        this.webgl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT);
        var time = (this.start - (new Date()).getTime()) / 1000.0;
        this.data.juliaX = -0.39100000262260437 + Math.sin(time / 31) / 10.0;
        this.data.juliaY = -0.5870000123977661 + Math.cos(time / 23.07) / 10.0;
        this.data.scaleX = 1.2999999523162842 - Math.sin(time / 10.0) * 0.8999999761581421;
        this.data.scaleY = 1.2999999523162842 - Math.sin(time / 10.0) * 0.8999999761581421;
        this.data.iteratorOffset = 0.0;
        this.shaderProgram.begin_t0l48p$(this.attribBuffer, this.data);
        this.webgl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, this.vertices, WebGLRenderingContext.DYNAMIC_DRAW);
        this.webgl.drawArrays(this.shaderProgram.drawType, 0, 6);
        this.shaderProgram.end();
        window.requestAnimationFrame(_.Julia.render$f(this));
      }
    }, /** @lends _.Julia */ {
      render$f: function (this$Julia) {
        return function (it) {
          this$Julia.render();
        };
      },
      Julia$f: function (program, data) {
        program.setUniform2f_9xt0da$('u_julia', data.juliaX, data.juliaY);
        program.setUniform4f_kjn4ou$('u_viewWindow', data.offsetX, data.offsetY, data.scaleX, data.scaleY);
        program.setUniform1f_9sobi5$('u_iteratorOffset', data.iteratorOffset);
      }
    })
  });
  Kotlin.defineModule('mandelbrot', _);
  _.main_kand9s$([]);
}(Kotlin));
