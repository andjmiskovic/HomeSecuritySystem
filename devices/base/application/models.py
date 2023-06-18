from flask_login import UserMixin
from flask import jsonify


class User(UserMixin):
    def __init__(self, id, username):
        self.id = id
        self.username = username


class ApiResponse:
    def __init__(self, message, status, data=None):
        self.message = message
        self.status = status
        self.data = data

    def to_json(self):
        response_object = {
            'message': self.message,
            'status': self.status
        }
        if self.data is not None:
            response_object['data'] = self.data
        return jsonify(response_object), self.status
