import requests
import json
from flask import current_app
from application.details import device_details
from application.config import device_config
from application.crypto import cryptography_manager

PAIRING_TIMEOUT = 65 # seconds

headers = {
    'Content-Type': 'application/json'
}

def request_pairing(code):
    device_handshake_data = {
        'name': device_details['NAME'],
        'manufacturer': device_details['MANUFACTURER'],
        'type': device_details['TYPE'],
        'macAddress': device_details['MAC_ADDRESS'],
        'label': device_config['LABEL'],
        'publicKey': cryptography_manager.public_key_pem
    }

    current_app.logger.info(f'Requested pairing for code {code}')
    response = requests.post(
        f'http://localhost:8001/devices/handshake/device/{code}',
        json=device_handshake_data,
        headers=headers,
        timeout=PAIRING_TIMEOUT
    )

    response_data = json.loads(response.text)
    device_config['DEVICE_ID'] = response_data['deviceId']
    device_config.save()

    if (response.status_code == requests.codes.ok):
        current_app.logger.info('Paired successfully.')
        return json.loads(response.text)
    else:
        current_app.logger.error('Pairing timed out.')
        return None


def send_message():
    message = {
        'data': f'Hello from {device_config.label}!',
        'deviceId': device_config.device_id
    }

    message_bytes = json.dumps(message).encode('utf-8')
    params = {
        'signature': cryptography_manager.sign(message_bytes)
    }

    response = requests.post(
        f'http://localhost:8001/monitor/send',
        json=message,
        params=params,
        headers=headers
    )