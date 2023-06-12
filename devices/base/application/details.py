from flask import current_app
import json

class DeviceDetailsManager:
    DETAILS_PATH = 'config/details.json'
    DETAILS_KEY = 'DEVICE_DETAILS'

    def __init__(self, app):
        self.app = app

    def load(self):
        with open(self.DETAILS_PATH) as config_file:
            self.app.config[self.DETAILS_KEY] = json.load(config_file)

    def get(self, key):
        return self.app.config[self.DETAILS_KEY].get(key, None)

    def __getitem__(self, key):
        return self.get(key)

    def __setitem__(self, key, value):
        pass
    
    def get_details(self):
        return self.app.config[self.DETAILS_KEY]

device_details = DeviceDetailsManager(current_app)

def load_details():
    device_details.load()
