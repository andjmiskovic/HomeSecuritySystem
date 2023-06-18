from flask import Blueprint, jsonify, current_app, request
from werkzeug.security import check_password_hash, generate_password_hash
from flask_login import login_user, logout_user, login_required
from application.models import User, ApiResponse
from application.config import device_config
import application.client as client

api = Blueprint('api', __name__)


@api.after_request
def apply_json_content_type(response):
    response.headers['Content-Type'] = 'application/json'
    return response


@api.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    if not data or 'password' not in data:
        return ApiResponse('Missing password.', 400).to_json()
    password = data['password']

    if check_password_hash(device_config['PASSWORD_HASH'], password):
        user = User(1, 'admin')
        login_user(user)
        return ApiResponse('Access granted.', 200).to_json()

    return ApiResponse('Access denied.', 403).to_json()


@api.route('/logout')
def logout():
    logout_user()
    return ApiResponse('Logged out successfuly.', 200).to_json()


@api.route('/password', methods=['PUT'])
@login_required
def change_password():
    data = request.get_json()
    if not data or 'oldPassword' not in data or 'newPassword' not in data:
        return ApiResponse('Missing old or new password.', 400).to_json()
    old_password = data['oldPassword']
    new_password = data['newPassword']

    if check_password_hash(device_config['PASSWORD_HASH'], old_password):
        new_password_hash = generate_password_hash(new_password)
        device_config['PASSWORD_HASH'] = new_password_hash
        device_config.save()

        return ApiResponse('Password updated successfully.', 200).to_json()
    else:
        return ApiResponse('Wrong old password.', 403).to_json()


@api.route('/label', methods=['PUT'])
@login_required
def change_label():
    data = request.get_json()
    if not data or 'label' not in data:
        return ApiResponse('Missing label.', 400).to_json()
    new_label = data['label']

    device_config['LABEL'] = new_label
    device_config.save()

    return ApiResponse('Label updated successfully.', 200).to_json()


@api.route('/pair', methods=['POST'])
@login_required
def pair_device():
    data = request.get_json()
    if not data or 'code' not in data:
        return ApiResponse('Connection PIN is missing.', 400).to_json()

    code = data['code']
    result = client.request_pairing(code)

    if result:
        return ApiResponse('Pairing successful.', 200).to_json()
    else:
        return ApiResponse('Pairing timed out.', 408).to_json()
    

@api.route('/toggle-simulation', methods=['POST'])
def toggle_simulation():
    client.alarm_simulation = not client.alarm_simulation
    return ApiResponse(f"Simulation {'enabled' if client.alarm_simulation else 'disabled'}.", 200).to_json()


@api.route('/data')
def get_data():
    return client.display_data

@api.errorhandler(400)
def bad_request(e):
    return ApiResponse('Bad Request', 400).to_json()


@api.errorhandler(401)
def unauthorized(e):
    return ApiResponse('Unauthorized', 401).to_json()


@api.errorhandler(403)
def forbidden(e):
    return ApiResponse('Forbidden', 403).to_json()


@api.errorhandler(404)
def not_found(e):
    return ApiResponse('Not found', 404).to_json()


@api.errorhandler(405)
def method_not_allowed(e):
    return ApiResponse('Method not allowed', 405).to_json()


@api.errorhandler(500)
def internal_server_error(e):
    return ApiResponse('Internal Server Error', 500).to_json()
