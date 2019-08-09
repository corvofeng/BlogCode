# -*- coding: utf-8 -*-

"""

"""

from bottle import Bottle, template


app = Bottle()


@app.route('/')
def index():
    return 'bottle: Hello world!'


application = app
