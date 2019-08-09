# -*- coding: utf-8 -*-

"""

"""

import os
from django.http import HttpResponse
from django.urls import include, path
from django.core.wsgi import get_wsgi_application


filepath, extension = os.path.splitext(__file__)
ROOT_URLCONF = os.path.basename(filepath)


def hello(request):
    return HttpResponse('Django: Hello world!')


urlpatterns = [
    path('',  hello)
]
os.environ["DJANGO_SETTINGS_MODULE"] = "django_app"
SECRET_KEY = "in test"

application = get_wsgi_application()
