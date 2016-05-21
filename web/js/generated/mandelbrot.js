(function (Kotlin) {
  'use strict';
  var _ = Kotlin.defineRootPackage(null, /** @lends _ */ {
    HTMLElements: Kotlin.createClass(null, function () {
      var tmp$0, tmp$1, tmp$2, tmp$3;
      this.windowWidth = window.innerWidth | 0;
      this.windowHeight = window.innerHeight | 0;
      this.container = Kotlin.isType(tmp$0 = document.createElement('div'), HTMLElement) ? tmp$0 : Kotlin.throwCCE();
      this.canvas = Kotlin.isType(tmp$1 = document.createElement('canvas'), HTMLCanvasElement) ? tmp$1 : Kotlin.throwCCE();
      this.container.setAttribute('style', 'position: relative;');
      this.canvas.setAttribute('style', 'position: absolute; left: 0px; top: 0px; z-index: 10; width: 2000px; height: 1000px;');
      ((tmp$2 = document.body) != null ? tmp$2 : Kotlin.throwNPE()).appendChild(this.container);
      this.container.appendChild(this.canvas);
      this.canvas2d = Kotlin.isType(tmp$3 = this.canvas.getContext('2d'), CanvasRenderingContext2D) ? tmp$3 : Kotlin.throwCCE();
    }, /** @lends _.HTMLElements.prototype */ {
      resize: function () {
        this.windowWidth = window.innerWidth | 0;
        this.windowHeight = window.innerHeight | 0;
        this.canvas.setAttribute('width', this.windowWidth.toString() + 'px');
        this.canvas.setAttribute('height', this.windowHeight.toString() + 'px');
        this.canvas.setAttribute('style', 'position: absolute; left: 0px; top: 0px; z-index: 5; width: ' + this.windowWidth + 'px; height: ' + this.windowHeight + 'px;');
      },
      drawMandel: function () {
        var tmp$0, tmp$1;
        var xs;
        var ys;
        var xx;
        var yy;
        var xt;
        var iteration;
        var max_iteration = 511;
        var halfWindowHeight = this.windowHeight / 2 | 0;
        var red;
        var fillStyle;
        Kotlin.println('Window width: ' + this.windowWidth + ', height: ' + this.windowHeight + ', half: ' + halfWindowHeight);
        tmp$0 = this.windowWidth;
        for (var x = 0; x <= tmp$0; x++) {
          tmp$1 = halfWindowHeight;
          for (var y = 0; y <= tmp$1; y++) {
            xs = 3.5 / this.windowWidth * x - 2.5;
            ys = 1.0 - 1.0 / halfWindowHeight * y;
            xx = 0.0;
            yy = 0.0;
            iteration = 0;
            while (xx * xx + yy * yy < 4 && iteration < max_iteration) {
              xt = xx * xx - yy * yy + xs;
              yy = 2 * xx * yy + ys;
              xx = xt;
              iteration++;
            }
            fillStyle = 'rgb(' + iteration * 2 % 256 + ', ' + iteration * 3 % 256 + ', ' + iteration % 256 + ')';
            if (iteration === max_iteration) {
              fillStyle = 'rgb(0, 0, 0)';
            }
            this.canvas2d.fillStyle = fillStyle;
            this.canvas2d.fillRect(x, y, 1.0, 1.0);
            this.canvas2d.fillRect(x, this.windowHeight - y, 1.0, 1.0);
          }
        }
      }
    }),
    main_kand9s$: function (args) {
      var html = new _.HTMLElements();
      html.resize();
      html.drawMandel();
    }
  });
  Kotlin.defineModule('mandelbrot', _);
  _.main_kand9s$([]);
}(Kotlin));