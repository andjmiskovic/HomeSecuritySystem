from flask import Blueprint, render_template, redirect, url_for
from flask_login import current_user
from functools import wraps
from application.config import device_config

web = Blueprint('web', __name__)


def web_login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if current_user.is_authenticated:
            return f(*args, **kwargs)
        return redirect(url_for('web.login'))
    return decorated_function


@web.route('/login')
def login():
    return render_template('login.html')


@web.route('/')
@web_login_required
def dashboard():
    return render_template('dashboard.html', label=device_config['LABEL'], is_paired='DEVICE_ID' in device_config.get_config())
