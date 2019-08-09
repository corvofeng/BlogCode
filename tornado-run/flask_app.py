# -*- coding: utf-8 -*-

"""

"""

from flask import Flask
app = Flask(__name__)


@app.route("/")
def hello():
    return "Flask: Hello world!"


app.secret_key = 'super secret key'
application = app
