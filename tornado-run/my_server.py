#!/usr/bin/env python
# -*- coding: utf-8 -*-
# vim: ts=4 sw=4 tw=99 et:

"""
@Date   : September 10, 2018
@Author : fengyuhao

USE tornado for wsgi container.
JUST for local test to fast reload and print pretty log.
DON'T use in Production Environment.

New version tornado doesn't support Python 2.7.3, need install an old version.
: pip install tornado==4.3

python my_server.py --port=8888 --debug=True

在程序中增加调试输出:
.. code-block:: python
    try:
        from tornado.options import options
    except:
        pass
    else:
        if options.debug:
            # 你可以做任何想做的事情, 而不用担心性能问题
            pass
"""

import logging
import tornado
import tornado.web
import tornado.wsgi
from tornado.options import options, define, parse_command_line
import tornado.httpserver

from flask_app import application
from uwsgi_app import application
from bottle_app import application
from django_app import application

define('port', type=int, default=8888)
define('debug', type=bool, default=False)

parse_command_line()
log = logging.getLogger("app")


def start_server():

    container = tornado.wsgi.WSGIContainer(application)
    settings = dict({
        'debug': options.debug,
        'autoreload': True,
    })
    # 主程序, 将所有请求发送到container中
    tornado_app = tornado.web.Application([
        ('.*', tornado.web.FallbackHandler, dict(fallback=container))
    ], **settings)

    server = tornado.httpserver.HTTPServer(tornado_app)
    server.listen(options.port)

    log.info('Start server at {}'.format(options.port))
    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":
    start_server()
