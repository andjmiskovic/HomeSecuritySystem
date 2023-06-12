from flask import current_app
from secrets import token_hex
from datetime import timedelta
import json

class DeviceConfigManager:
    CONFIG_PATH = 'config/config.json'
    CONFIG_KEY = 'DEVICE_CONFIG'

    def __init__(self, app):
        self.app = app

    def load(self):
        with open(self.CONFIG_PATH) as config_file:
            self.app.config[self.CONFIG_KEY] = json.load(config_file)
    
    def save(self):
        with open(self.CONFIG_PATH, 'w') as config_file:
            json.dump(self.app.config[self.CONFIG_KEY], config_file, indent=4)
    
    def set(self, key, value):
        self.app.config[self.CONFIG_KEY][key] = value

    def get(self, key):
        return self.app.config[self.CONFIG_KEY].get(key, None)

    def __getitem__(self, key):
        return self.get(key)

    def __setitem__(self, key, value):
        self.set(key, value)
    
    def get_config(self):
        return self.app.config[self.CONFIG_KEY]

device_config = DeviceConfigManager(current_app)

def setup_config():
    device_config.load()

    if not device_config['APP_SECRET']:
        app_secret = token_hex(32)
        device_config['APP_SECRET'] = app_secret
        device_config.save()

    current_app.config['SECRET_KEY'] = device_config['APP_SECRET']
    current_app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(minutes=10)
