import requests
import json
from flask import current_app
from application.details import device_details
from application.config import device_config

SERVICE_URL = "192.168.1.90:8001"
PAIRING_TIMEOUT = 65 # seconds

def request_pairing(code):
    headers = {
        'Content-Type': 'application/json'
    }

    device_info = {
        "name": device_details['NAME'],
        "manufacturer": device_details['MANUFACTURER'],
        "type": device_details['TYPE'],
        "macAddress": device_details['MAC_ADDRESS'],
        "label": device_config['DEVICE_LABEL']
    }

    current_app.logger.info(f"Requested pairing for code {code}")
    response = requests.post(
        f'http://localhost:8001/devices/handshake/device/{code}',
        json=device_info,
        headers=headers,
        timeout=PAIRING_TIMEOUT
    )

    if (response.status_code == requests.codes.ok):
        current_app.logger.info("Paired successful.")
        return json.loads(response.text)
    else:
        current_app.logger.error("Pairing timed out.")
        return None
        

def on_message(ws, message):
    print("Received message:", message)
