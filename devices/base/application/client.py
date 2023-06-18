import requests
import json
import random
from flask import current_app
from datetime import datetime, timezone
from application.details import device_details
from application.config import device_config
from application.crypto import cryptography_manager

alarm_simulation = False

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
        'publicKey': cryptography_manager.public_key_pem,
        'sensors': [{'name': s['name'], 'unit': s['unit']} for s in device_details['SENSORS']]
    }

    current_app.logger.info(f'Requested pairing for code {code}')
    response = requests.post(
        f'http://localhost:8001/devices/handshake/device/{code}',
        json=device_handshake_data,
        headers=headers,
        timeout=65  # seconds
    )

    print(response.text)
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
    if not device_config['DEVICE_ID']:
        current_app.logger.info(
            f'Device is not connected to SecureIT. Sending message skipped.')
        return

    message = {
        'measures': { sensor['name']: generate_measurement(sensor) for sensor in device_details['SENSORS']},
        'timestamp': str(datetime.now(timezone.utc)),
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

    if (response.status_code == requests.codes.ok):
        current_app.logger.info(f'Server received {json.loads(response.text)["message"]} bytes.')
    else:
        current_app.logger.info(f'Message denied. Invalid signature.')


def generate_measurement(sensor):
    key = 'alarm' if alarm_simulation else 'regular'

    if sensor['type'] == 'number':
        return random.gauss(
            sensor[key]['mu'],
            sensor[key]['sigma']
        )
    elif sensor['type'] == 'state':
        return sensor[key]
