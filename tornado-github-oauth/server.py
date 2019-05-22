# -*- coding: utf-8 -*-

"""

"""

import tornado.auth
import tornado.ioloop
import tornado.options
import tornado.web

from tornado.options import define, options
from tornado.log import app_log
from github_oauth import GithubOAuth2Mixin

define("port", default=8888, help="run on the given port", type=int)
define("github_client_id", help="your Github client id", type=str)
define("github_client_secret", help="your Github client secret", type=str)


class BaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        user_json = self.get_secure_cookie("github_user")
        if not user_json:
            return None
        return tornado.escape.json_decode(user_json)


class MainHandler(BaseHandler, GithubOAuth2Mixin):
    @tornado.web.authenticated
    async def get(self):

        if self.current_user is None:
            self.redirect("/auth/login")
            return
        app_log.info(self.current_user)
        self.write("Current user is {}".format(self.current_user['name']))


class AuthLoginHandler(BaseHandler, GithubOAuth2Mixin):
    async def get(self):
        my_url = "{}://{}{}".format(
            self.request.protocol, self.request.host, "/auth/login")
        app_log.info(my_url)

        if self.get_argument("code", False):
            user = await self.get_authenticated_user(
                redirect_uri=my_url,
                client_id=self.settings["github_client_id"],
                client_secret=self.settings["github_client_secret"],
                code=self.get_argument("code")
            )

            self.set_secure_cookie(
                "github_user", tornado.escape.json_encode(user))
            self.redirect(self.get_argument("next", "/"))
            return

        self.authorize_redirect(
            client_id=self.settings["github_client_id"],
            extra_params={"scope": "read:user"})


class AuthLogoutHandler(BaseHandler, GithubOAuth2Mixin):
    def get(self):
        self.clear_cookie("github_user")
        # self.redirect(self.get_argument("next", "/"))
        self.write("logout success")


class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", MainHandler),
            (r"/auth/login", AuthLoginHandler),
            (r"/auth/logout", AuthLogoutHandler),
        ]

        settings = dict(
            cookie_secret="__TODO:_GENERATE_YOUR_OWN_RANDOM_VALUE_HERE__",
            login_url="/auth/login",
            github_client_id=options.github_client_id,
            github_client_secret=options.github_client_secret,

            debug=True,
        )
        super(Application, self).__init__(handlers, **settings)


def main():
    tornado.options.parse_command_line()
    if not (options.github_client_id and options.github_client_secret):
        print("--github_client_id and --github_client_secret must be set")
        return
    http_server = tornado.httpserver.HTTPServer(Application())
    http_server.listen(options.port)

    app_log.info("Listen to %s", options.port)
    tornado.ioloop.IOLoop.current().start()


if __name__ == "__main__":
    main()
