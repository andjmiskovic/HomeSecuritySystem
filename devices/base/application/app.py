from flask import Flask
from flask_login import login_user, LoginManager
from application.models import User, ApiResponse
from application.api import api
from application.web import web
import logging

app = Flask(__name__)

app.register_blueprint(web)
app.register_blueprint(api, url_prefix='/api')

login_manager = LoginManager(app)


@login_manager.user_loader
def load_user(user_id):
    return User(user_id, 'admin')


app.logger.setLevel(logging.DEBUG)
formatter = logging.Formatter('%(asctime)s [%(levelname)s] %(message)s')
for handler in app.logger.handlers:
    handler.setFormatter(formatter)


@app.errorhandler(400)
def bad_request(e):
    return ApiResponse('Bad Request', 400).to_json()


@app.errorhandler(401)
def unauthorized(e):
    return ApiResponse('Unauthorized', 401).to_json()


@app.errorhandler(403)
def forbidden(e):
    return ApiResponse('Forbidden', 403).to_json()


@app.errorhandler(404)
def not_found(e):
    return ApiResponse('Not found', 404).to_json()


@app.errorhandler(405)
def method_not_allowed(e):
    return ApiResponse('Method not allowed', 405).to_json()


@app.errorhandler(500)
def internal_server_error(e):
    return ApiResponse('Internal Server Error', 500).to_json()
