# 我的gulp学习之路(后端程序员的一次入坑)


> 本身, 我是比较抵制前端的, 因为实在太过庞大和杂乱, 后端已经做了这么久,
> 从心里也不想要改行, 但是最近在做Chrome插件, 涉及到打包, 依赖方面的东西
> 就完全不了解, 问问前端的同事也是杯水车薪, 故而自己开始学习gulp了.
> 因为我先了解的是后端, 建议大家在学习之前可以吧`Makefile`复习一下.


## gulp初体验


[入门纲要](http://www.gulpjs.com.cn/docs/getting-started/)

  这是我第一次使用`gulp`, 一步一步安装, 我也不知为何要全局安装结束之后在项目目录中继续安装,
学习gulp的目的只是为了打包, 这里暂时就不求甚解了, 

```bash

> npm install --global gulp     # 全局安装
> cd <项目目录>
> npm install --save-dev gulp
```

  刚开始学习也用不着插件那么高级的东西, 一个裸`gulpfile.js`就足够了
```js
var gulp = require('gulp');

gulp.task('default', function() {
    console.log("hello")
});
```

而后

```bash
☁  gulp_study [master] ⚡ gulp
[22:54:50] Using gulpfile ~/Learning/NodeJS/gulp_study/gulpfile.js
[22:54:50] Starting 'default'...
hello
[22:54:50] Finished 'default' after 139 μs
```

看了上述这个例子, 感觉gulp基本没什么作用, 还是需要自己写函数来跑


## gulp的稍稍入门

其实我们看看程序也能明白, 这里定义了一个任务`default`, 运行`gulp`命令的时候就是在执行
`default`任务对应的函数, 有了第一个任务, 当然可以有第二个任务

```js
var gulp = require('gulp');

gulp.task('default', function() {
  // 将你的默认的任务代码放在这
    console.log("helllo")
});
gulp.task('task2', function() {
  console.log("In task2")
});
```

怎么让gulp运行`task2`呢?

```bash
☁  gulp_study [master] ⚡ gulp task2 
[23:05:39] Using gulpfile ~/Learning/NodeJS/gulp_study/gulpfile.js
[23:05:39] Starting 'task2'...
In task2
[23:05:39] Finished 'task2' after 138 μs
```

## gulp中的任务搭配

> 有了第一个任务, 第二个任务, 自然有第三个, 第四个任务, 这些任务之间怎么搭配, 
> 或许说有什么先后的顺序?

`gulp.task`的原型如下`gulp.task(name[, deps], fn)`, 

`name`和`fn`我们已经写过了, 剩下的就是`deps`的形式了

```js
gulp.task('mytask', ['task1', 'task2', ...], function() {
  // 做一些事
});
```

通过看这个实例, 可以了解到`dep`具体的形式是一个数组, 这个数组由字符串组成.
而这些字符串, 代表了一个一个的任务, 表示, 当前任务需要依靠这些任务, 或者说,
是一种依赖关系

> 如果你恰巧是没有学过`Makefile`的后端同学, 那么, 还是去补习一下吧.
> 下面的扩展只适合于已经熟练编写`Makefile`的同学

也许你已经发现, 这个很像是`Makefile`. 没错, 作为`C/C++`爱好者, 
这个就是`Makefile`风格, 我来回顾一下`Makefile`的结构

```Makefile
CC := gcc
EXE := main

all: main.o
	${CC} $^ -o ${EXE}

main.o: main.c
	${CC} -c $^

.PHONEY:clean
clean:
	rm -vf ${EXE} main.o
```

是不是很亲切, `make all`与`gulp default`有异曲同工之妙, 而`all`: `main.o`中, 
`main.o`其实是作为依赖而存在的.

可以看到, `gulp`的任务组织方式其实是类似`Makefile`的, 当你有一个任务需要依赖其他任务时,
那么将其组织起来, 看来也十分的方便




