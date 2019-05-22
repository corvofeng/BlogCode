
import urllib.parse
from tornado import httpclient
from tornado import escape
from tornado.auth import OAuth2Mixin
from tornado.httpclient import AsyncHTTPClient
from tornado.log import app_log

# Github 的oauth返回很慢, 如果使用tornado默认的client, 就会一直报599这样的错误
AsyncHTTPClient.configure("tornado.curl_httpclient.CurlAsyncHTTPClient")


class GithubOAuth2Mixin(OAuth2Mixin):
    _OAUTH_AUTHORIZE_URL = "https://github.com/login/oauth/authorize"
    _OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token"
    _OAUTH_USERINFO_URL = "https://api.github.com/user"

    async def get_authenticated_user(self, redirect_uri, client_id, client_secret, code):
        http_client = self.get_auth_http_client()

        body = urllib.parse.urlencode({
            "code": code,
            "client_id": client_id,
            "client_secret": client_secret,
        })

        try:
            response = await http_client.fetch(
                request=self._OAUTH_ACCESS_TOKEN_URL,
                raise_error=False,
                method="POST",
                headers={
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json',
                },
                body=body,
            )
            app_log.debug(response.body)
        except httpclient.HTTPError as e:
            # HTTPError is raised for non-200 responses; the response
            # can be found in e.response.
            app_log.error("Error: " + str(e))
            return None
        except Exception as e:
            # Other errors are possible, such as IOError.
            app_log.error("Error: " + str(e))
            return None

        try:
            token = escape.json_decode(response.body).get('access_token', None)
            if token is None:
                app_log.error("Get no token with %s", response.body)
                return
        except Exception as e:
            app_log.error("Parse json error with %s", response.body)
            return None

        try:
            user = await self.oauth2_request(
                self._OAUTH_USERINFO_URL,
                access_token=token
            )
        except Exception as e:
            app_log.error("Error: " + str(e))
            return None

        return user
